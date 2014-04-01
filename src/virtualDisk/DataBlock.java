package virtualDisk;

public class DataBlock extends Block {
	/**
	 * Represents the fixed size block inside memory in which the system can
	 * store data. Maximum Block Size is 16 Bytes
	 */
	private long lengthOfData;
	private long startingAddressOfNextBlock;
	private long maximumBlockLength;

	public DataBlock(long offset) {
		super(offset);
	}

	public void setOffset(long offset) {
		this.offset = offset;
	}

	public long getOffset() {
		return this.offset;
	}
}
