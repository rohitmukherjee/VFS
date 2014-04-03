package commands;

import cli.Console;

public class CopyFile implements Command{
	private Console console;
	
	public CopyFile(Console aConsole) {
		console = aConsole;
	}
	
	
	@Override
	public void execute(String[] params) {
		if(console.fs.isValidFile(params[0])) {
			console.fs.writeFile(params[1],
				console.fs.readFile(params[0]));
		}
	}

}
