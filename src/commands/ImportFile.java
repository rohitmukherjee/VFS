package commands;

import java.nio.file.Paths;

import cli.Console;

public class ImportFile implements Command {
	private Console console;

	public ImportFile(Console aConsole) {
		console = aConsole;
	}

	/*
	 * @param params first is source on real machine 
	 * second is destination on VFS.
	 */
	@Override
	public void execute(String[] params) {
		if (console.fs.isValidFile(params[0])) {
			byte[] tempFile;
			try {
				tempFile = java.nio.file.Files.readAllBytes(Paths
						.get(params[0]));
				console.fs.writeFile(params[1], tempFile);
				console.print("Successfully copied");
			} catch (Exception e) {
				console.print(e.getMessage());
			}
		}
	}

}
