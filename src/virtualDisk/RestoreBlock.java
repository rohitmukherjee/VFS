package virtualDisk;

/**
 * Utility class for restoring offset position in underlying random acess file
 * When the getNextBlock() method of BlockManager is called, this utility class
 * is used to reset back to the previous position.
 * 
 * @author ROHIT
 * 
 */
public class RestoreBlock {

	private long filePointer;
	private Block currentBlock;

	protected RestoreBlock(BlockManager blockManager) {
		try {
			filePointer = blockManager.getVirtualDisk().getFilePosition();
			currentBlock = blockManager.getCurrentBlock();
		} catch (Exception ex) {
			// TODO: Have to figure out something better to do here
			ex.printStackTrace();
		}
	}

	protected void restore(BlockManager blockManager) {
		try {
			blockManager.getVirtualDisk().seek(filePointer);
		} catch (Exception ex) {
			// TODO: Have to figure out something better to do here
			ex.printStackTrace();
		}
	}
}
