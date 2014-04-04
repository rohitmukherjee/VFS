package test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.io.RandomAccessFile;

import org.junit.Ignore;
import org.junit.Test;

import virtualDisk.BlockManager;
import virtualDisk.BlockSettings;

public class BlockManagerTest {

	@Ignore
	public void settingUpDiskUsingBlockManagerShouldWriteFirstBlock()
			throws Exception {
		BlockManager blockManager = new BlockManager(TestUtilities.WINDOWS_PATH);
		blockManager.setupBlocks();
		byte[] result = new byte[8];
		RandomAccessFile file = new RandomAccessFile(
				TestUtilities.WINDOWS_PATH, "r");
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

	@Test
	public void diskReadTestsForBlockData() throws Exception {
		BlockManager blockManager = new BlockManager(TestUtilities.WINDOWS_PATH);
		blockManager.setupBlocks();
		assertEquals(0, blockManager.getCurrentBlockNumber());
		assertEquals(0, blockManager.getNextFreeBlock());
		blockManager.write(TestUtilities.testBlockDataLessThan);
		assertEquals(0, blockManager.getNextBlock());
		// Writes to block 0, so file pointer should be at position
		assertArrayEquals(TestUtilities.testBlockDataLessThan,
				blockManager.read(0));
		blockManager.write(TestUtilities.testBlockDataLessThan);
		assertArrayEquals(TestUtilities.testBlockDataLessThan,
				blockManager.read(1));
		assertEquals(2, blockManager.getNextFreeBlock());
		blockManager.write(TestUtilities.testBlockDataFullBlock);
		assertArrayEquals(TestUtilities.testBlockDataFullBlock,
				blockManager.read(2));
		assertEquals(3, blockManager.getNextFreeBlock());
		// writing big data
		TestUtilities.bigDataSetup();
		blockManager.write(TestUtilities.bigData);
		assertEquals(66, blockManager.getNextFreeBlock());
		blockManager.getVirtualDisk().seek(3 * BlockSettings.BLOCK_SIZE);
		assertEquals(4, blockManager.getNextBlock());
		blockManager.getVirtualDisk().seek(56 * BlockSettings.BLOCK_SIZE);
		assertEquals(57, blockManager.getNextBlock());
		byte[] result = blockManager.read(3);
		assertArrayEquals(TestUtilities.bigData, result);
	}
	@Test 
	public void diskDeleteFileGeneral() throws Exception { 
		BlockManager blockManager = new BlockManager(TestUtilities.WINDOWS_PATH);
		blockManager.setupBlocks();
		long initialNextFreeBlock = setUpDeleteTests(blockManager);
		TestUtilities.twoBlockSetup();
		
		//dont allow deleting at 0
		blockManager.delete(0);
		assertEquals(initialNextFreeBlock, blockManager.getNextFreeBlock());
		assertEquals(0, blockManager.getNextBlock());
		
		//try deleting the first block
		blockManager.delete(1);
		assertEquals(1, blockManager.getNextFreeBlock());
		assertEquals(0, blockManager.getNextBlock());
		//maybe add this in the future?
//		assertEquals(initialNextFreeBlock-1, blockManager.getNumberOccupiedBlocks());
		//rewrite first block at same place
		blockManager.write(TestUtilities.testBlockData2Blocks);
		assertEquals(initialNextFreeBlock, blockManager.getNextFreeBlock());
		
		//try deleting file with multiple blocks
		blockManager.delete(5);
		assertEquals(5, blockManager.getNextFreeBlock());
		assertEquals(0, blockManager.getNextBlock());
		//rewrite first block at same place
		blockManager.write(TestUtilities.bigData);
		assertEquals(initialNextFreeBlock, blockManager.getNextFreeBlock());

	} 
	
	@Test 
	public void diskDeleteFileMiddle() throws Exception {
		BlockManager blockManager = new BlockManager(TestUtilities.WINDOWS_PATH);
		blockManager.setupBlocks();
		long initialNextFreeBlock = setUpDeleteTests(blockManager);
		
		//deleting some blocks in the middle
		blockManager.delete(4);
		assertEquals(4, blockManager.getNextFreeBlock());
		assertEquals(0, blockManager.getNextBlock());
		//delete another block before that one
		blockManager.delete(1);
		assertEquals(1, blockManager.getNextFreeBlock());
		assertEquals(0, blockManager.getNextBlock());
		//delete another one in the middle
		blockManager.delete(3);
		assertEquals(1, blockManager.getNextFreeBlock());
		assertEquals(0, blockManager.getNextBlock());
		//write one block, should move nextfree to appropriate position
		blockManager.write(TestUtilities.testBlockDataFullBlock);
		assertEquals(2, blockManager.getNextFreeBlock());
		assertEquals(0, blockManager.getNextBlock());
	}
	
	@Test 
	public void diskDeleteFileInterleaved() throws Exception {
		BlockManager blockManager = new BlockManager(TestUtilities.WINDOWS_PATH);
		blockManager.setupBlocks();
		long initialNextFreeBlock = setUpDeleteTests(blockManager);

		//delete some files so we have alternating used/unused blocks
		blockManager.delete(1);
		blockManager.delete(4);
		assertEquals(1, blockManager.getNextFreeBlock());
		assertEquals(0, blockManager.getNextBlock());
		
		//write a 2 block file where blocks arent contiguous
		blockManager.write(TestUtilities.testBlockDataFullBlock);
		assertEquals(2, blockManager.getNextFreeBlock());
		blockManager.write(TestUtilities.testBlockData2Blocks);
		assertEquals(initialNextFreeBlock, blockManager.getNextFreeBlock());
		blockManager.getVirtualDisk().seek(2*BlockSettings.BLOCK_SIZE);
		assertEquals(4, blockManager.getNextBlock());
		
		//delete non contiguous file
		blockManager.delete(2);
		assertEquals(2, blockManager.getNextFreeBlock());
		blockManager.write(TestUtilities.testBlockDataFullBlock);
		assertEquals(4, blockManager.getNextFreeBlock());
	}
	
	/**
	 * helper method that sets up some data in the virtual disk that we can then delete
	 * 1 full block, 2 full blocks, 3 unfilled but used blocks, large block, 3 full blocks
	 * assertions commented out for now because we dont need to check them every time
	 * 
	 * @param blockManager
	 * @throws Exception
	 */
	public long setUpDeleteTests(BlockManager blockManager) throws Exception {
		blockManager.write(TestUtilities.testBlockDataFullBlock);
//		assertArrayEquals(TestUtilities.testBlockDataFullBlock,
//				blockManager.read(2));
//		assertEquals(3, blockManager.getNextFreeBlock());
		blockManager.write(TestUtilities.testBlockData2Blocks);		
		blockManager.write(TestUtilities.testBlockDataLessThan);
		blockManager.write(TestUtilities.testBlockDataLessThan);
		blockManager.write(TestUtilities.testBlockDataLessThan);
		blockManager.write(TestUtilities.bigData);
		blockManager.write(TestUtilities.testBlockDataFullBlock);		
		blockManager.write(TestUtilities.testBlockDataFullBlock);
		blockManager.write(TestUtilities.testBlockDataFullBlock);
		return blockManager.getNextFreeBlock();
		
	}
}