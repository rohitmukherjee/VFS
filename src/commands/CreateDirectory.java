package commands;

import cli.Console;

public class CreateDirectory implements Command{
	private Console console;
	
	public CreateDirectory(Console aConsole) {
		console = aConsole;
	}
	@Override
	public void execute(String[] params) {
		try {
			console.fs.addNewDirectory(params[0], console.path);
			console.print(params[0]);
		} catch (Exception e) {
			console.print(e.getMessage());
		}
		
	}

}
