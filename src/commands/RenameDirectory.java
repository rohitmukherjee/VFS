package commands;

import cli.Console;
import exceptions.InvalidDirectoryException;

public class RenameDirectory implements Command {
	private Console console;

	public RenameDirectory(Console aConsole) {
		console = aConsole;
	}

	@Override
	public void execute(String[] params) {
		if (console.fs.isValidPath(params[0])) {
			try {
				console.fs.renameDirectory(params[0], params[1]);
				console.print("New title is:" + params[1]);
			} catch (InvalidDirectoryException ex) {
				console.print(ex.getMessage());
			}

		}

	}
}
