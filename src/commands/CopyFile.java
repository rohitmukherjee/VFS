package commands;

import cli.Console;

public class CopyFile implements Command {
	private Console console;

	public CopyFile(Console aConsole) {
		console = aConsole;
	}

	@Override
	public void execute(String[] params) {
		try {
			if (console.fs.isValidFile(params[0])) {
				console.fs.writeFile(params[1], console.fs.readFile(params[0]));
			}
		} catch (Exception ex) {
			console.print(ex.getMessage());
		}
	}
}
