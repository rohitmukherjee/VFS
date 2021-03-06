package cli;

import java.io.BufferedReader;
import java.io.PrintWriter;

import commands.Command;

import fileSystem.FileSystem;

public class Console {
	private BufferedReader reader;
	private PrintWriter writer;
	public fileSystem.FileSystem fs;
	public String path;
	private boolean shouldQuit;
	private CommandMap map;

	/**
	 * Constructor.
	 * 
	 * @param aScanner
	 *            Source of input.
	 * @param aPrinter
	 *            Destination of output.
	 * @param afs
	 *            FileSystem object.
	 */
	public Console(BufferedReader aReader, PrintWriter aWriter, FileSystem afs) {
		reader = aReader;
		writer = aWriter;
		fs = afs;
		path = "root";
		shouldQuit = false;
	}

	/**
	 * Print message.
	 */
	public void print(String str) {
		writer.println(str);
		writer.flush();
	}

	/**
	 * Signify the console to quit.
	 */
	public void quit() {
		shouldQuit = true;
	}

	/*
	 * Read, parse, and forward loop.
	 */
	public void run() {
		// have to set up the map here because this is not fully constructed in
		// ctor
		map = new CommandMap(this);

		String inputCmd = "";

		while (!shouldQuit) {
			printPrompt();
			try {
				inputCmd = reader.readLine().trim();
			} catch (Exception e) {
				print(e.getMessage());
				quit();
			}
			// first token is command name
			String command = inputCmd.substring(0, inputCmd.indexOf(" "));
			inputCmd = inputCmd.substring(inputCmd.indexOf(" ") + 1);
			// all others are arguments and forwarded to command object
			String[] args = inputCmd.split(" ");
			runCommand(command, args);

		}
	}

	// Print command prompt.
	private void printPrompt() {
		writer.print(">");
		writer.flush();
	}

	/**
	 * Tries to run the command specified by key. If command isn't found in the
	 * CommandMap, does not execute and prints out "command not found" to
	 * console.
	 * 
	 * @param key
	 *            - String representing name of command
	 * @param args
	 *            - arguments to be passed when executing the command
	 */
	public void runCommand(String key, String[] args) {
		Command command = map.getCommandWithKey(key);
		if (command != null) {
			command.execute(args);
		} else {
			print("Command not found.");
		}
	}
}
