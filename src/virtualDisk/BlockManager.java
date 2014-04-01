package virtualDisk;

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

	public void setupDisk() throws Exception {
		virtualDisk.setupDisk();
		BitmapBlock bitmapBlock = new BitmapBlock(blockSize);
		byte[] firstBitmapBlock = bitmapBlock.getBitmap();
		logger.debug("Created firstBitmap Block");
		// First block should be occupied hence we should set first bit of the
		// vector to 1
		firstBitmapBlock[0] = 1;
		virtualDisk.write(0, firstBitmapBlock);
		logger.debug("Finished writing first bitmap block");
	}

	public long getVirtualDiskOffset(long offset) {
		return blockSize + offset;
	}

	public long getCurrentBlockNumber() {
		try {
			long currentPosition = virtualDisk.getFilePosition();
			currentPosition -= blockSize;
			return currentPosition / blockSize;
		} catch (Exception ex) {
			return -1;
		}
	}

	// Only for testing purposes, will definitely be removed in production code
	public VirtualDisk getVirtualDisk() {
		return virtualDisk;
	}

	/**
	 * Function returns the current Block (both Data and Bitmap)
	 * 
	 * @return
	 */
	private Block getCurrentBlock() {
		Block block = new Block(getCurrentBlockNumber() * blockSize + blockSize);
		return block;
	}
}
