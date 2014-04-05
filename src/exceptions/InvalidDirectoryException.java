package exceptions;

public class InvalidDirectoryException extends Exception {

	public InvalidDirectoryException() {
		super("Please provide a valid directory");
	}

	public InvalidDirectoryException(String message) {
		super(message);
	}
}
