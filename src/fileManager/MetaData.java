package fileManager;

import java.io.IOException;
import java.nio.ByteBuffer;

import utils.BlockSettings;
import utils.MetaDataUtilities;

public class MetaData {

	private String name;
	private byte type;
	private long blockNumber;
	private long parent;
	private long timestamp;

	public MetaData(byte[] data) {
		ByteBuffer metaBuffer = ByteBuffer
				.allocate(BlockSettings.METADATA_LENGTH);
		metaBuffer.put(data);
		metaBuffer.flip();
		byte[] name = new byte[BlockSettings.FILENAME_LENGTH];
		// Order of extracting data from the MetaData block
		long positionToStore = metaBuffer.getLong();
		this.setBlockNumber(positionToStore);
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
		//set to a dummy value so we get errors in case we never properly set block number
		this.blockNumber = -1;
	}

	/**
	 * Converts MetaData object into an array of bytes for writing to the disk
	 * at block level
	 * 
	 * @return byte array containing MetaData
	 * @throws IOException
	 */
	public byte[] getBytes() throws IOException {
		return MetaDataUtilities.metaDataToBytes(this);
	}

	/**
	 * Getter method for name field
	 * 
	 * @return name of file/directory
	 */
	public String getName() {
		return name;
	}

	/**
	 * Setter method for name field
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Getter method for timestamp field
	 * 
	 * @return timestamp of a given file/directory
	 */
	public long getTimestamp() {
		return timestamp;
	}

	/**
	 * Setter method for timestamp
	 * 
	 * @param timestamp
	 */
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * Getter method for type field
	 * 
	 * @return Type, 1 for file and 0 for directory
	 */
	public Byte getType() {
		return type;
	}

	/**
	 * Setter method for type field
	 * 
	 * @param type
	 */
	public void setType(Byte type) {
		this.type = type;
	}

	/**
	 * Getter method for parent field
	 * 
	 * @return blockNumber of parent of a given file/directory
	 */
	public long getParent() {
		return parent;
	}

	/**
	 * Setter method for parent blockNumber
	 * 
	 * @param parent
	 */
	public void setParent(long parent) {
		this.parent = parent;
	}

	/**
	 * Getter method for parent field
	 * 
	 * @return blockNumber of parent of a given file/directory
	 */
	public long getBlockNumber() {
		return blockNumber;
	}

	/**
	 * Setter method for blockNumber field
	 * 
	 * @param blockNumber
	 *            for a given file/directory
	 */
	public void setBlockNumber(long blockNumber) {
		this.blockNumber = blockNumber;
	}

	/**
	 * Override hashCode since we overrode the equals method
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (blockNumber ^ (blockNumber >>> 32));
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + (int) (parent ^ (parent >>> 32));
		result = prime * result + type;
		return result;
	}

	/**
	 * Override equals method for MetaData
	 * Checks if all fields are the same except for timestamp since we might not know that 
	 * when searching
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MetaData other = (MetaData) obj;
		if (blockNumber != other.blockNumber)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (parent != other.parent)
			return false;
		if (type != other.type)
			return false;
		return true;
	}
	
	
}