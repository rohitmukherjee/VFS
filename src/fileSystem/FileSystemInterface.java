package fileSystem;

import java.io.IOException;

import exceptions.FileAlreadyExistsException;
import exceptions.InvalidFileException;


public interface FileSystemInterface {

	boolean writeFile(String path, byte[] content) throws Exception;

	void addNewDirectory(String path);

	byte[] readFile(String path) throws Exception;

	String[] getChildren(String path);

	boolean isValidPath(String path);
	
	boolean isValidDirectory(String path);

	boolean isValidFile(String path);

	void deleteFile(String path) throws IOException, Exception;

	void deleteDirectory(String path) throws Exception;

	long getFreeMem();

	long getOccupiedMem();

	long getTotalMem();

	void renameDirectory(String oldName, String newName) throws Exception;

	void renameFile(String oldName, String newName) throws Exception;



}
