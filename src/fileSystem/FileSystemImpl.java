package fileSystem;

public class FileSystemImpl implements FileSystemInterface {

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
		// TODO Auto-generated method stub

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
}
