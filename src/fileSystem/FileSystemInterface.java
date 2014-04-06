package fileSystem;

public interface FileSystemInterface {

	boolean writeFile(String path, byte[] content);

	void addNewDirectory(String path);

	byte[] readFile(String path);

	String[] getChildren(String path);

	boolean isValidPath(String path);

	boolean isValidFile(String path);

	void deleteFile(String path);

	void deleteDirectory(String path) throws Exception;

	long getFreeMem() throws Exception;

	long getOccupiedMem() throws Exception;

	long getTotalMem() throws Exception;

	void renameDirectory(String oldName, String newName) throws Exception;

	void renameFile(String oldName, String newName) throws Exception;

}
