package commands;

import cli.Console;
import exceptions.InvalidDirectoryException;

public class DeleteDirectory implements Command {
	private Console console;

	public DeleteDirectory(Console aConsole) {
		console = aConsole;
	}

	@Override
	public void execute(String[] params) {
		try {
			if (console.fs.isValidPath(params[0])) {
				console.fs.deleteDirectory(params[0]);
				console.print("successfully deleted");
			}
		} catch (InvalidDirectoryException ex) {
			console.print(ex.getMessage());
		}
	}
}
