package virtualDisk;

import java.io.DataInput;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;

import exceptions.NotImplementedException;

/**
 * This class is a stream wrapper around the byte oriented io that happens with
 * chunks. This is what we are going to use to read from blocks
 * 
 * @author ROHIT
 * 
 */
public class BlockReader extends InputStream implements DataInput {

	private BlockManager blockManager;
	private long blockNumber;
	private long positionInBlock;
	private byte[] byteBuffer = new byte[(int) BlockSettings.DATA_LENGTH];

	private long blockMark;
	private long positionMark;

	public BlockReader(BlockManager blockManager, long blockNumber) {
		this(blockManager, blockNumber, BlockSettings.HEADER_LENGTH);
	}

	public BlockReader(BlockManager blockManager, long blockNumber, long offset) {
		this.blockManager = blockManager;
		this.positionInBlock = offset;
		this.positionMark = offset;
		this.blockMark = blockNumber;
		this.blockNumber = blockNumber;
	}

	@Override
	public void reset() {
		blockNumber = blockMark;
		positionInBlock = positionMark;
	}

	@Override
	public void mark(int position) {
		positionMark = positionInBlock;
		blockMark = blockNumber;
	}

	@Override
	public boolean readBoolean() throws IOException {
		if (positionInBlock > BlockSettings.NEXT_ADDRESS_START)
			moveToNextBlock();
		positionInBlock++;
		return byteBuffer[(int) positionInBlock] != 0;
	}

	private void moveToNextBlock() throws IOException {
		long nextBlockNumber = blockManager.getNextBlock(this.blockNumber);
		if (nextBlockNumber != 0) {
			byteBuffer = blockManager.readBlock(nextBlockNumber);
			blockNumber = nextBlockNumber;
			positionInBlock = BlockSettings.HEADER_LENGTH;
		}
		// TODO: Have to handle end of file more elegantly
		else
			throw new IOException("EOF Reached");
	}

	@Override
	public byte readByte() throws IOException {
		if (positionInBlock > BlockSettings.NEXT_ADDRESS_START)
			moveToNextBlock();
		positionInBlock++;

		return byteBuffer[(int) positionInBlock];
	}

	@Override
	public char readChar() throws IOException {
		if (positionInBlock > BlockSettings.NEXT_ADDRESS_START)
			moveToNextBlock();
		positionInBlock++;
		return (char) byteBuffer[(int) positionInBlock];
	}

	@Override
	public double readDouble() throws IOException {
		if (positionInBlock > BlockSettings.NEXT_ADDRESS_START)
			moveToNextBlock();
		positionInBlock++;

		return getByteBuffer(8).getDouble();
	}

	@Override
	public float readFloat() throws IOException {
		if (positionInBlock > BlockSettings.NEXT_ADDRESS_START)
			moveToNextBlock();
		positionInBlock++;

		return getByteBuffer(4).getFloat();
	}

	// Assumption is that a read won't be attempted before a write
	@Override
	public void readFully(byte[] arg0) throws IOException {
		if (bufferIsEmpty())
			byteBuffer = blockManager.readBlock(blockNumber);
		byte[] temp = blockManager.read(blockNumber);
		for (int i = 0; i < arg0.length; i++)
			arg0[i] = temp[i];
		// This leaves vDisk file pointer at getOffset(blockNumber)
	}

	@Override
	public void readFully(byte[] arg0, int offset, int length)
			throws IOException {
		arg0 = blockManager.read(0, offset, length);
	}

	@Override
	public int readInt() throws IOException {
		if (positionInBlock > BlockSettings.NEXT_ADDRESS_START)
			moveToNextBlock();
		positionInBlock++;

		return getByteBuffer(4).getInt();
	}

	@Override
	public String readLine() throws IOException {
		throw new NotImplementedException();
	}

	@Override
	public long readLong() throws IOException {
		if (positionInBlock > BlockSettings.NEXT_ADDRESS_START)
			moveToNextBlock();
		positionInBlock++;

		return getByteBuffer(8).getLong();
	}

	@Override
	public short readShort() throws IOException {
		if (positionInBlock > BlockSettings.NEXT_ADDRESS_START)
			moveToNextBlock();
		positionInBlock++;

		return getByteBuffer(2).getShort();
	}

	@Override
	public String readUTF() throws IOException {
		throw new NotImplementedException();
	}

	@Override
	public int readUnsignedByte() throws IOException {
		throw new NotImplementedException();
	}

	@Override
	public int readUnsignedShort() throws IOException {
		throw new NotImplementedException();
	}

	@Override
	public int skipBytes(int arg0) throws IOException {
		throw new NotImplementedException();
	}

	@Override
	public int read() throws IOException {
		return readByte() & 0xFF;
	}

	private ByteBuffer getByteBuffer(int size) throws IOException {
		byte[] data = new byte[size];
		for (int i = 0; i < size; ++i) {
			if (positionInBlock > BlockSettings.NEXT_ADDRESS_START)
				moveToNextBlock();
			positionInBlock++;
			data[i] = byteBuffer[(int) positionInBlock];
		}
		return ByteBuffer.wrap(data);
	}

	private boolean bufferIsEmpty() {
		return Arrays.equals(byteBuffer, BlockSettings.EMPTY_BUFFER);
	}
}
