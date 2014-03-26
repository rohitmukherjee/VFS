package exceptions;

public class IncorrectPathException extends Exception {
	private IncorrectPathException() {
		super("The disk path is incorrect");
	}

	public IncorrectPathException(String message) {
		super(message);
	}
}
