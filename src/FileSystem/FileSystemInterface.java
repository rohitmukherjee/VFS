package FileSystem;

public interface FileSystemInterface {
	
	boolean writeFile(String path, byte[] content);
	
	byte[] readFile(String path);
	
	String[] getChildren(String path);
	
}
