package virtualDisk;

public class Block {
	protected long offset;
	protected static final int MAXIMUM_BLOCK_SIZE = 16;

	public Block(long offset) {
		this.offset = offset;
	}
}