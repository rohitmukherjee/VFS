package virtualDisk;

import java.io.DataOutput;
import java.io.IOException;
import java.io.OutputStream;

public class BlockWriter extends OutputStream implements DataOutput {

	private BlockManager BlockManager;
	private long blockNumber;

	public BlockWriter(BlockManager blockManager, long blockNumber, long offset) {
		this.BlockManager = blockManager;
		this.blockNumber = blockNumber;

	}

	@Override
	public void writeBoolean(boolean arg0) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void writeByte(int arg0) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void writeBytes(String arg0) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void writeChar(int arg0) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void writeChars(String arg0) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void writeDouble(double arg0) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void writeFloat(float arg0) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void writeInt(int arg0) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void writeLong(long arg0) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void writeShort(int arg0) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void writeUTF(String arg0) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void write(int b) throws IOException {
		// TODO Auto-generated method stub

	}

}
