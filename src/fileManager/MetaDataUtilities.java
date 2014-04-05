package fileManager;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;

import utils.BlockSettings;

public class MetaDataUtilities {

	public static byte[] StringToBytes(String string) {
		return string.getBytes();
	}

	public static String bytesToString(byte[] data) {
		return new String(data);
	}

	public static byte[] longToBytes(long value) {
		ByteBuffer buffer = ByteBuffer.allocate(8);
		buffer.putLong(value);
		return buffer.array();
	}

	public static long bytesToLong(byte[] data) {
		ByteBuffer buffer = ByteBuffer.allocate(8);
		buffer.put(data);
		buffer.flip();
		return buffer.getLong();
	}

	public static byte[] fileToBytes(String path) throws IOException {
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

	public static byte[] metaDataToBytes(MetaData metaData) {
		ByteBuffer meta = ByteBuffer.allocate(BlockSettings.METADATA_LENGTH);
		byte[] name = StringToBytes(fixedLengthString(metaData.getName()));
		byte[] parent = longToBytes(metaData.getParent());
		byte[] position = longToBytes(metaData.getPosition());
		byte[] timestamp = longToBytes(metaData.getTimestamp());
		// Order of storing meta data inside a block
		meta.put(position);
		meta.put(metaData.getType());
		meta.put(name);
		meta.put(timestamp);
		meta.put(parent);
		return meta.array();
	}

	public static MetaData bytesToMetaData(byte[] data) throws Exception {
		ByteBuffer meta = ByteBuffer.allocate(BlockSettings.METADATA_LENGTH);
		MetaData metaData = new MetaData(data);
		meta.put(data);
		meta.flip();
		metaData.setType(meta.get());
		byte[] name = new byte[BlockSettings.FILENAME_LENGTH];
		meta.get(name);
		metaData.setName(bytesToString(name));
		metaData.setPosition(meta.getLong());
		metaData.setTimestamp(meta.getLong());
		metaData.setParent(meta.getLong());
		return metaData;
	}

	private static String fixedLengthString(String string) {
		return String.format("%1$" + BlockSettings.FILENAME_LENGTH + "s",
				string);
	}
}
