package commands;

import cli.Console;

public class RenameFile implements Command{
private Console console;
	
	public RenameFile(Console aConsole) {
		console = aConsole;
	}
	
	@Override
	public void execute(String[] params) {
		if(console.fs.isValidFile(params[0])) {
			console.fs.renameFile(params[0], params[1]);
			console.print("New title is:" + params[1]);
		}

	}
}
