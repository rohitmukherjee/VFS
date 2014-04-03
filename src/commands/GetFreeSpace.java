package commands;

import cli.Console;

public class GetFreeSpace implements Command{
	private Console console;
	
	public GetFreeSpace(Console aConsole) {
		console = aConsole;
	}
	
	@Override
	public void execute(String[] params) {
		console.print(
				String.valueOf(console.fs.getFreeMem()));
	}

}
