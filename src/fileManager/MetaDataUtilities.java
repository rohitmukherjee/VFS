package fileManager;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MetaDataUtilities {

	public static byte[] StringToBytes(String string) {
		return string.getBytes();
	}

	public String bytesToString(byte[] data) {
		return new String(data);
	}

	public byte[] longToBytes(long value) {
		ByteBuffer buffer = ByteBuffer.allocate(8);
		buffer.putLong(value);
		return buffer.array();
	}

	public long bytesToLong(byte[] data) {
		ByteBuffer buffer = ByteBuffer.allocate(8);
		buffer.put(data);
		buffer.flip();
		return buffer.getLong();
	}

	public byte[] fileToBytes(String path) throws IOException {
		return Files.readAllBytes(Paths.get(path));
	}

	/**
	 * This class needs the file path and the byte array containing file
	 * contents to create a file on the host system
	 * 
	 * @param data
	 * @throws IOException
	 */
	public void bytesToFile(byte[] data, String path) throws IOException {
		FileOutputStream output = new FileOutputStream(path);
		output.write(data);
		output.close();
	}
}
