package exceptions;

public class InvalidFileException extends Exception {

	public InvalidFileException() {
		super("File is invalid");
	}

	public InvalidFileException(String message) {
		super(message);
	}
}