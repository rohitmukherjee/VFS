package commands;

import cli.Console;
import exceptions.DiskStructureException;

public class List implements Command {
	private Console console;

	public List(Console aConsole) {
		console = aConsole;
	}

	@Override
	public void execute(String[] params) {
		try {
			String[] children = console.fs.getChildren(params[0]);
			for (int i = 0; i < children.length; ++i)
				console.print(children[i]);
		} catch (DiskStructureException ex) {
			console.print(ex.getMessage());
		}
	}
}
