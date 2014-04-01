package test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import virtualDisk.BlockManager;

public class BlockManagerTest {
	private final String WINDOWS_PATH = "D:/test.vdisk";

	@Test
	public void settingUpDiskUsingBlockManagerShouldWriteFirstBlock()
			throws Exception {
		BlockManager blockManager = new BlockManager(WINDOWS_PATH);
		blockManager.setupDisk();
		byte[] correctResult = new byte[16];
		correctResult[0] = 1;
		for (int i = 1; i < 16; i++)
			correctResult[i] = 0;
		assertArrayEquals(correctResult,
				blockManager.getVirtualDisk().read(0, 16));
		assertEquals(0, blockManager.getCurrentBlockNumber());
		blockManager.getVirtualDisk().deleteDisk();
	}
}
