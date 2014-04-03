package commands;

import cli.Console;

public class List implements Command{
	private Console console;
	
	public List(Console aConsole) {
		console = aConsole;
	}
	
	@Override
	public void execute(String[] params) {
		String[] children = console.fs.getChildren(params[0]);
		for(int i = 0; i < children.length; ++i){
			console.printMessage(children[i]);
		}
	}

}
