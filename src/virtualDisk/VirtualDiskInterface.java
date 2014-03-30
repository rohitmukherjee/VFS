package virtualDisk;

import java.io.IOException;

public interface VirtualDiskInterface {

	// TODO: Adding JavaDoc to all methods with parameters, returns etc.

	void deleteDisk() throws IOException;

	void deleteData(long position, long size);

	byte[] getDiskInformation();

	long getDiskSize() throws IOException;

	long getFreeSpace();

	long getAllocatedSpace();

	long getNextFreeBlock();

	void write(long position, byte[] data) throws IOException;

	long write(byte[] data);

	byte[] read(long position, int size) throws IOException;

}
