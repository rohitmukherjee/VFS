package test;

public class TestUtilities {

	public static final byte[] testBlockDataLessThan = { 1, 0, 1, 0, 1, 0, 1,
			0, 1, 0 };
	public static final byte[] testBlockDataFullBlock = { 0, 1, 0, 1, 0, 1, 0,
			1, 0, 1, 0, 1, 0, 1, 0, 1 };

	public static byte[] bigData = new byte[1000];

	public static void bigDataSetup() {
		for (int i = 0; i < 1000; i++) {
			bigData[i] = (byte) (i % 2);
		}
	}
}
