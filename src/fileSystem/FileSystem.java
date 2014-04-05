package fileSystem;

import java.util.Date;

import utils.BlockSettings;
import exceptions.InvalidDirectoryException;
import fileManager.FileManager;
import fileManager.MetaData;

public class FileSystem implements FileSystemInterface {

	FileManager fileManager;

	public FileSystem(String path) throws Exception {
		this.writeRoot();
		fileManager = new FileManager(path);
	}

	@Override
	public boolean writeFile(String path, byte[] content) {

		return false;
	}

	@Override
	public byte[] readFile(String path) {
		return null;
	}

	@Override
	public String[] getChildren(String path) {
		// get children bytes
		return null;
	}

	@Override
	public boolean isValidPath(String path) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isValidFile(String path) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void addNewDirectory(String path) {
		// TODO Auto-generated method stub
	}

	@Override
	public void deleteFile(String path) {
		MetaData fileMetaData = fileManager.search(path);
		if (fileMetaData != null)
		// Some kind of delete method inside fileManager
		{
		}
	}

	@Override
	public void deleteDirectory(String path) {
		MetaData directoryMetaData = fileManager.search(path);
		if (isValidDirectory(directoryMetaData.getName())
				&& !(isRoot(directoryMetaData)))
		// Some kind of recursive delete method inside fileManager
		{
		}
	}

	@Override
	public long getFreeMem() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getOccupiedMem() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getTotalMem() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void renameDirectory(String oldName, String newName)
			throws InvalidDirectoryException {
		MetaData originalMetaData = fileManager.search(oldName);
		if (isValidDirectory(originalMetaData.getName())
				&& !(isRoot(originalMetaData))) {
			originalMetaData.setName(newName);
			originalMetaData.setTimestamp(new Date().getTime());
		} else
			throw new InvalidDirectoryException(
					"Rename directory failed because the directory wasn't valid");
	}

	private boolean isRoot(MetaData metaData) {
		return metaData.getName().equals(BlockSettings.ROOT_NAME)
				|| metaData.getPosition() == BlockSettings.ROOT_POSITION;
	}

	private boolean isValidDirectory(String name) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void renameFile(String oldName, String newName) {
		// TODO Auto-generated method stub
	}

	// Writes the root directory at position 0
	private void writeRoot() throws Exception {
		MetaData rootMetaData = new MetaData(BlockSettings.ROOT_NAME,
				BlockSettings.ROOT_PARENT, BlockSettings.ROOT_TYPE,
				BlockSettings.ROOT_TIMESTAMP);
		rootMetaData.setPosition(BlockSettings.ROOT_POSITION);
		// Currently sends a block of zeroes
		fileManager.writeFile(BlockSettings.ROOT_POSITION, rootMetaData,
				new byte[BlockSettings.ROOT_SUPERBLOCK_SIZE]);
	}
}
