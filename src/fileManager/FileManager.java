package fileManager;

import java.io.IOException;

import virtualDisk.BlockManager;

public class FileManager implements FileManagerInterface {

	private BlockManager blockManager;

	public FileManager(String path) {
		blockManager = new BlockManager(path);
	}

	@Override
	public void writeMetaData(long position, MetaData metaData) throws IOException, Exception {
		long dataPosition = blockManager.getNextBlock(metaData.getPosition());
		metaData.setPosition(position);
		blockManager.write(metaData.getBytes(), position);
		blockManager.combineBlocks(metaData.getPosition(), dataPosition);
	}
	
	@Override
	public void writeReplaceMetaData(MetaData metaData) throws IOException, Exception {
		long dataPosition = blockManager.getNextBlock(metaData.getPosition());
		metaData.setPosition(blockManager.getNextFreeBlock());
		blockManager.write(metaData.getBytes(), metaData.getPosition());
		blockManager.combineBlocks(metaData.getPosition(), dataPosition);
	}

	@Override
	public MetaData getMetaData(long position) throws IOException, Exception {
		//change to metaData.getPosition();
		return new MetaData(blockManager.readBlock(position));
	}
	
	@Override
	public MetaData getMetaData(MetaData metaData) throws IOException, Exception {
		return new MetaData (blockManager.readBlock(metaData.getPosition()));
	}

	@Override
	public void writeFile(MetaData meta, byte[] data) throws Exception {
		meta.setPosition(blockManager.getNextFreeBlock());
		blockManager.write(MetaDataUtilities.concaByteArrays(
				meta.getBytes(), data));
	}

	@Override
	public void search(String path) {
		
		byte[] toSearch = MetaDataUtilities.StringToBytes(path);

	}

	@Override
	public byte[] getData(MetaData metaData) throws IOException {
		return blockManager.read(blockManager.getNextBlock(metaData.getPosition()));
	}

	@Override
	public void deleteFile(MetaData metaData) throws Exception {
		blockManager.delete(metaData.getPosition());
		
	}
}
