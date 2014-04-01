package virtualDisk;

import java.io.IOException;

public class DataBlock implements DataBlockInterface {
	//This temporary but we need to access the VirtualDisk class
	VirtualDisk vDisk = new VirtualDisk("path");
	long startPosition;
	long size;
	long written;
	boolean occupied;
	
	public DataBlock(long position, long size) {
		startPosition = position;
		this.size = size;
		written = 0;
		occupied = true;
	}
	
	@Override
	public long getBlockPosition() {
		return startPosition;
	}

	@Override
	public long getSize() {
		return size;
	}

	@Override
	public void write(long position, byte data) throws IOException {
		byte[] toWrite = new byte[1];
		toWrite[0] = data;
		vDisk.write(startPosition+position, toWrite);
		written += 1;
		
	}

	@Override
	public void write(long position, byte[] data) throws IOException {
		vDisk.write(startPosition+position, data);
		written += data.length;
	}

	@Override
	public byte read(long position) throws IOException {
		byte[] data = vDisk.read(startPosition + position, 1);
		return data[0];
	}

	@Override
	public void read(long position, byte[] data) throws IOException {
		data = vDisk.read(startPosition + position, 1);
	}

	@Override
	public long getStartingAddressOfNextBlock() {
		return startPosition + size;
	}

	@Override
	public void setStartingAddressOfNextBlock(long address) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public long getDataSize() {
		return written; //currently this is an overapproxmiaton will fix later
	}

	@Override
	public void setDataSize() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public long getFreeSize() {
		return size - written;
	}

	@Override
	public void freeBlock() throws IOException {
		byte[] emptyBuf = new byte[(int) size];
		vDisk.write(startPosition, emptyBuf);
		occupied = false;
		written = 0;
		
	}

	@Override
	public boolean isOccupied() {
		return occupied;
	}

}
