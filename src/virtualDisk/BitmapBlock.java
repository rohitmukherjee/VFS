package virtualDisk;

public class BitmapBlock extends Block {

	private byte[] bitmap;

	public BitmapBlock(long offset) {
		super(offset);
		bitmap = new byte[MAXIMUM_BLOCK_SIZE];
		initializeBitmap();
	}

	private void initializeBitmap() {
		for (int i = 0; i < MAXIMUM_BLOCK_SIZE; i++)
			bitmap[i] = 0;
	}

	public byte[] getBitmap() {
		return bitmap;
	}
}
