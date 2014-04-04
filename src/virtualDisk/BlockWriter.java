package virtualDisk;

import java.io.DataOutput;
import java.io.IOException;
import java.io.OutputStream;

public class BlockWriter extends OutputStream implements DataOutput {

	private BlockManager blockManager;
	private long blockNumber;
	private long positionInBlock;
	private byte[] writeBlock;

	private long blockMark;
	private long positionMark;

	public BlockWriter(BlockManager blockManager, long blockNumber) {
		this.blockManager = blockManager;
		
		writeBlock = new byte[(int) BlockSettings.DATA_LENGTH];
		this.positionInBlock = 0;
		this.positionMark = 0;
		this.blockMark = blockNumber;
		this.blockNumber = blockNumber;

	}

	public void writeToBuff(byte[] data) throws IOException {
		for(int i = 0; i < data.length; ++i) {
			if(positionInBlock > BlockSettings.DATA_LENGTH) 
				flush();
			else {
				writeBlock[(int) positionInBlock] = data[i];
				++positionInBlock;
			}
		}
	}
	
	@Override 
	public void flush() {
		try {
			blockManager.write(writeBlock);
			long newBlock = blockManager.getVirtualDisk().getFilePosition() / BlockSettings.BLOCK_SIZE;
			blockManager.combineBlocks(blockNumber, newBlock);
			blockNumber = newBlock;
			writeBlock = new byte[(int) BlockSettings.DATA_LENGTH];
		} catch (Exception e) {
			// TODO Meaningful logging
			e.printStackTrace();
		}		
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
		writeToBuff(arg0.getBytes());

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
