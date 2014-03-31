package virtualDisk;

public interface DataBlockInterface extends BlockInterface {

	/**
	 * write the 'data' byte to the specified position position here is the
	 * offset of the block (starting at 0)
	 * 
	 * @param position
	 * @param data
	 */
	void write(long position, byte data);

	/**
	 * write the byte[] data into the block starting at position position here
	 * is the offset of the block (starting at 0)
	 * 
	 * @param position
	 * @param data
	 */
	void write(long position, byte[] data);

	/**
	 * Read a byte at specified position. Position here is the offset of the
	 * block (starting at 0)
	 * 
	 * @param position
	 * @return byte read at given position
	 */
	byte read(long position);

	/**
	 * Read
	 * 
	 * @param position
	 * @param data
	 */
	void read(long position, byte[] data);

	/**
	 * Returns the position on the virtual disk at which the next block begins
	 * 
	 * @return the position
	 */
	long getStartingAddressOfNextBlock();

	/**
	 * set the starting position of the next block
	 **/
	void setStartingAddressOfNextBlock(long address);

	/**
	 * Returns the total size of data on the block
	 * 
	 * @return size of data on the block
	 */
	long getDataSize();

	/**
	 * sets the size of data on the block. This means that either the block
	 * "grows" or "shrinks"
	 * 
	 * @return
	 */
	void setDataSize();

	/**
	 * returns amount of free space in bytes on the block
	 * 
	 * @return
	 */
	long getFreeSize();

	/**
	 * Deletes the block and prevents future usage
	 */
	void freeBlock();

	/**
	 * indicates whether the block has been freed or not
	 * 
	 * @return freed or not
	 */
	boolean isOccupied();
}
