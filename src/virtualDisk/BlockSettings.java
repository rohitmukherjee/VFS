package virtualDisk;

public class BlockSettings {

	public static final long BLOCK_SIZE = 32;
	public static final long NEXT_ADDRESS_LENGTH = 8;
	public static final long HEADER_LENGTH = 8;
	public static final long UNUSED = 0;
	public static final byte[] MAGIC_NUMBER = { 1, 1, 1, 1, 1, 1, 1, 1 };
	public static final long NEXT_ADDRESS_START = BLOCK_SIZE
			- NEXT_ADDRESS_LENGTH;
	public static final long DATA_LENGTH = BLOCK_SIZE - NEXT_ADDRESS_LENGTH
			- HEADER_LENGTH;
	public static final long BLOCKS_TO_INITIALIZE = 8;
	public static final long BYTES_TO_GROW = BLOCKS_TO_INITIALIZE * BLOCK_SIZE;
}
