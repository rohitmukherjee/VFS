package fileManager;

import virtualDisk.BlockManager;

public class FileManager implements FileManagerInterface {

	private BlockManager blockManager;

	public FileManager(String path) {
		blockManager = new BlockManager(path);
	}

	@Override
	public void writeMetaData(long position, MetaData metaData) {

	}

	@Override
	public byte[] getData(long position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MetaData getMetaData(long position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void writeFile(long position, MetaData meta, byte[] data) {
		// TODO Auto-generated method stub
	}

	private byte[] metaDataToByteArray(MetaData metaData) {
		return null;
	}
}
