package commands;

import cli.Console;

public class ChangeDirectory implements Command{
	private Console console;
	
	public ChangeDirectory(Console aConsole) {
		console = aConsole;
	}
	
	@Override
	public void execute(String[] params) {
		if(console.fs.isValidPath(params[0])) {
			console.path = params[0];
		} else {
		console.printMessage(params[0] + "is not a directory");
		}
	}

}
