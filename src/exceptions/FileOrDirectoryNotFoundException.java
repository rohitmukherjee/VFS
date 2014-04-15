package exceptions;

public class FileOrDirectoryNotFoundException extends Exception {
	public FileOrDirectoryNotFoundException() {
		super("Couldn't find the file or directory you were searching for");
	}

	public FileOrDirectoryNotFoundException(String message) {
		super(message);
	}
}
