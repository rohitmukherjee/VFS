package commands;

import cli.Console;

public class PWD implements Command{
	private Console console;
	
	public PWD(Console aConsole) {
		console = aConsole;
	}
	@Override
	public void execute(String[] params) {
		console.printMessage(console.path);
		
	}

}
