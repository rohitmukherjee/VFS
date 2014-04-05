package utils;

public class BlockSettings {

	public static final long BLOCK_SIZE = 256;
	public static final long NEXT_ADDRESS_LENGTH = 8;
	public static final long HEADER_LENGTH = 8;
	public static final long MAGIC_NUMBER_LENGTH = 8;
	public static final long UNUSED = 0;
	public static final byte[] MAGIC_NUMBER = { 1, 1, 1, 1, 1, 1, 1, 1 };
	public static final long NEXT_ADDRESS_START = BLOCK_SIZE
			- NEXT_ADDRESS_LENGTH;
	public static final long DATA_LENGTH = BLOCK_SIZE - NEXT_ADDRESS_LENGTH
			- HEADER_LENGTH;
	public static final long BLOCKS_TO_INITIALIZE = 8;
	public static final long BYTES_TO_GROW = BLOCKS_TO_INITIALIZE * BLOCK_SIZE;
	// Number of blocks to allocate to root directory
	public static final long ROOT_SUPERBLOCK_SIZE = 10;
	public static final byte[] EMPTY_BUFFER = new byte[(int) DATA_LENGTH];
	public static final int META_NAME_SIZE = (int)BLOCK_SIZE/2;
	public static final int META_PARENT_START = META_NAME_SIZE;
	public static final int META_FILE_TYPE_START = META_PARENT_START + 8;
	public static final int META_TIMESTAMP_START = META_FILE_TYPE_START + 4;
	public static final int META_END = META_TIMESTAMP_START + 8;
	public static final long META_LEFTOVER = DATA_LENGTH - META_NAME_SIZE - META_FILE_TYPE_START
			-META_TIMESTAMP_START - META_PARENT_START;
}
