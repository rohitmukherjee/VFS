package exceptions;

public class FileOrDirectoryAlreadyExistsException extends Exception {
	public FileOrDirectoryAlreadyExistsException() {
		super("File or Directory already exists");
	}

	public FileOrDirectoryAlreadyExistsException(String message) {
		super(message);
	}
}
