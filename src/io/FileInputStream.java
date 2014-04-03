package io;

import java.io.IOException;
import java.io.InputStream;

import virtualDisk.BlockReader;

/**
 * This class implements the InputStream interface. It abstracts away the lower
 * level details of reading from the file block allowing us to easily extend the
 * system
 * 
 * @author ROHIT
 * 
 */
public class FileInputStream extends InputStream {

	private int readBytes;
	private BlockReader reader;
	private long length;

	public FileInputStream(BlockReader reader, long length) {
		this.length = length;
		this.reader = reader;
		this.readBytes = 0;
	}

	@Override
	public int read() throws IOException {
		if (readBytes >= length) {
			return -1;
		}
		reader.reset();
		readBytes++;
		int readByte = reader.read();
		// Mark Method of Input Stream does nothing. It is there only to fulfill
		// the contract
		reader.mark(0);
		return readByte;
	}
}
