package cli;

import java.io.BufferedReader;
import java.io.PrintWriter;

import FileSystem.FileSystemImpl;

public class Console {
		private BufferedReader reader;
		private PrintWriter writer;
		public FileSystemImpl fs;
		public String path;
		private boolean shouldQuit;
		
		/**
		 * Constructor.
		 * 
		 * @param aScanner
		 *            Source of input.
		 * @param aPrinter
		 *            Destination of output.
		 * @param aCalc
		 *            Calculator object.
		 */
		public Console(BufferedReader aReader, PrintWriter aWriter,
				FileSystemImpl afs) {
			reader = aReader;
			writer = aWriter;
			fs = afs;
			path = "root";
			shouldQuit = false;
		}

		
		/**
		 * Print message.
		 */
		public void printMessage(String str) {
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
			String inputCmd = "";

			while (!shouldQuit) {
				printPrompt();
				try {
					inputCmd = reader.readLine().trim();
				} catch (Exception e) {
					printMessage(e.getMessage());
					quit();
				}
			}
		}
		
		// Print command prompt.
		private void printPrompt() {
			writer.print(">");
			writer.flush();
		}

	}

