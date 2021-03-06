package fileManager;

import java.io.IOException;

public interface FileManagerInterface {

	/**
	 * Function writes metData for a particular File/Directory to the position
	 * passed in as a parameter
	 * 
	 * @param position
	 * @param metaData
	 * @throws Exception
	 * @throws IOException
	 */
	void writeMetaData(long position, MetaData metaData) throws IOException,
			Exception;

	void writeReplaceMetaData(MetaData metaData) throws IOException, Exception;

	/**
	 * Get the data of a particular file/directory starting at the position
	 * passed in as a parameter. Data for a file is the actual content whereas
	 * for a directory it is the positions of the files/sub - directories
	 * contained in it
	 * 
	 * @param position
	 * @return
	 * @throws IOException
	 * @throws Exception
	 */
	byte[] getData(MetaData metaData) throws IOException, Exception;

	/**
	 * Returns a MetaData object containing information about the file/directory
	 * starting at position passed in as a parameter
	 * 
	 * @param position
	 * @return
	 * @throws Exception
	 * @throws IOException
	 */
	MetaData getMetaData(long position) throws IOException, Exception;

	MetaData getMetaData(MetaData metaData) throws IOException, Exception;

	/**
	 * Write a file/Directory starting at the position passed in as a parameter.
	 * The first block is always the metadata followed by data in the following
	 * blocks
	 * 
	 * @param position
	 * @param meta
	 * @param data
	 * @throws Exception
	 */
	void createFile(MetaData meta, byte[] data) throws Exception;

	void createDirectory(MetaData meta) throws Exception;

	void writeData(MetaData meta, byte[] data) throws Exception;

	void deleteFile(MetaData metaData) throws Exception;

	void deleteDirectory(MetaData metaData) throws Exception;

	MetaData search(String path) throws IOException, Exception;

	void rewriteMetaData(MetaData newMetaData) throws Exception;
}
