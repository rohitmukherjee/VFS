package utils;

import java.io.FileOutputStream;
import java.io.IOException;

public class FileSystemUtilities {

	public static boolean isFileName(String name) {
		return name.contains(".");
	}

	public static boolean isDirectoryName(String name) {
		return !(isFileName(name));
	}

	public static void exportFile(byte[] fileData, String fileSelected)
			throws IOException {
		FileOutputStream fileOuputStream = new FileOutputStream(fileSelected);
		fileOuputStream.write(fileData);
		fileOuputStream.close();

	}

}
