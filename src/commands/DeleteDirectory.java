package commands;

import cli.Console;

public class DeleteDirectory implements Command{
	private Console console;
	
	public DeleteDirectory(Console aConsole) {
		console = aConsole;
	}
	
	@Override
	public void execute(String[] params) {
		if(console.fs.isValidPath(params[0])) {
			console.fs.deleteDirectory(params[0]);
			console.printMessage("successfully deleted");
		}
	}

}
