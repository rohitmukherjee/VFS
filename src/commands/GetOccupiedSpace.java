package commands;

import cli.Console;
import exceptions.CannotAccessDiskException;

public class GetOccupiedSpace implements Command {
	private Console console;

	public GetOccupiedSpace(Console aConsole) {
		console = aConsole;
	}

	@Override
	public void execute(String[] params) {
		try {
			console.print(String.valueOf(console.fs.getOccupiedMem()));
		} catch (CannotAccessDiskException ex) {
			console.print(ex.getMessage());
		}
	}

}
