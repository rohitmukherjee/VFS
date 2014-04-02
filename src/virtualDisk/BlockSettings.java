package virtualDisk;

public class BlockSettings {

	public static final long MAXIMUM_BLOCK_SIZE = 32;
	public static final long NEXT_ADDRESS_LENGTH = 8;
	public static final long MAGIC_NUMBER_LENGTH = 8;
	public static final byte[] MAGIC_NUMBER = { 1, 1, 1, 1, 1, 1, 1, 1 };
	public static final byte[] UNUSED = { 0, 0, 0, 0, 0, 0, 0, 0 };
}
