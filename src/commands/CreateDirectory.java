package commands;

import cli.Console;

public class CreateDirectory implements Command{
	private Console console;
	
	public CreateDirectory(Console aConsole) {
		console = aConsole;
	}
	@Override
	public void execute(String[] params) {
		console.fs.addNewDirectory(params[0]);
		console.print(params[0]);
	}

}
