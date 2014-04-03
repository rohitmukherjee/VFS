package virtualDisk;

public class BlockSettings {

	public static final long MAXIMUM_BLOCK_SIZE = 32;
	public static final long NEXT_ADDRESS_LENGTH = 8;
	public static final long HEADER_LENGTH = 8;
	public static final byte[] MAGIC_NUMBER = { 1, 1, 1, 1, 1, 1, 1, 1 };
	public static final byte[] UNUSED = { 0, 0, 0, 0, 0, 0, 0, 0 };
	public static final long NEXT_ADDRESS_START = MAXIMUM_BLOCK_SIZE
			- NEXT_ADDRESS_LENGTH;
}
