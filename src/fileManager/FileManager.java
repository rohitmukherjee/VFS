package fileManager;

import java.io.IOException;

import utils.BlockSettings;
import virtualDisk.BlockManager;

public class FileManager implements FileManagerInterface {

	private BlockManager blockManager;

	public FileManager(String path) {
		blockManager = new BlockManager(path);
	}

	public void writeRoot(MetaData meta) {

	}

	@Override
	public void writeMetaData(long position, MetaData metaData)
			throws IOException, Exception {
		long dataPosition = blockManager.getNextBlock(metaData.getPosition());
		metaData.setPosition(position);
		blockManager.write(metaData.getBytes(), position);
		blockManager.combineBlocks(metaData.getPosition(), dataPosition);
	}

	@Override
	public void writeReplaceMetaData(MetaData metaData) throws IOException,
			Exception {
		long dataPosition = blockManager.getNextBlock(metaData.getPosition());
		metaData.setPosition(blockManager.getNextFreeBlock());
		blockManager.write(metaData.getBytes(), metaData.getPosition());
		blockManager.combineBlocks(metaData.getPosition(), dataPosition);
	}

	@Override
	public MetaData getMetaData(long position) throws IOException, Exception {
		// change to metaData.getPosition();
		return new MetaData(blockManager.readBlock(position));
	}

	@Override
	public MetaData getMetaData(MetaData metaData) throws IOException,
			Exception {
		return new MetaData(blockManager.readBlock(metaData.getPosition()));
	}

	@Override
	public void createFile(MetaData meta, byte[] data) throws Exception {
		// Write the metadata and the data, then combines the blocks
		meta.setPosition(blockManager.getNextFreeBlock());
		long position = meta.getPosition();
		byte[] compressed = MetaDataUtilities.getCompressedBytes(data);
		byte[] toWrite = MetaDataUtilities.getEncryptedBytes(compressed);
		blockManager.write(meta.getBytes(), meta.getPosition());
		long dataPosition = blockManager.getNextFreeBlock();
		blockManager.write(toWrite, dataPosition);
		blockManager.combineBlocks(meta.getPosition(), dataPosition);

		// grabs the parents and updates those values
		MetaData parent = getMetaData(meta.getParent());

		byte[] parentDataRaw = getData(parent);
		long[] parentData = MetaDataUtilities.getLongArray(parentDataRaw);
		long[] newData = new long[1];
		newData[0] = meta.getPosition();
		long[] parentDataNew = MetaDataUtilities.concaLongArrays(newData,
				parentData);
		byte[] parentDataToWrite = MetaDataUtilities
				.getByteArray(parentDataNew);
		long parentDataLocation = blockManager.getNextFreeBlock();
		blockManager.write(parentDataToWrite);
		blockManager.combineBlocks(parent.getPosition(), parentDataLocation);

	}

	@Override
	public MetaData search(String path) throws IOException, Exception {
		String[] pathComponents = path.split("/");
		MetaData root = getMetaData(0);
		return rsearch(root, 1, pathComponents);
	}

	private MetaData rsearch(MetaData metaData, int location,
			String[] pathComponents) throws Exception {
		MetaData[] contents = getChildrenMeta(metaData);
		for (int i = 0; i < contents.length; ++i) {
			if (location == pathComponents.length - 1) {
				// looking for a file now
				if (contents[i].getType() == BlockSettings.FILE_TYPE
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
		return null;
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
		byte[] tempData = blockManager.read(blockManager.getNextBlock(metaData
				.getPosition()));
		return MetaDataUtilities.getDecryptedBytes(MetaDataUtilities
				.getDecompressedBytes(tempData));
	}

	@Override
	public void deleteFile(MetaData metaData) throws Exception {
		// TODO: delete the file in the parents data!
		blockManager.delete(metaData.getPosition());
	}

	@Override
	public void deleteRecursively(MetaData metaData) throws Exception {
		if (metaData.getType() == utils.BlockSettings.DIRECTORY_TYPE) {
			// directory
			MetaData[] childrenMetaData = getChildrenMeta(metaData);
			for (int i = 0; i < childrenMetaData.length; ++i) {
				deleteRecursively(childrenMetaData[i]);
			}
		} else {
			// file
			deleteFile(metaData);
		}
	}

	@Override
	public void writeData(MetaData meta, byte[] data) throws Exception {
		blockManager.delete(blockManager.getNextBlock(meta.getPosition()));
		long dataPosition = blockManager.getNextFreeBlock();
		byte[] compressed = MetaDataUtilities.getCompressedBytes(data);
		byte[] toWrite = MetaDataUtilities.getEncryptedBytes(compressed);
		blockManager.write(toWrite, dataPosition);
		blockManager.combineBlocks(meta.getPosition(), dataPosition);

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
		// TODO Auto-generated method stub

	}
}
