package test;

import static org.junit.Assert.assertArrayEquals;

import java.io.IOException;
import java.util.zip.DataFormatException;

import org.junit.Test;

import utils.BlockSettings;
import utils.CompressionUtilities;

public class CompressionUtilitiesTest {

	private byte[] largeArray = new byte[BlockSettings.BLOCK_SIZE * 100];
	private byte[] compressedLargeArray;
	private byte[] decompressedLargeArray;

	private void largeArraySetup() {
		for (int i = 0; i < largeArray.length; i++) {
			largeArray[i] = (byte) ((byte) i % 2);
		}
	}

	@Test
	public void compressAndDecompressLargeArray() throws IOException,
			DataFormatException {
		largeArraySetup();
		compressedLargeArray = CompressionUtilities.compress(largeArray);
		decompressedLargeArray = CompressionUtilities
				.decompress(compressedLargeArray);
		assertArrayEquals(largeArray, decompressedLargeArray);
	}
}
