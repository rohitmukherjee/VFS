package commands;

import cli.Console;

public class MoveFile implements Command{
	private Console console;
	
	public MoveFile(Console aConsole) {
		console = aConsole;
	}
	@Override
	public void execute(String[] params) {
		if(console.fs.isValidFile(params[0])) {
			try{
				byte[] tempFile = console.fs.readFile(params[0]);
				console.fs.writeFile(params[1], tempFile);
				console.fs.deleteFile(params[0]);
				console.printMessage(params[1] + "has been moved");
			} catch (Exception e) {
				console.printMessage("An Error has occured");
			}
		}
		
	}

}
