package virtualDisk;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

public class BlockManager {

	private VirtualDisk virtualDisk;
	private long blockSize = Block.MAXIMUM_BLOCK_SIZE;
	private Logger logger;

	public BlockManager(String pathName) {
		virtualDisk = new VirtualDisk(pathName);
		logger = Logger.getLogger(VirtualDisk.class);
		BasicConfigurator.configure();
	}

	public void setupBlocks() throws Exception {
		virtualDisk.setupDisk();
		// Setup first block here
		// Write a magic number into the first 8 bytes to show that it's
		// allocated 11111111
		logger.debug("Wrote magic number to first block, block now ready for writing");
		// virtualDisk.write(0, BlockSettings.MAGIC_NUMBER);
		logger.debug("Finished writing first bitmap block");
	}

	public byte[] read(long blockNumber) throws IOException {
		ByteArrayOutputStream resultStream = new ByteArrayOutputStream();
		long positionBeforeReading = virtualDisk.getFilePosition();
		byte[] results = read(resultStream, blockNumber);
		logger.debug("Resetting file Pointer to previous position");
		virtualDisk.seek(positionBeforeReading);
		return results;
	}

	private byte[] read(ByteArrayOutputStream results, long blockNumber)
			throws IOException {
		virtualDisk.seek(getOffset(blockNumber));
		long lengthOfDataToRead = virtualDisk.readLong();
		results.write(virtualDisk.read(getOffset(blockNumber)
				+ BlockSettings.MAGIC_NUMBER_LENGTH, lengthOfDataToRead));
		// Read the next address
		virtualDisk.seek(getOffset(blockNumber)
				+ BlockSettings.NEXT_ADDRESS_START);
		long nextAddress = virtualDisk.readLong();
		if (nextAddress == 0)
			return results.toByteArray();
		else
			return read(results, getBlockNumber(nextAddress));
	}

	private long getBlockNumber(long offset) {
		return offset / BlockSettings.MAXIMUM_BLOCK_SIZE;
	}

	private long getOffset(long blockNumber) {
		return blockNumber * BlockSettings.MAXIMUM_BLOCK_SIZE;
	}

	public void write(byte[] data) throws IOException {
		long currentWritePosition = getCurrentBlockNumber()
				* BlockSettings.MAXIMUM_BLOCK_SIZE
				+ BlockSettings.MAGIC_NUMBER_LENGTH;
		logger.debug("Value of currentWritePosition:" + currentWritePosition);
		if (data.length <= BlockSettings.MAXIMUM_BLOCK_SIZE
				- BlockSettings.NEXT_ADDRESS_LENGTH
				- BlockSettings.MAGIC_NUMBER_LENGTH) {
			logger.debug("Data fits into current block, writing data");
			virtualDisk.write(currentWritePosition, data);
			// write magic number to beginning of block only after filling block
			virtualDisk.seek(currentWritePosition
					- BlockSettings.MAGIC_NUMBER_LENGTH);
			virtualDisk.writeLong(data.length);
			// write the next address as NULL_NEXT_ADDRESS
			virtualDisk.seek(virtualDisk.getFilePosition()
					+ BlockSettings.MAXIMUM_BLOCK_SIZE
					- BlockSettings.MAGIC_NUMBER_LENGTH
					- BlockSettings.NEXT_ADDRESS_LENGTH);
			virtualDisk.write(BlockSettings.UNUSED);
		}
		// we don't write a next block address because data has bit into this
		// block
		else
			logger.debug("Byte array too big for current block, partitioning data");
	}

	public long getNextFreeBlock() throws Exception {
		logger.debug("getNextFreeBlock was called");
		RandomAccessFile file = new RandomAccessFile("D:/test.vdisk", "r");
		return getNextFreeBlock(0, file);
	}

	public long getNextFreeBlock(long blockNumber, RandomAccessFile filePointer)
			throws IOException {
		filePointer.seek(blockNumber * BlockSettings.MAXIMUM_BLOCK_SIZE);
		byte[] result = new byte[8];
		filePointer.read(result);
		if (isUnused(result)) {
			return blockNumber;
		} else
			return getNextFreeBlock(blockNumber + 1, filePointer);
	}

	private boolean isUnused(byte[] result) {
		return Arrays.equals(result, BlockSettings.UNUSED);
	}

	public long getCurrentBlockNumber() {
		try {
			long currentPosition = virtualDisk.getFilePosition();
			return (currentPosition + 1) / BlockSettings.MAXIMUM_BLOCK_SIZE;
		} catch (Exception ex) {
			return -1;
		}
	}

	// Only for testing purposes, will definitely be removed in production code
	public VirtualDisk getVirtualDisk() {
		return virtualDisk;
	}

	/**
	 * Function returns the current Block
	 * 
	 * @return
	 */
	public Block getCurrentBlock() {
		Block block = new Block(getCurrentBlockNumber() * blockSize);
		return block;
	}

	public long getCurrentBlockStartingAddress() {
		return getCurrentBlockNumber() * BlockSettings.MAXIMUM_BLOCK_SIZE;
	}

	public long OffsetInsideCurrentBlock() {
		try {
			long currentPosition = virtualDisk.getFilePosition();
			return currentPosition % blockSize;
		} catch (Exception ex) {
			return -1;
		}
	}

	/**
	 * returns the next block for computation
	 * 
	 * @return
	 */
	public Block getNextBlock() {
		RestoreBlock restoreBlock = new RestoreBlock(this);
		long nextStartingAddress = -1;
		virtualDisk = getVirtualDisk();
		try {
			virtualDisk.seek(getCurrentBlockStartingAddress() + blockSize - 8);
			nextStartingAddress = virtualDisk.readLong();
		} catch (Exception ex) {
			// TODO: some meaningful logging
			ex.printStackTrace();
		} finally {
			restoreBlock.restore(this);
		}
		return new Block(nextStartingAddress);
	}

	public void combineBlocks(long firstOffset, long secondOffset) {
		RestoreBlock restoreBlock = new RestoreBlock(this);
		try {
			long offset = firstOffset + blockSize - 8;
			virtualDisk.seek(offset);
			virtualDisk.writeLong(offset);
		} catch (Exception ex) {
			// TODO: Some meaningful logging
		} finally {
			restoreBlock.restore(this);
		}
	}

	public boolean hasNextBlock() {
		return getNextBlock().getBlockNumber() != -1;
	}

}
