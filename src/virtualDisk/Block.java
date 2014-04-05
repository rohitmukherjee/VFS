package virtualDisk;

import utils.BlockSettings;

public class Block {
	private long blockNumber;
	protected static final long MAXIMUM_BLOCK_SIZE = BlockSettings.BLOCK_SIZE;

	public Block(long blockNumber) {
		this.blockNumber = blockNumber;
	}

	public long getBlockNumber() {
		return blockNumber;
	}
}