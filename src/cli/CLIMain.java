package cli;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import fileSystem.FileSystem;

public class CLIMain {

	private static String diskLocation = "disk.vdisk";

	public static void main(String args[]) throws Exception {
		BufferedReader inputStream = new BufferedReader(new InputStreamReader(
				System.in));
		PrintWriter outputStream = new PrintWriter(System.out);
		FileSystem vfs = new FileSystem(diskLocation);
		Console app = new Console(inputStream, outputStream, vfs);
		app.run();
	}
}
