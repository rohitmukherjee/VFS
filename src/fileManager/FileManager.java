package fileManager;

import java.io.IOException;
import java.util.Arrays;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import utils.BlockSettings;
import virtualDisk.BlockManager;
import exceptions.FileOrDirectoryNotFoundException;

public class FileManager implements FileManagerInterface {

	private BlockManager blockManager;

	Logger logger;

	public FileManager(String path) {
		logger = Logger.getLogger(FileManager.class);
		BasicConfigurator.configure();
		blockManager = new BlockManager(path);
		try {
			blockManager.setupBlocks();
		} catch (Exception e) {
			logger.error("Couldn't setup blocks inside disk");
		}
	}

	public void writeRoot(MetaData meta) throws Exception {
		meta.setBlockNumber(blockManager.getNextFreeBlock());
		// logger.warn("Position of root is " + meta.getBlockNumber());
		blockManager.write(meta.getBytes());
	}

	@Override
	public void writeMetaData(long position, MetaData metaData)
			throws IOException, Exception {
		long dataBlockNumber = blockManager.getNextBlock(metaData
				.getBlockNumber());
		metaData.setBlockNumber(position);
		blockManager.write(metaData.getBytes(), position);
		blockManager.combineBlocks(metaData.getBlockNumber(), dataBlockNumber);
	}

	@Override
	public void writeReplaceMetaData(MetaData metaData) throws IOException,
			Exception {
		long dataPosition = blockManager
				.getNextBlock(metaData.getBlockNumber());
		blockManager.write(metaData.getBytes(), metaData.getBlockNumber());
		metaData.setBlockNumber(blockManager.getNextFreeBlock());
		blockManager.write(metaData.getBytes(), metaData.getBlockNumber());
		blockManager.combineBlocks(metaData.getBlockNumber(), dataPosition);
	}

	@Override
	public MetaData getMetaData(long blockNumber) throws IOException, Exception {
		return new MetaData(blockManager.readBlock(blockNumber));
	}

	@Override
	public MetaData getMetaData(MetaData metaData) throws Exception {
		return new MetaData(blockManager.readBlock(metaData.getBlockNumber()));
	}

	@Override
	public void createFile(MetaData meta, byte[] data) throws Exception {
		// Write the metadata and the data, then combines the blocks
		meta.setBlockNumber(blockManager.getNextFreeBlock());
		// * BlockSettings.BLOCK_SIZE);
		long position = meta.getBlockNumber();
		byte[] compressed = MetaDataUtilities.getCompressedBytes(data);
		byte[] toWrite = MetaDataUtilities.getEncryptedBytes(compressed);
		blockManager.write(meta.getBytes(), meta.getBlockNumber());
		long dataPosition = blockManager.getNextFreeBlock();
		blockManager.write(data, dataPosition);
		blockManager.combineBlocks(meta.getBlockNumber(), dataPosition);
		// logger.warn("pos{ " + meta.getBlockNumber());
		addToParent(getMetaData(meta.getParent()), meta.getBlockNumber());
		// grabs the parents and updates those values
	}

	private void addToParent(MetaData parent, long position) throws Exception {
		byte[] parentDataRaw = getData(parent);
		long[] parentData = MetaDataUtilities.getLongArray(parentDataRaw);
		long[] newData = new long[1];
		newData[0] = position;
		long[] parentDataNew = MetaDataUtilities.concaLongArrays(newData,
				parentData);
		byte[] parentDataToWrite = MetaDataUtilities
				.getByteArray(parentDataNew);
		long newParentDataBlock = blockManager.getNextFreeBlock();
		blockManager.write(parentDataToWrite);
		// Have to find a way of deleting the unused space to prevent
		// unnecessary growing
		blockManager.combineBlocks(parent.getBlockNumber(), newParentDataBlock);
	}

	@Override
	public MetaData search(String path) throws Exception {
		String[] pathComponents = path.split("/");
		MetaData root = getMetaData(0);
		// Incase the path is only one word, return root
		if (pathComponents.length == 1)
			return root;
		return rsearch(root, 1, pathComponents);
	}

	private MetaData rsearch(MetaData metaData, int location,
			String[] pathComponents) throws Exception {
		MetaData[] contents = getChildrenMeta(metaData);
		for (int i = 0; i < contents.length; ++i) {
			System.out.println(" Looking at " + contents[i].getName()
					+ " at position " + contents[i].getBlockNumber()
					+ " and type " + contents[i].getType());
			if (location == pathComponents.length - 1) {
				// looking for a file now
				if (contents[i].getType() == BlockSettings.FILE_TYPE
						&& contents[i].getName().equals(
								pathComponents[location])) {
					return contents[i];
				} else if (contents[i].getType() == BlockSettings.DIRECTORY_TYPE
						&& contents[i].getName().equals(
								pathComponents[location])) {
					return contents[i];
				}
			} else {
				// looking for a directory
				if (contents[i].getType() == BlockSettings.DIRECTORY_TYPE
						&& contents[i].getName().equals(
								pathComponents[location])) {
					return rsearch(contents[i], location + 1, pathComponents);
				}
			}
		}
		throw new FileOrDirectoryNotFoundException();
	}

	public MetaData[] getChildrenMeta(MetaData metaData) throws Exception {
		byte[] data = getData(metaData);
		MetaData[] result = new MetaData[data.length / 8];
		for (int i = 0; i < result.length; ++i) {
			result[i] = getMetaData(MetaDataUtilities.getLong(data, i * 8));
		}
		return result;
	}

	@Override
	public byte[] getData(MetaData metaData) throws Exception {
		// Limited to one block, so only metaData and no data
		if (blockManager.getNextBlock(metaData.getBlockNumber()) == 0) {
			return new byte[0];
		}

		byte[] tempData = blockManager.read(blockManager.getNextBlock(metaData
				.getBlockNumber()));
		// return MetaDataUtilities.getDecryptedBytes(MetaDataUtilities
		// .getDecompressedBytes(tempData));
		return tempData;
	}

	@Override
	public void deleteFile(MetaData metaData) throws Exception {
		MetaData parent = getMetaData(metaData.getParent());

		long positionToRemove = metaData.getBlockNumber();

		byte[] parentDataRaw = getData(parent);
		long[] parentData = MetaDataUtilities.getLongArray(parentDataRaw);
		long[] newData = new long[parentData.length - 1];
		int location = Arrays.binarySearch(parentData, positionToRemove);
		for (int i = 0; i < location; ++i) {
			newData[i] = parentData[i];
		}
		for (int i = location + 1; i < parentData.length; ++i) {
			newData[i - 1] = parentData[i];
		}
		byte[] parentDataToWrite = MetaDataUtilities.getByteArray(newData);
		long parentDataLocation = blockManager.getNextFreeBlock();
		blockManager.write(parentDataToWrite);
		blockManager.combineBlocks(parent.getBlockNumber(), parentDataLocation);

		blockManager.delete(metaData.getBlockNumber());
	}

	@Override
	public void writeData(MetaData meta, byte[] data) throws Exception {
		blockManager.delete(blockManager.getNextBlock(meta.getBlockNumber()));
		long dataPosition = blockManager.getNextFreeBlock();
		byte[] compressed = MetaDataUtilities.getCompressedBytes(data);
		byte[] toWrite = MetaDataUtilities.getEncryptedBytes(compressed);
		blockManager.write(data, dataPosition);
		blockManager.combineBlocks(meta.getBlockNumber(), dataPosition);

	}

	public long getFreeMemory() throws IOException {
		return (blockManager.getTotalSpace() - blockManager.getOccupiedSpace());
	}

	public long getOccupiedMemory() throws IOException {
		return blockManager.getOccupiedSpace();
	}

	public long getTotalMemory() throws IOException {
		return blockManager.getTotalSpace();
	}

	@Override
	public void createDirectory(MetaData meta) throws Exception {
		MetaData parent = getMetaData(meta.getParent());
		meta.setBlockNumber(blockManager.getNextFreeBlock());
		blockManager.write(meta.getBytes(), meta.getBlockNumber());
		addToParent(parent, meta.getBlockNumber());
	}

	public void deleteDisk() throws IOException {
		blockManager.getVirtualDisk().deleteDisk();
	}

	public void deleteMetaData(MetaData metaData) throws Exception {
		blockManager.deleteBlock(metaData.getBlockNumber());
	}

	@Override
	public void deleteDirectory(MetaData metaData) throws Exception {
		deleteRecursively(metaData);
		// Parent MetaData has to change after deletion so search for it by name
		// again
		MetaData retrieved = search(metaData.getName());
		deleteMetaData(retrieved);
	}

	private void deleteRecursively(MetaData metaData) throws Exception {
		if (metaData.getType() == utils.BlockSettings.DIRECTORY_TYPE) {
			// directory
			MetaData[] childrenMetaData = getChildrenMeta(metaData);
			// need to delete the current directory Meta as well
			for (int i = 0; i < childrenMetaData.length; ++i) {
				System.out.println("Deleting child "
						+ childrenMetaData[i].getName());
				deleteRecursively(childrenMetaData[i]);
			}
		} else {
			// file
			System.out.println("Deleting file " + metaData.getName());
			deleteFile(metaData);
		}
	}
}
