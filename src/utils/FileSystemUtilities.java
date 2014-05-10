package utils;

import java.io.FileOutputStream;

public class FileSystemUtilities {

	private static final CharSequence FILE_EXTENSION_CHARACTER = ".";

	public static void exportFile(byte[] data, String path) throws Exception {
		FileOutputStream fileOutputStream = new FileOutputStream(path);
		fileOutputStream.write(data);
		fileOutputStream.close();
	}

	public static boolean isFileName(String path) {
		if (path.contains(FILE_EXTENSION_CHARACTER))
			return true;
		return false;
	}

	public static boolean isDirectoryName(String path) {
		return (!(isFileName(path)));
	}
}
