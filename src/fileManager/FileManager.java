package fileManager;

import java.io.IOException;

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

	@Override
	public void search(String path) {
		byte[] toSearch = MetaDataUtilities.StringToBytes(path);

	}

	private void getMetaData(long blockNumber, byte[] data) throws IOException {
		data = blockManager.readBlock(blockNumber);
	}
}
