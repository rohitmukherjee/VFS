package commands;

import cli.Console;
import exceptions.CannotAccessDiskException;

public class GetFreeSpace implements Command {
	private Console console;

	public GetFreeSpace(Console aConsole) {
		console = aConsole;
	}

	@Override
	public void execute(String[] params) {
		try {
			console.print(String.valueOf(console.fs.getFreeMem()));
		} catch (CannotAccessDiskException ex) {
			console.print(ex.getMessage());
		}
	}
}
