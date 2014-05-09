package utils;

import java.io.FileOutputStream;

public class FileSystemUtilities {

	public static void exportFile(byte[] data, String path) throws Exception {
		FileOutputStream fileOutputStream = new FileOutputStream(path);
		fileOutputStream.write(data);
		fileOutputStream.close();
	}
}
