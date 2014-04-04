package virtualDisk;

import java.io.DataInput;
import java.io.IOException;
import java.io.InputStream;

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
		try {
			byteBuffer = blockManager.readBlock(blockNumber);
		} catch (IOException e) {
			// TODO : Add some meaning logging
			e.printStackTrace();
		}

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
		return 0;
	}

	@Override
	public float readFloat() throws IOException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void readFully(byte[] arg0) throws IOException {
		arg0 = blockManager.read(0);
		// This leaves vDisk file pointer at 0
	}

	@Override
	public void readFully(byte[] arg0, int offset, int length)
			throws IOException {
		arg0 = blockManager.read(0, offset, length);
	}

	@Override
	public int readInt() throws IOException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String readLine() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long readLong() throws IOException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public short readShort() throws IOException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String readUTF() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int readUnsignedByte() throws IOException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int readUnsignedShort() throws IOException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int skipBytes(int arg0) throws IOException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int read() throws IOException {
		// TODO Auto-generated method stub
		return 0;
	}

}
