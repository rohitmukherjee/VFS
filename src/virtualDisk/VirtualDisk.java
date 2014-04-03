package virtualDisk;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

// logging imports
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import exceptions.IncorrectPathException;

public class VirtualDisk implements VirtualDiskInterface {
	private RandomAccessFile disk;
	private final String path;
	private static Logger logger;

	public VirtualDisk(String path) {
		this.path = path;
		setupLogging();
	}

	public void setupDisk() throws Exception {
		if (fileExists(path)) {
			logger.error("Could not setup disk");
			throw new IncorrectPathException("File already exists");
		}
		disk = new RandomAccessFile(new File(path), "rw");
		logger.info("Successfully created disk");
		setupSuperBlock();
		setupRootDirectory();
	}

	private void setupSuperBlock() {
		// TODO Auto-generated method stub

	}

	private void setupRootDirectory() {
		// TODO Auto-generated method stub
	}

	@Override
	public long getDiskSize() throws IOException {
		return disk.length();
	}

	@Override
	public void deleteDisk() throws IOException {
		closeDisk();
		File diskToDelete = new File(this.path);
		diskToDelete.delete();
	}

	@Override
	public void deleteData(long position, long size) throws IOException {
		// THIS IS BAD-- the cast can cause problems
		byte[] emptyBuf = new byte[(int) size];
		write(position, emptyBuf);

	}

	@Override
	public byte[] getDiskInformation() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getFreeSpace() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getAllocatedSpace() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void write(long position, byte[] data) throws IOException {
		// @TODO ensure that were aren't writing over used data
		disk.seek(position);
		disk.write(data);
	}

	@Override
	public byte[] read(long position, long size) throws IOException {
		byte[] bytesRead = new byte[(int) size];
		disk.seek(position);
		disk.read(bytesRead);
		return bytesRead;
	}

	private void setupLogging() {
		logger = Logger.getLogger(VirtualDisk.class);
		BasicConfigurator.configure();
	}

	private boolean fileExists(String path) {
		File file = new File(path);
		if (file.exists())
			return true;
		return false;
	}

	private void closeDisk() throws IOException {
		disk.close();
	}

	public long getFilePosition() throws IOException {
		return disk.getFilePointer();
	}

	public void seek(long filePointer) throws IOException {
		disk.seek(filePointer);
	}

	public void write(byte[] data) throws IOException {
		disk.write(data);
	}

	public long readLong() throws IOException {
		return disk.readLong();
	}

	public void writeLong(long offset) throws IOException {
		disk.writeLong(offset);
	}

	@Override
	public byte[] read(long position, int size) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}
}