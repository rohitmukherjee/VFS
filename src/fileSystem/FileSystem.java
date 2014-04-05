package filesystem;

import java.io.File;

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
		// Writes the data from the byte[] content to path
		return false;
	}

	@Override
	public byte[] readFile(String path) {
		// TODO Auto-generated method stub
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
		File f = new File(path);
		f.getAbsolutePath();
	}

	@Override
	public void deleteDirectory(String path) {
		// TODO Auto-generated method stub

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
	public void renameDirectory(String oldName, String newName) {
		// TODO Auto-generated method stub
	}

	@Override
	public void renameFile(String oldName, String newName) {
		// TODO Auto-generated method stub
	}

	// Writes the root directory at position 0
	private void writeRoot() throws Exception {
		/*
		 * TODO: Have to figure out who should convert real meta data in the
		 * form of / String, long etc. to byte arrays. After writing root, we
		 * should forcefully allocate it some data space because our current
		 * design doesn't allow us to grow files/folders. If we allocated some
		 * space to it to store longs of sub - directories/files, it is easy to
		 * add entries to it later on.
		 */
		MetaData rootMetaData = new MetaData(null);
		fileManager.writeFile(0, rootMetaData,
				new byte[(int) (utils.BlockSettings.ROOT_SUPERBLOCK_SIZE)]);
	}
}
