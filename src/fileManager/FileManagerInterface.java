package fileManager;

public interface FileManagerInterface {

	/**
	 * Function writes metData for a particular File/Directory to the position
	 * passed in as a parameter
	 * 
	 * @param position
	 * @param metaData
	 */
	void writeMetaData(long position, MetaData metaData);

	/**
	 * Get the data of a particular file/directory starting at the position
	 * passed in as a parameter. Data for a file is the actual content whereas
	 * for a directory it is the positions of the files/sub - directories
	 * contained in it
	 * 
	 * @param position
	 * @return
	 */
	byte[] getData(long position);

	/**
	 * Returns a MetaData object containing information about the file/directory
	 * starting at position passed in as a parameter
	 * 
	 * @param position
	 * @return
	 */
	MetaData getMetaData(long position);

	/**
	 * Write a file/Directory starting at the position passed in as a parameter.
	 * The first block is always the metadata followed by data in the following
	 * blocks
	 * 
	 * @param position
	 * @param meta
	 * @param data
	 */
	void writeFile(long position, MetaData meta, byte[] data);

	void search(String path);

}
