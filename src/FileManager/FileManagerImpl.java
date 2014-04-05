package FileManager;

import java.io.IOException;

import virtualDisk.BlockManager;

public class FileManagerImpl {
	private BlockManager blockManager;
	
	public FileManagerImpl(String host) {
		blockManager = new BlockManager(host);
	}
	
	public void writeMeta(MetaDeta meta) throws Exception {
		blockManager.write(meta.getBytes(), meta.address);
	}
	
	public byte[] getData(MetaDeta meta) {
		return null;
	}
	
	public MetaDeta getMeta(MetaDeta meta) throws IOException {
		return new MetaDeta(blockManager.readBlock(meta.address));
	}
	
	public void deleteFile(MetaDeta meta) {
		
	}
	
	public void writeFile(MetaDeta meta, byte[] data) {
		
	}
}
