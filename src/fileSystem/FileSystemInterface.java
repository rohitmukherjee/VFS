package fileSystem;

public interface FileSystemInterface {

	boolean writeFile(String path, byte[] content) throws Exception;

	byte[] readFile(String path) throws Exception;

	String[] getChildren(String path) throws Exception;

	boolean isValidPath(String path);

	boolean isValidDirectory(String path);

	boolean isValidFile(String path);

	void deleteFile(String path) throws Exception;

	void deleteDirectory(String path) throws Exception;

	long getFreeMemory() throws Exception;

	long getOccupiedMemory() throws Exception;

	long getTotalMemory() throws Exception;

	void renameDirectory(String oldName, String newName) throws Exception;

	void renameFile(String oldName, String newName) throws Exception;

	void addNewDirectory(String path, String currentPath) throws Exception;

	void moveFile(String oldPath, String newPath) throws Exception;

	void moveDirectory(String oldPath, String newPath) throws Exception;

	void copyFile(String oldPath, String newPath) throws Exception;

}
