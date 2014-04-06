package exceptions;

public class DiskStructureException extends Exception {

	public DiskStructureException() {
		super("Disk Structure is broken");
	}

	public DiskStructureException(String message) {
		super(message);
	}
}
