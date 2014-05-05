package utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import fileManager.FileManager;

public class CompressionUtilities {

	private static Logger logger = Logger.getLogger(CompressionUtilities.class);

	public CompressionUtilities() {
		logger = Logger.getLogger(FileManager.class);
		BasicConfigurator.configure();
	}

	public static byte[] compress(byte[] data) throws IOException {
		Deflater deflater = new Deflater(Deflater.BEST_COMPRESSION);
		deflater.setStrategy(Deflater.DEFAULT_STRATEGY);
		deflater.setInput(data);
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream(
				data.length);
		deflater.finish();
		byte[] buffer = new byte[1024];
		while (!deflater.finished()) {
			int count = deflater.deflate(buffer);
			outputStream.write(buffer, 0, count);
		}
		outputStream.close();
		byte[] output = outputStream.toByteArray();
		logger.debug("Original: " + data.length + "bytes");
		logger.debug("Compressed: " + output.length + "bytes");
		return output;
	}

	public static byte[] decompress(byte[] data) throws IOException,
			DataFormatException {
		Inflater inflater = new Inflater();
		inflater = new Inflater();
		inflater.setInput(data);

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream(
				data.length);
		byte[] buffer = new byte[1024];
		while (!inflater.finished()) {
			int count = inflater.inflate(buffer);
			outputStream.write(buffer, 0, count);
		}
		outputStream.close();
		byte[] output = outputStream.toByteArray();

		logger.debug("Original: " + data.length);
		logger.debug("Decompressed: " + output.length);
		return output;
	}
}