package test;

import static org.junit.Assert.assertArrayEquals;

import org.junit.Test;

import virtualDisk.BlockManager;
import virtualDisk.BlockReader;

public class BlockReaderTest {

	@Test
	public void test() throws Exception {
		BlockManager blockManager = new BlockManager(TestUtilities.WINDOWS_PATH);
		blockManager.setupBlocks();
		BlockReader blockReader = new BlockReader(blockManager, 0);
		TestUtilities.bigDataSetup();
		blockManager.write(TestUtilities.bigData);
		byte[] result = new byte[TestUtilities.bigData.length];
		blockReader.readFully(result);
		assertArrayEquals(TestUtilities.bigData, result);
	}
}
