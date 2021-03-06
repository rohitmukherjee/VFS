package utils;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import fileManager.MetaData;

public class MetaDataUtilities {

	public static byte[] StringToBytes(String string) {
		return string.getBytes();
	}

	public static String bytesToString(byte[] data) {
		String string = "";
		try {
			string = new String(data, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// return ""
		}
		return string.trim();
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
		Path filePath = Paths.get(path);
		return Files.readAllBytes(filePath);
	}

	/**
	 * This class needs the file path and the byte array containing file
	 * contents to create a file on the host system
	 * 
	 * @param data
	 * @throws IOException
	 */
	public static void bytesToFile(byte[] data, String path) throws IOException {
		File file = new File(path);
		FileOutputStream output = new FileOutputStream(file);
		BufferedOutputStream bos = new BufferedOutputStream(output);
		output.flush();
		bos.flush();
		bos.write(data);
		bos.close();
		output.close();
	}

	public static byte[] metaDataToBytes(MetaData metaData) {
		ByteBuffer meta = ByteBuffer.allocate(BlockSettings.METADATA_LENGTH);
		byte[] name = StringToBytes(fixedLengthString(metaData.getName()));
		byte[] parent = longToBytes(metaData.getParent());
		byte[] blockNumber = longToBytes(metaData.getBlockNumber());
		byte[] timestamp = longToBytes(metaData.getTimestamp());
		// Order of storing meta data inside a block
		meta.put(blockNumber);
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
		metaData.setBlockNumber(meta.getLong());
		metaData.setTimestamp(meta.getLong());
		metaData.setParent(meta.getLong());
		return metaData;
	}

	public static byte[] concaByteArrays(byte[] array1, byte[] array2) {
		byte[] result = new byte[array1.length + array2.length];
		for (int i = 0; i < array1.length; ++i) {
			result[i] = array1[i];
		}
		for (int i = 0; i < array2.length; ++i) {
			result[i + array1.length] = array2[i];
		}
		return result;
	}

	private static String fixedLengthString(String string) {
		return String.format("%1$" + BlockSettings.FILENAME_LENGTH + "s",
				string);
	}

	public static long getLong(byte[] data, int startingPosition) {
		byte[] info = new byte[8];
		for (int i = 0; i < info.length; ++i) {
			info[i] = data[startingPosition + i];
		}
		return MetaDataUtilities.bytesToLong(info);
	}

	public static long[] getLongArray(byte[] data) throws Exception {
		long[] result = new long[data.length / 8];
		for (int i = 0; i < data.length / 8; ++i) {
			result[i] = getLong(data, i * 8);
		}
		return result;
	}

	public static long[] concaLongArrays(long[] array1, long[] array2) {
		long[] result = new long[array1.length + array2.length];
		for (int i = 0; i < array1.length; ++i) {
			result[i] = array1[i];
		}
		for (int i = 0; i < array2.length; ++i) {
			result[i + array1.length] = array2[i];
		}
		return result;
	}

	public static byte[] getByteArray(long[] longArray) throws IOException {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		for (int i = 0; i < longArray.length; ++i) {
			stream.write(MetaDataUtilities.longToBytes(longArray[i]));
		}
		byte[] data = stream.toByteArray();
		return data;
	}
}
