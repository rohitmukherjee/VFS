package virtualDisk;

public interface BlockInterface {
	/**
	 * This function returns the position of the starting byte of the block This
	 * would be 0 for the superblock
	 * 
	 * @return position of the block on the disk
	 */
	long getBlockPosition();

	/***
	 * This function returns the size of the block (in bytes) on the disk
	 * 
	 * @return size of the block on the disk
	 */
	long getSize();
}