package commands;

import java.io.IOException;
import java.nio.file.Paths;

import cli.Console;

public class ImportFile implements Command{
	private Console console;
	
	public ImportFile(Console aConsole) {
		console = aConsole;
	}
	
	/*
	 * @param params first is source second is destination on real machine.
	 */
	@Override
	public void execute(String[] params) {
		if(console.fs.isValidFile(params[0])) {
			byte[] tempFile;
			try {
				tempFile = java.nio.file.Files.readAllBytes(
						Paths.get(params[0]));
				console.fs.writeFile(params[1], tempFile);
				console.print("Successfully copied");
			} catch (IOException e) {
				console.print("File could not be copied");
			}
		}
	}

}
