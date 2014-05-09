package fileSystem;

import java.io.IOException;
import java.util.Date;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import utils.BlockSettings;
import utils.CompressionUtilities;
import utils.EncryptionUtilities;
import utils.MetaDataUtilities;
import exceptions.CannotAccessDiskException;
import exceptions.FileAlreadyExistsException;
import exceptions.FileOrDirectoryAlreadyExistsException;
import exceptions.FileOrDirectoryNotFoundException;
import exceptions.IncorrectPathException;
import exceptions.InvalidDirectoryException;
import exceptions.InvalidFileException;
import fileManager.FileManager;
import fileManager.MetaData;

public class FileSystem implements FileSystemInterface {

	private FileManager fileManager;
	private Logger logger;
	private String currentDirectory = BlockSettings.ROOT_NAME + "/";

	public FileSystem(String path) throws Exception {
		fileManager = new FileManager(path);
		logger = Logger.getLogger(FileSystem.class);
		BasicConfigurator.configure();
		this.writeRoot();
	}

	@Override
	public boolean writeFile(String path, byte[] content) throws Exception {
		content = EncryptionUtilities.encrypt(CompressionUtilities
				.compress(content));
		MetaData meta;
		try {
			meta = fileManager.search(path);
			if (isFile(meta)) {
				throw new FileAlreadyExistsException();
			}
		} catch (FileOrDirectoryNotFoundException ex) {
			logger.info("File not found, so can continue with writing");
		}

		String[] pathTokens = path.split("/");
		if (isDirectory(fileManager.search(path.substring(0, path.length()
				- pathTokens[pathTokens.length - 1].length() - 1)))) {
			MetaData metaData = new MetaData(pathTokens[pathTokens.length - 1],
					fileManager.search(
							path.substring(
									0,
									path.length()
											- pathTokens[pathTokens.length - 1]
													.length() - 1))
							.getBlockNumber(), utils.BlockSettings.FILE_TYPE,
					System.currentTimeMillis());
			MetaData newMeta = metaData;
			fileManager.createFile(newMeta, content);
			return true;
		} else {
			throw new IncorrectPathException(
					"Write File failed because of invalid path");
		}
	}

	@Override
	public byte[] readFile(String path) throws Exception {
		MetaData meta = fileManager.search(path);
		System.out.println(meta.getName());
		if (isFile(meta)) {
			return CompressionUtilities.decompress(EncryptionUtilities
					.decrypt(fileManager.getData(meta)));
		}
		throw new InvalidFileException("Could not read file");
	}

	@Override
	public String[] getChildren(String path) throws Exception {
		MetaData[] childrenMetaData = null;
		MetaData parent = fileManager.search(path);
		if (isValidDirectory(parent)) {
			childrenMetaData = fileManager.getChildrenMeta(parent);
			logger.debug(childrenMetaData[0].getName());
		}
		String[] childrenPaths = new String[childrenMetaData.length];
		for (int i = 0; i < childrenMetaData.length; i++) {
			childrenPaths[i] = childrenMetaData[i].getName();
			logger.debug("******** " + childrenPaths[i]);
		}
		return childrenPaths;
	}

	private boolean isValidDirectory(MetaData parent) {
		return isDirectory(parent);
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

	private boolean isValidFile(MetaData meta) {
		// Works on the assumption that metaData already has been retrieved.
		// Optimized to prevent 2 lookups
		return isFile(meta);
	}

	private boolean isFile(MetaData meta) {
		return meta != null && meta.getType() == utils.BlockSettings.FILE_TYPE;
	}

	@Override
	public void addNewDirectory(String directoryName, String currentPath)
			throws Exception {
		if (directoryExists(currentPath + "/" + directoryName))
			throw new FileOrDirectoryAlreadyExistsException(
					"Directory already exists");
		MetaData parent = fileManager.search(currentPath);
		MetaData newDirectoryMeta = new MetaData(directoryName,
				parent.getBlockNumber(), BlockSettings.DIRECTORY_TYPE,
				new Date().getTime());
		fileManager.createDirectory(newDirectoryMeta);
	}

	@Override
	public void deleteFile(String path) throws Exception {
		MetaData fileMetaData = fileManager.search(path);
		if (fileMetaData != null)
			fileManager.deleteFile(fileMetaData);
	}

	@Override
	public void deleteDirectory(String path) throws Exception {
		MetaData directoryMetaData = fileManager.search(path);
		if (isValidDirectory(directoryMetaData.getName())
				&& !(isRoot(directoryMetaData)))
			fileManager.deleteDirectory(directoryMetaData);
		else
			throw new InvalidDirectoryException(
					"The directory cannot be deleted");
	}

	@Override
	public long getFreeMemory() throws CannotAccessDiskException {
		try {
			return fileManager.getFreeMemory();
		} catch (IOException ex) {
			throw new CannotAccessDiskException(
					"The getFreeMemory operation failed because the disk cannot be accessed");
		}
	}

	@Override
	public long getOccupiedMemory() throws CannotAccessDiskException {
		try {
			return fileManager.getOccupiedMemory();
		} catch (IOException ex) {
			throw new CannotAccessDiskException(
					"The getOccupiedMemory operation failed because the disk cannot be accessed");
		}
	}

	@Override
	public long getTotalMemory() throws CannotAccessDiskException {
		try {
			return fileManager.getTotalMemory();
		} catch (IOException ex) {
			throw new CannotAccessDiskException(
					"The getTotalMemory operation failed because the disk cannot be accessed");
		}
	}

	@Override
	public void renameDirectory(String oldPath, String newPath)
			throws Exception {
		MetaData originalMetaData = fileManager.search(oldPath);
		if (originalMetaData.getType() == BlockSettings.DIRECTORY_TYPE
				&& !(isRoot(originalMetaData))) {
			String pathComponents[] = newPath.split("/");
			originalMetaData.setName(pathComponents[pathComponents.length - 1]);
			originalMetaData.setTimestamp(new Date().getTime());
			fileManager.rewriteMetaData(originalMetaData);
		} else
			throw new InvalidDirectoryException(
					"Rename directory failed because the directory is invalid");
	}

	private boolean isRoot(MetaData metaData) {
		return metaData.getName().equals(BlockSettings.ROOT_NAME)
				|| metaData.getBlockNumber() == BlockSettings.ROOT_POSITION;
	}

	@Override
	public void renameFile(String oldName, String newName) throws Exception {
		MetaData originalMetaData = fileManager.search(oldName);
		if (isValidFile(oldName)) {
			String[] pathComponents = newName.split("/");
			originalMetaData.setName(pathComponents[pathComponents.length - 1]);
			originalMetaData.setTimestamp(new Date().getTime());
			fileManager.rewriteMetaData(originalMetaData);
		} else
			throw new InvalidFileException(
					"Rename file failed because the file is invalid");
	}

	// Writes the root directory at position 0
	private void writeRoot() throws Exception {
		MetaData rootMetaData = new MetaData(BlockSettings.ROOT_NAME,
				BlockSettings.ROOT_PARENT, BlockSettings.ROOT_TYPE,
				BlockSettings.ROOT_TIMESTAMP);
		rootMetaData.setBlockNumber(BlockSettings.ROOT_POSITION);
		fileManager.writeRoot(rootMetaData);
	}

	private boolean directoryExists(String path) throws Exception {
		try {
			MetaData meta = fileManager.search(path);
			return true;
		} catch (FileOrDirectoryNotFoundException ex) {
			return false;
		}
	}

	@Override
	public void moveFile(String oldPath, String newPath) throws Exception {
		MetaData fileToMoveMetaData = fileManager.search(oldPath);
		if (!isValidFile(fileToMoveMetaData))
			throw new FileOrDirectoryNotFoundException(
					"Couldn't move file because it couldn't be found");
		String pathWithoutFileName = newPath.substring(0,
				newPath.lastIndexOf("/"));
		String newFileName = newPath.substring(newPath.lastIndexOf("/") + 1);
		MetaData newParentMeta = fileManager.search(pathWithoutFileName);
		if (!isValidDirectory(newParentMeta))
			throw new FileOrDirectoryNotFoundException(
					"Destination directory doesn't exist");

		// First get data of the original file
		byte[] fileContents = fileManager.getData(fileToMoveMetaData);
		fileManager.deleteFile(fileToMoveMetaData);
		fileToMoveMetaData.setParent(newParentMeta.getBlockNumber());
		fileToMoveMetaData.setName(newFileName);
		fileManager.createFile(fileToMoveMetaData, fileContents);
	}

	@Override
	public void moveDirectory(String oldPath, String newPath) throws Exception {
		MetaData directoryToMoveMetaData = fileManager.search(oldPath);
		if (!isValidDirectory(directoryToMoveMetaData)
				|| isRoot(directoryToMoveMetaData))
			throw new FileOrDirectoryNotFoundException(
					"Couldn't move directory because it couldn't be found or it is the root directory");
		String newPathWithoutFileName = newPath.substring(0,
				newPath.lastIndexOf("/"));
		String newDirectoryName = newPath
				.substring(newPath.lastIndexOf("/") + 1);
		MetaData newParentMeta = fileManager.search(newPathWithoutFileName);
		if (!isValidDirectory(newParentMeta))
			throw new FileOrDirectoryNotFoundException(
					"Destination directory doesn't exist");

		// get contents of directory
		byte[] directoryContents = fileManager.getData(directoryToMoveMetaData);
		fileManager.deleteFile(directoryToMoveMetaData);
		directoryToMoveMetaData.setParent(newParentMeta.getBlockNumber());
		directoryToMoveMetaData.setName(newDirectoryName);
		// a filled directory is a special file, so creation is the same
		fileManager.createFile(directoryToMoveMetaData, directoryContents);
	}

	public void importFile(String hostPath) throws Exception {
		byte[] fileContents = MetaDataUtilities.fileToBytes(hostPath);
		String hostPathComponents[] = hostPath.split("\\\\");
		writeFile(currentDirectory
				+ hostPathComponents[hostPathComponents.length - 1],
				fileContents);
	}

	public String getCurrentDirectory() {
		return currentDirectory;
	}

	public String getFilePath(String fileName) {
		if (fileName != null)
			return getCurrentDirectory().concat(fileName);
		return "";
	}
}