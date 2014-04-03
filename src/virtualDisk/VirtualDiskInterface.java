package virtualDisk;

import java.io.IOException;

public interface VirtualDiskInterface {

	// TODO: checking JavaDoc of all methods with parameters, returns etc.

	/*
	 * Deletes the disk. After this action is complete the file will cease to
	 * exist
	 * 
	 * @throws IOException if something goes wrong when removing the .vdisk file
	 */
	void deleteDisk() throws IOException;

	/*
	 * This function will remove size number of bytes starting at location and
	 * ending at location+size. This will set all of these bytes to null byte so
	 * that it will not get confused with significant data in the future.
	 * 
	 * @param position location of the starting byte of the chunk of memory to
	 * be deleted
	 * 
	 * @param size the size of the chunk of memory that will be removed
	 */
	void deleteData(long position, long size) throws IOException;

	byte[] getDiskInformation();

	/*
	 * This function will return the size of the virtual disk
	 * 
	 * @throw IOException if there is problems accessing the meta-data of the
	 * file
	 * 
	 * @return the sie of the current Virtual Hard Disk
	 */
	long getDiskSize() throws IOException;

	/*
	 * Queries the disk to find out how much free space is left on the virtual
	 * disk
	 * 
	 * @return the size of the unused memory on the disk
	 */
	long getFreeSpace();

	/*
	 * Queries the disk to find out how much space is currently allocated on the
	 * virtual disk
	 * 
	 * @return the size of the unused memory on the disk. Note it should hold
	 * that: getDiskSize() = getFreeSpace() + getAllocatedSpace();
	 */
	long getAllocatedSpace();

	/*
	 * Writes data at the position. This ensures that no other data will be
	 * overwritten
	 * 
	 * @param position the starting position of where the new data will be
	 * placed
	 * 
	 * @param data the data that will be written to the file.
	 * 
	 * @throws IOException if the is a problem accessing the .vdisk file
	 */
	void write(long position, byte[] data) throws IOException;

	/*
	 * This reads the specified number of bytes from the location that is
	 * specified
	 * 
	 * @param position the position to start reading from
	 * 
	 * @param size the number of bytes to read
	 * 
	 * @return the byte[] from the information gathered
	 * 
	 * @throws IOException if there is a problem accesing the .vdisk file
	 */
	byte[] read(long position, int size) throws IOException;

	byte[] read(long position, long size) throws IOException;

}