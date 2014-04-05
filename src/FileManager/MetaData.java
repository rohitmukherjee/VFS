package FileManager;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;

public class MetaData {
	
	public long address;
	public String name;
	public long parent;
	public DiskEntry type;
	public long timestamp;
	
	//maybe should make this a class?
	public enum DiskEntry {
		FILE(0), DIRECTORY(1);
		
		private final int index;
	    private DiskEntry(int index) {
	        this.index = index;
	    }

	    public int getIndex() {
	        return index;
	    }
	    
	    public static DiskEntry fromInteger(int x) throws Exception {
	        switch(x) {
	        case 0:
	            return FILE;
	        case 1:
	            return DIRECTORY;
	        default:
	        	throw new Exception("cannot create enum from int greater than 1");
	        }
	    }
		
	}
	
	public MetaData(long address, byte[] data) throws Exception {
		this.address = address;
		bytesToName(Arrays.copyOfRange(data, 0, utils.BlockSettings.META_NAME_SIZE));
		bytesToParent(Arrays.copyOfRange(data, utils.BlockSettings.META_PARENT_START,
				utils.BlockSettings.META_FILE_TYPE_START));
		bytesToType(Arrays.copyOfRange(data, utils.BlockSettings.META_FILE_TYPE_START,
				utils.BlockSettings.META_TIMESTAMP_START));
		bytesToTimeStamp(Arrays.copyOfRange(data, utils.BlockSettings.META_TIMESTAMP_START, 
				utils.BlockSettings.META_END));
	}
	
	/**
	 * alternate ctor which creates metadata object not already in a block
	 * @param name 
	 * @param parent 
	 * @param type
	 * @param timestamp
	 */
	public MetaData(String name, long parent, DiskEntry type, long timestamp) {
		this.name = name;
		this.parent = parent;
		this.type = type;
		this.timestamp = timestamp;
		//TODO get an address?
	}
	
//	public updateBlock() {
//		
//	}
	
	public byte[] getBytes() throws IOException {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		stream.write(nameToBytes());
		stream.write(parentToBytes());
		stream.write(typeToBytes());
		stream.write(timeStampToBytes());
		byte [] shortData = stream.toByteArray();
		return Arrays.copyOf(shortData, (int) utils.BlockSettings.DATA_LENGTH);
	}
	
	
	private byte[] nameToBytes() {
		byte [] nameBytes = name.getBytes(Charset.forName("UTF-8"));
		short length = (short) nameBytes.length;
		ByteBuffer buffer = ByteBuffer.allocate(2);
		buffer.putShort(length);
		byte[] lengthBytes = buffer.array();
		byte[] allBytes = new byte[utils.BlockSettings.META_NAME_SIZE];
		System.arraycopy(lengthBytes, 0, allBytes, 0, 2);
		System.arraycopy(nameBytes, 0, allBytes, 2, nameBytes.length);
		return allBytes;
	}
	
	private void bytesToName(byte[] bytes) throws UnsupportedEncodingException {
		ByteBuffer buffer = ByteBuffer.wrap(bytes);
		short length = buffer.getShort();
		byte[] stringBytes = new byte[length];
		buffer.get(stringBytes, 0, length);
		name = new String(stringBytes, "UTF-8");
	}
	
	private byte[] parentToBytes() {
	    ByteBuffer buffer = ByteBuffer.allocate(8);
	    buffer.putLong(parent);
	    byte[] temp = buffer.array();
		return Arrays.copyOf(temp, utils.BlockSettings.META_FILE_TYPE_START 
				- utils.BlockSettings.META_PARENT_START);
	}
	
	private void bytesToParent(byte[] bytes) {
		ByteBuffer buffer = ByteBuffer.wrap(bytes);
		parent = buffer.getLong();
	}
	
	private byte[] typeToBytes() {
		ByteBuffer buffer = ByteBuffer.allocate(4);
		buffer.putInt(type.getIndex());
		byte[] temp = buffer.array();
		return Arrays.copyOf(temp, utils.BlockSettings.META_TIMESTAMP_START 
				- utils.BlockSettings.META_FILE_TYPE_START);
	}
	
	private void bytesToType(byte[] bytes) throws Exception {
		ByteBuffer buffer = ByteBuffer.wrap(bytes);
		type = DiskEntry.fromInteger(buffer.getInt());
	}
	
	private byte[] timeStampToBytes() {
	    ByteBuffer buffer = ByteBuffer.allocate(8);
	    buffer.putLong(parent);
	    byte[] temp = buffer.array();
	    return Arrays.copyOf(temp, utils.BlockSettings.META_END 
	    		- utils.BlockSettings.META_TIMESTAMP_START);
	}
	
	private void bytesToTimeStamp(byte[] bytes) {
		ByteBuffer buffer = ByteBuffer.wrap(bytes);
		timestamp = buffer.getLong();
	}
	

}
