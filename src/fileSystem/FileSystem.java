package fileSystem;

import java.io.IOException;
import java.util.Date;

import utils.BlockSettings;
import exceptions.CannotAccessDiskException;
import exceptions.DiskStructureException;
import exceptions.InvalidDirectoryException;
import exceptions.InvalidFileException;
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
	public String[] getChildren(String path) throws DiskStructureException {
		MetaData[] childrenMetaData = null;
		MetaData parent = fileManager.search(path);
		if (isValidDirectory(parent))
			childrenMetaData = fileManager.getChildrenMeta();
		String[] childrenPaths = new String[childrenMetaData.length];
		for (int i = 0; i < childrenMetaData.length; i++)
			childrenPaths[i] = childrenMetaData[i].getName();
		if (childrenPaths != null)
			return childrenPaths;
		else
			throw new DiskStructureException(
					"Cannot getChildren because the disk structure is invalid");
	}

	private boolean isValidDirectory(MetaData parent) {
		// TODO Auto-generated method stub
		return false;
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
	public void deleteDirectory(String path) throws InvalidDirectoryException {
		MetaData directoryMetaData = fileManager.search(path);
		if (isValidDirectory(directoryMetaData.getName())
				&& !(isRoot(directoryMetaData)))
		// Some kind of recursive delete method inside fileManager
		{
		} else
			throw new InvalidDirectoryException(
					"The directory cannot be deleted");
	}

	@Override
	public long getFreeMem() throws CannotAccessDiskException {
		try {
			return fileManager.getFreeMemory();
		} catch (IOException ex) {
			throw new CannotAccessDiskException(
					"The getFreeMemory operation failed because the disk cannot be accessed");
		}
	}

	@Override
	public long getOccupiedMem() throws CannotAccessDiskException {
		try {
			return fileManager.getOccupiedMemory();
		} catch (IOException ex) {
			throw new CannotAccessDiskException(
					"The getOccupiedMemory operation failed because the disk cannot be accessed");
		}
	}

	@Override
	public long getTotalMem() throws CannotAccessDiskException {
		try {
			return fileManager.getTotalMemory();
		} catch (IOException ex) {
			throw new CannotAccessDiskException(
					"The getTotalMemory operation failed because the disk cannot be accessed");
		}
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
					"Rename directory failed because the directory is invalid");
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
	public void renameFile(String oldName, String newName)
			throws InvalidFileException {
		MetaData originalMetaData = fileManager.search(oldName);
		if (isValidFile(originalMetaData.getName())) {
			originalMetaData.setName(newName);
			originalMetaData.setTimestamp(new Date().getTime());
		} else
			throw new InvalidFileException(
					"Rename file failed because the file is invalid");
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