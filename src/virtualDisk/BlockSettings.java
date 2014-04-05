package virtualDisk;

public class BlockSettings {

	public static final int BLOCK_SIZE = 256;
	public static final int NEXT_ADDRESS_LENGTH = 8;
	public static final int HEADER_LENGTH = 8;
	public static final int MAGIC_NUMBER_LENGTH = 8;
	public static final long UNUSED = 0;
	public static final byte[] MAGIC_NUMBER = { 1, 1, 1, 1, 1, 1, 1, 1 };
	public static final int NEXT_ADDRESS_START = BLOCK_SIZE
			- NEXT_ADDRESS_LENGTH;
	public static final long DATA_LENGTH = BLOCK_SIZE - NEXT_ADDRESS_LENGTH
			- HEADER_LENGTH;
	public static final long BLOCKS_TO_INITIALIZE = 8;
	public static final long BYTES_TO_GROW = BLOCKS_TO_INITIALIZE * BLOCK_SIZE;
	// Number of blocks to allocate to root directory
	public static final long ROOT_SUPERBLOCK_SIZE = 10;
	public static final byte[] EMPTY_BUFFER = new byte[(int) DATA_LENGTH];

	// MetaData Specific Settings go here:
	public static final int METADATA_LENGTH = BLOCK_SIZE - HEADER_LENGTH
			- NEXT_ADDRESS_LENGTH;
	public static final int TIMESTAMP_LENGTH = 8;
	public static final int POSITION_LENGTH = 8;
	public static final int FILETYPE_LENGTH = 1;
	public static final int PARENT_LENGTH = 8;
	public static final int FILENAME_LENGTH = METADATA_LENGTH
			- TIMESTAMP_LENGTH - FILETYPE_LENGTH - PARENT_LENGTH
			- POSITION_LENGTH;
}
