package virtualDisk;

import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import utils.BlockSettings;


public class BlockManager {

	private VirtualDisk virtualDisk;
	private long blockSize = Block.MAXIMUM_BLOCK_SIZE;
	private Logger logger;

	public BlockManager(String pathName) {
		virtualDisk = new VirtualDisk(pathName);
		logger = Logger.getLogger(VirtualDisk.class);
		BasicConfigurator.configure();
	}

	/**
	 * First method that should be called when the BlockManager is initialized
	 * 
	 * @throws Exception
	 */
	public void setupBlocks() throws Exception {
		virtualDisk.setupDisk();
	}

	public byte[] readBlock(long blockNumber) throws IOException {
		return this.read(blockNumber, (int) BlockSettings.HEADER_LENGTH,
				(int) BlockSettings.DATA_LENGTH);
	}

	/**
	 * reads data into byte array starting from a particular byte array
	 * 
	 * @param blockNumber
	 * @return
	 * @throws IOException
	 */
	public byte[] read(long blockNumber, int offset, int length)
			throws IOException {
		byte[] temp = read(blockNumber);
		byte[] result = Arrays.copyOfRange(temp, offset, offset + length);
		return result;
	}

	/**
	 * reads data into byte array starting from a particular byte array
	 * 
	 * @param blockNumber
	 * @return
	 * @throws IOException
	 */
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

	/**
	 * Start writing data into available positions
	 * 
	 * @param data
	 * @throws Exception
	 */
	public void write(byte[] data) throws Exception {
		long startBlock = getNextFreeBlock();
		write(data, startBlock);
	}

	public void write(byte[] data, long blockNumber) throws Exception {
		long startPosition = getOffset(blockNumber);
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

	public void delete(long blockNumber) throws Exception {
		if (blockNumber == 0) {
			logger.warn("Nice try, can't delete the root");
			return;
		}

		virtualDisk.seek(getOffset(blockNumber));
		virtualDisk.writeLong(BlockSettings.UNUSED);
		// get the address of next block
		logger.debug("deleting data in block " + blockNumber);
		virtualDisk.seek(getOffset(blockNumber)
				+ BlockSettings.NEXT_ADDRESS_START);
		long fileAddress = virtualDisk.getFilePosition();
		long nextAddress = virtualDisk.readLong();

		while (nextAddress != 0) {
			virtualDisk.seek(fileAddress);
			virtualDisk.writeLong(0);
			virtualDisk.seek(nextAddress);
			virtualDisk.writeLong(BlockSettings.UNUSED);
			virtualDisk.seek(getOffset(blockNumber)
					+ BlockSettings.NEXT_ADDRESS_START);
			fileAddress = virtualDisk.getFilePosition();
			nextAddress = virtualDisk.readLong();
		}
		// reset pointer to current first free block
		virtualDisk.seek(getNextFreeBlock());
	}

	/**
	 * Function scans the disk for free memory and returns the absolute position
	 * of the first free block
	 * 
	 * @return position of the first available/free block
	 * @throws Exception
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

	/**
	 * returns the current block number
	 * 
	 * @return current block number
	 */
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

	public long getNextBlock(long blockNumber) throws IOException {
		long currentPosition = virtualDisk.getFilePosition();
		virtualDisk.seek(getOffset(blockNumber));
		long nextBlockStart = this.getNextBlock();
		virtualDisk.seek(currentPosition);
		return nextBlockStart;
	}
}
