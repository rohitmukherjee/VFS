package exceptions;

public class FileAlreadyExistsException extends Exception {
	
	public FileAlreadyExistsException() {
		super("Sorry that file already exists");
	}

	public FileAlreadyExistsException(String message) {
		super(message);
	}
}
