package filesystem;

public interface FileSystemInterface {
	
	boolean writeFile(String path, byte[] content);
	
	void addNewDirectory(String path);
	
	byte[] readFile(String path);
	
	String[] getChildren(String path);
	
	boolean isValidPath(String path);
	
	boolean isValidFile(String path);
	
	void deleteFile(String path);
	
	void deleteDirectory(String path);
	
	long getFreeMem();
	
	long getOccupiedMem();
	
	long getTotalMem();
	
	void renameDirectory(String oldName, String newName);
	
	void renameFile(String oldName, String newName);
	
}
