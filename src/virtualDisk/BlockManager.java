package virtualDisk;

import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.nio.ByteBuffer;

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
	}

	public byte[] read(long blockNumber) throws IOException {
		logger.debug("Starting to read the blockNumber: " + blockNumber);
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
				+ BlockSettings.HEADER_LENGTH, lengthOfDataToRead));
		// Read the next address
		virtualDisk.seek(getOffset(blockNumber)
				+ BlockSettings.NEXT_ADDRESS_START);
		long nextAddress = virtualDisk.readLong();
		if (nextAddress == 0)
			return results.toByteArray();
		else
			return read(results, getBlockNumber(nextAddress));
	}

	public void write(byte[] data) throws Exception {
		long startPosition = getOffset(getNextFreeBlock());
		rwrite(data, 0, startPosition);
	}

	private void rwrite(byte[] data, int startPosInArray, long startPosInFile)
			throws Exception {
		if (data.length - startPosInArray > BlockSettings.DATA_LENGTH) {
			byte[] toWrite = subArray(data, startPosInArray,
					(int) BlockSettings.DATA_LENGTH);
			// Writing the header
			virtualDisk.write(startPosInFile, BlockSettings.DATA_LENGTH);
			// Writing the data
			virtualDisk.write(startPosInFile + BlockSettings.HEADER_LENGTH,
					toWrite);
			// Writing the next address
			long startPos = getOffset(getNextFreeBlock());
			virtualDisk
					.write(startPosInFile + BlockSettings.NEXT_ADDRESS_START,
							startPos);
			rwrite(data, startPosInArray + (int) BlockSettings.DATA_LENGTH,
					startPos);
		} else {
			logger.debug("Suup Dawg?!");
			int numWrite = data.length - startPosInArray;
			byte[] toWrite = subArray(data, startPosInArray, numWrite);
			virtualDisk.write(startPosInFile, numWrite);
			virtualDisk.write(startPosInFile + BlockSettings.HEADER_LENGTH,
					toWrite);
			byte[] empty = new byte[(int) BlockSettings.HEADER_LENGTH];
			virtualDisk.write(startPosInFile + BlockSettings.HEADER_LENGTH
					+ BlockSettings.DATA_LENGTH, empty);
		}
	}

	public byte[] longToBytes(long x) {
		ByteBuffer buffer = ByteBuffer.allocate(8);
		buffer.putLong(x);
		return buffer.array();
	}

	private byte[] subArray(byte[] data, int start, int length) {
		byte[] tempArr = new byte[length];
		for (int i = 0; i < length; ++i) {
			tempArr[i] = data[start + i];
		}
		return tempArr;
	}

	private long getBlockNumber(long offset) {
		return offset / BlockSettings.BLOCK_SIZE;
	}

	private long getOffset(long blockNumber) {
		return blockNumber * BlockSettings.BLOCK_SIZE;
	}

	/*
	 * public void write(byte[] data) throws IOException { long
	 * currentWritePosition = getCurrentBlockNumber() BlockSettings.BLOCK_SIZE +
	 * BlockSettings.HEADER_LENGTH;
	 * logger.debug("Value of currentWritePosition:" + currentWritePosition); if
	 * (data.length <= BlockSettings.BLOCK_SIZE -
	 * BlockSettings.NEXT_ADDRESS_LENGTH - BlockSettings.HEADER_LENGTH) {
	 * logger.debug("Data fits into current block, writing data");
	 * virtualDisk.write(currentWritePosition, data); // write magic number to
	 * beginning of block only after filling block virtualDisk
	 * .seek(currentWritePosition - BlockSettings.HEADER_LENGTH);
	 * virtualDisk.writeLong(data.length); // write the next address as
	 * NULL_NEXT_ADDRESS virtualDisk.seek(currentWritePosition +
	 * BlockSettings.DATA_LENGTH); virtualDisk.writeLong(BlockSettings.UNUSED);
	 * } // we don't write a next block address because data has bit into this
	 * // block else
	 * logger.debug("Byte array too big for current block, partitioning data");
	 * }
	 */

	public long getNextFreeBlock() throws Exception {
		logger.debug("getNextFreeBlock was called");
		long currentPosition = virtualDisk.getFilePosition();
		logger.debug("Stored current position: " + currentPosition);
		long result = getNextFreeBlock(0);
		virtualDisk.seek(currentPosition);
		return result;
	}

	private long getNextFreeBlock(long blockNumber) throws IOException {
		virtualDisk.seek(blockNumber * BlockSettings.BLOCK_SIZE);
		logger.debug("virtualDisk file pointer is now at position: "
				+ virtualDisk.getFilePosition());
		try {
			long result = virtualDisk.readLong();
			logger.debug("BlockNumber: " + blockNumber + " header length: "
					+ result);
			if (isUnused(result))
				return blockNumber;
			else
				return getNextFreeBlock(blockNumber + 1);
		} catch (EOFException ex) {
			logger.debug("Creating free space");
			setupEmptyBlock(blockNumber);
			return blockNumber;
		}
	}

	private void setupEmptyBlock(long blockNumber) throws IOException {
		long currentPosition = blockNumber * BlockSettings.BLOCK_SIZE;
		virtualDisk.write(new byte[(int) BlockSettings.BYTES_TO_GROW]);
		virtualDisk.seek(currentPosition);
	}

	private boolean isUnused(long result) {
		return (result == BlockSettings.UNUSED);
	}

	public long getCurrentBlockNumber() {
		try {
			long currentPosition = virtualDisk.getFilePosition();
			return (currentPosition + 1) / BlockSettings.BLOCK_SIZE;
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

		Block block = new Block(getCurrentBlockNumber()
				* BlockSettings.BLOCK_SIZE);
		return block;
	}

	public long getCurrentBlockStartingAddress() {
		return getCurrentBlockNumber() * BlockSettings.BLOCK_SIZE;
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
	 * @throws IOException
	 */
	public long getNextBlock() throws IOException {
		long currentPosition = virtualDisk.getFilePosition();
		long nextStartingAddress = -1;
		try {
			virtualDisk.seek(getCurrentBlockNumber() * BlockSettings.BLOCK_SIZE
					+ BlockSettings.NEXT_ADDRESS_START);
			nextStartingAddress = virtualDisk.readLong();
		} catch (Exception ex) {
			// TODO: some meaningful logging
			ex.printStackTrace();
		} finally {
			virtualDisk.seek(currentPosition);
		}
		return getBlockNumber(nextStartingAddress);
	}

	public void combineBlocks(long firstOffset, long secondOffset)
			throws IOException {
		try {
			long offset = firstOffset + BlockSettings.NEXT_ADDRESS_START;
			virtualDisk.seek(offset);
			virtualDisk.writeLong(secondOffset);
		} catch (Exception ex) {
			// TODO: Some meaningful logging
		} finally {
			virtualDisk.seek(secondOffset);
		}
	}

	public boolean hasNextBlock() throws IOException {
		return getNextBlock() != -1;
	}
}
