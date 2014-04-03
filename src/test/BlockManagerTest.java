package test;

import static org.junit.Assert.assertEquals;

import java.io.RandomAccessFile;

import org.junit.Ignore;
import org.junit.Test;

import virtualDisk.BlockManager;
import virtualDisk.BlockSettings;

public class BlockManagerTest {
	private final String WINDOWS_PATH = "D:/test.vdisk";
	private final String POSIX_PATH = "~/home";

	@Test
	public void settingUpDiskUsingBlockManagerShouldWriteFirstBlock()
			throws Exception {
		BlockManager blockManager = new BlockManager(WINDOWS_PATH);
		blockManager.setupBlocks();
		byte[] result = new byte[8];
		RandomAccessFile file = new RandomAccessFile(WINDOWS_PATH, "r");
		file.read(result);
		assertEquals(0, blockManager.getCurrentBlockNumber());
		assertEquals(0, blockManager.getCurrentBlockStartingAddress());
		assertEquals(0, blockManager.getVirtualDisk().getFilePosition());
		assertEquals(0, blockManager.getNextFreeBlock());
		assertEquals(0, blockManager.OffsetInsideCurrentBlock());
		blockManager.write(BlockSettings.MAGIC_NUMBER);
		file.seek(0);
		assertEquals(BlockSettings.MAGIC_NUMBER.length, file.readLong());
		assertEquals(1, blockManager.getNextFreeBlock());
		// Checking reading capabilities
		assertEquals(32, blockManager.getVirtualDisk().getFilePosition());
		assertEquals(0, blockManager.OffsetInsideCurrentBlock());
		blockManager.getVirtualDisk().deleteDisk();
	}

	@Ignore
	public void diskReadTestsForBlockData() throws Exception {
		BlockManager blockManager = new BlockManager(WINDOWS_PATH);
		blockManager.setupBlocks();
		RandomAccessFile file = new RandomAccessFile(WINDOWS_PATH, "r");
		assertEquals(0, blockManager.getCurrentBlockNumber());
		assertEquals(0, blockManager.getNextFreeBlock());
		assertEquals(-1, blockManager.getNextBlock());
		// Writes to block 0, so file pointer should be at position
		// blockManager.write(TestUtilities.testBlockDataLessThan);
		// assertArrayEquals(Block);

	}
}