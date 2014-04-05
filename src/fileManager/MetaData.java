package fileManager;

import java.io.IOException;
import java.nio.ByteBuffer;

import utils.BlockSettings;

public class MetaData {

	private String name;
	private Byte type;
	private long position;
	private long parent;
	private long timestamp;

	public MetaData(byte[] data) {
		ByteBuffer metaBuffer = ByteBuffer
				.allocate(BlockSettings.METADATA_LENGTH);
		metaBuffer.put(data);
		metaBuffer.flip();
		byte[] name = new byte[BlockSettings.FILENAME_LENGTH];
		// Order of extracting data from the MetaData block
		this.setPosition(metaBuffer.getLong());
		this.setType(metaBuffer.get());
		metaBuffer.get(name);
		this.setName(MetaDataUtilities.bytesToString(name));
		this.setTimestamp(metaBuffer.getLong());
		this.setParent(metaBuffer.getLong());
	}

	/**
	 * alternate constructor which creates MetaData object not already in a
	 * block ie. from the host file system
	 * 
	 * @param name
	 * @param parent
	 * @param type
	 * @param timestamp
	 */
	public MetaData(String name, long parent, byte type, long timestamp) {
		this.name = name;
		this.parent = parent;
		this.type = type;
		this.timestamp = timestamp;
	}

	public byte[] getBytes() throws IOException {
		return MetaDataUtilities.metaDataToBytes(this);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public Byte getType() {
		return type;
	}

	public void setType(Byte type) {
		this.type = type;
	}

	public long getParent() {
		return parent;
	}

	public void setParent(long parent) {
		this.parent = parent;
	}

	public long getPosition() {
		return position;
	}

	public void setPosition(long position) {
		this.position = position;
	}
}