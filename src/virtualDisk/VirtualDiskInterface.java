package virtualDisk;

public interface VirtualDiskInterface {

	// TODO:Implementation of the functions below in VirtualDisk implementation
	// TODO: Adding JavaDoc to all methods with parameters, returns etc.

	void loadDisk();

	void deleteDisk();

	void deleteData(long position, long size);

	byte[] getDiskInformation();

	long getDiskSize();

	long getFreeSpace();

	long getAllocatedSpace();

	long getNextFreeBlock();

	long write(long position, byte[] data);

	long write(byte[] data);

	byte[] read(long position, long size);

}
