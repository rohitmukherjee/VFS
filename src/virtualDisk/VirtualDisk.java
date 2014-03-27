package virtualDisk;

import java.io.File;
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
	}

	@Override
	public long getDiskSize() {
		return 0;
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
}