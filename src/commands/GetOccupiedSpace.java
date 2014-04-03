package commands;

import cli.Console;

public class GetOccupiedSpace implements Command{
	private Console console;
	
	public GetOccupiedSpace(Console aConsole) {
		console = aConsole;
	}
	
	@Override
	public void execute(String[] params) {
		console.printMessage(
				String.valueOf(console.fs.getOccupiedMem()));
	}

}
