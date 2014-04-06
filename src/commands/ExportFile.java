package commands;

import java.nio.file.Path;
import java.nio.file.Paths;

import cli.Console;

public class ExportFile implements Command {
	private Console console;

	public ExportFile(Console aConsole) {
		console = aConsole;
	}

	/*
	 * @param params first is source second is destination on real machine.
	 */
	@Override
	public void execute(String[] params) {
		if (console.fs.isValidFile(params[0])) {
			try {
				byte[] tempFile = console.fs.readFile(params[0]);
				Path filePath = Paths.get(params[1]);
				java.nio.file.Files.write(filePath, tempFile);
				console.print("Successfully copied");
			} catch (Exception e) {
				console.print("File could not be copied");
			}
		}
	}

}
