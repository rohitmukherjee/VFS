package fileSystem;

import java.io.IOException;
import java.util.Date;

import utils.BlockSettings;
import exceptions.FileAlreadyExistsException;
import exceptions.IncorrectPathException;
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
	public boolean writeFile(String path, byte[] content) throws Exception {
		MetaData meta;
		meta = fileManager.search(path);
		if (isFile(meta)) {
			throw new FileAlreadyExistsException();
		}

		String[] pathTokens = path.split("/");
		MetaData parentMeta = fileManager.search(path.substring(0,
				path.length() - pathTokens[pathTokens.length].length() - 1));
		if (isDirectory(parentMeta)) {
			MetaData newMeta = new MetaData(pathTokens[pathTokens.length],
					parentMeta.getPosition(), utils.BlockSettings.FILE_TYPE,
					System.currentTimeMillis());
			fileManager.writeFile(newMeta, content);
			return true;
		} else {
			throw new IncorrectPathException("");
		}
	}

	@Override
	public byte[] readFile(String path) throws Exception {
			MetaData meta = fileManager.search(path);
			if (isFile(meta)) {
				return fileManager.getData(meta);
			}
			throw new InvalidFileException("");
	}

	@Override
	public String[] getChildren(String path) {
		// get children bytes
		return null;
	}

	@Override
	public boolean isValidPath(String path) {
		return isValidDirectory(path) || isValidFile(path);
	}

	@Override
	public boolean isValidDirectory(String path) {
		MetaData meta;
		try {
			meta = fileManager.search(path);
			return isDirectory(meta);
		} catch (Exception e) {
			return false;
		}

	}

	private boolean isDirectory(MetaData meta) {
		return meta != null
				&& meta.getType() == utils.BlockSettings.DIRECTORY_TYPE;
	}

	@Override
	public boolean isValidFile(String path) {
		MetaData meta;
		try {
			meta = fileManager.search(path);
			return isFile(meta);
		} catch (Exception e) {
			return false;
		}

	}

	private boolean isFile(MetaData meta) {
		return meta != null && meta.getType() == utils.BlockSettings.FILE_TYPE;
	}

	@Override
	public void addNewDirectory(String path) {

	}

	@Override
	public void deleteFile(String path) throws IOException, Exception {
		MetaData fileMetaData = fileManager.search(path);
		if (fileMetaData != null)
		// Some kind of delete method inside fileManager
		{
		}
	}

	@Override
	public void deleteDirectory(String path) throws IOException, Exception {
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
			throws IOException, Exception {
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

	@Override
	public void renameFile(String oldName, String newName)
			throws IOException, Exception {
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
