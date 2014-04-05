package fileManager;

import java.io.IOException;

import utils.BlockSettings;
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
		//comp/enc data
		blockManager.write(MetaDataUtilities.concaByteArrays(
				meta.getBytes(), data));
	}

	@Override
	public MetaData search(String path) throws IOException, Exception {
		String[] pathComponents = path.split("/");
		MetaData root = getMetaData(0);
		return rsearch(root, 1, pathComponents);
	}

	private MetaData rsearch(MetaData metaData,int location, String[] pathComponents) throws Exception {
		MetaData[] contents = getChildrenMeta(metaData);
		for(int i = 0; i < contents.length; ++i) {
			if(location == pathComponents.length-1){
				//looking for a file now
				if(contents[i].getType() == BlockSettings.FILE_TYPE && 
						contents[i].getName().equals(pathComponents[location])) {
					return contents[i];
				}
			}
			else {
				//looking for a directory
				if(contents[i].getType() == BlockSettings.DIRECTORY_TYPE && 
						contents[i].getName().equals(pathComponents[location])) {
					return rsearch(contents[i], location+1, pathComponents);
				}
			}
		}
		return null;
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
		//decomp/decrypt
		return blockManager.read(blockManager.getNextBlock(metaData.getPosition()));
	}

	@Override
	public void deleteFile(MetaData metaData) throws Exception {
		blockManager.delete(metaData.getPosition());
		
	}

	@Override
	public void deleteRecurisively(MetaData metaData) throws Exception {
		if(metaData.getType() == utils.BlockSettings.DIRECTORY_TYPE){
			//directory
			MetaData[] childrenMetaData = getChildrenMeta(metaData);
			for(int i = 0; i < childrenMetaData.length; ++i) {
				deleteRecurisively(childrenMetaData[i]);
			}
		}
		else {
			//file
			deleteFile(metaData);
		}
	}
}
