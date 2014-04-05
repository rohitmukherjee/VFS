package fileManager;

import java.io.IOException;

import virtualDisk.BlockManager;

public class FileManager implements FileManagerInterface {

	private BlockManager blockManager;
	private boolean found;

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
	public MetaData search(String path) throws IOException, Exception {
		String[] pathComponents = path.split("/");
		MetaData root = getMetaData(0);
		// This may seem wierd, but this lets us stop all of the branches of 
		// the recursive search if it is parallelized. (which it cant be...so)
		found = false;
		return rsearch(root, 1, pathComponents);
		//byte[] toSearch = MetaDataUtilities.StringToBytes(path);

	}

	private MetaData rsearch(MetaData metaData,int location, String[] pathComponents) throws Exception {
		MetaData[] contents = getChildrenMeta(metaData);
		for(int i = 0; i < contents.length; ++i) {
			if(location == pathComponents.length-1){
				//looking for a file now
				
			}
			else {
				//looking for a directory
			}
		}
		return metaData;
	}

	private MetaData[] getChildrenMeta(MetaData metaData) throws Exception {
		byte[] data = getData(metaData);
		MetaData[] result =  new MetaData[data.length % 8];
		for(int i = 0; i < result.length; ++i) {
			result[i] = getMetaData(MetaDataUtilities.getLong(data, i*8));
		}
		return result;
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
