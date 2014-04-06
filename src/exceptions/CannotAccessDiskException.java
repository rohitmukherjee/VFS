package exceptions;

public class CannotAccessDiskException extends Exception {

	public CannotAccessDiskException() {
		super("Cannot access Disk");
	}

	public CannotAccessDiskException(String message) {
		super(message);
	}
}
