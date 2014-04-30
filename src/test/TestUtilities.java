package test;

import utils.BlockSettings;

public class TestUtilities {

	public final static String WINDOWS_PATH_1 = "D:/test.vdisk";
	public final static String WINDOWS_PATH_2 = "D:/test2.vdisk";
	public final static String WINDOWS_PATH_3 = "D:/test3.vdisk";
	public final static String WINDOWS_PATH_4 = "D:/test4.vdisk";
	public final static String WINDOWS_PATH_5 = "D:/test5.vdisk";
	public final static String POSIX_PATH = "~\\Desktop\\test.vdisk";
	public static final byte[] testBlockDataLessThan = { 1, 0, 1, 0, 1, 0, 1,
			0, 1, 0 };
	public static final byte[] testBlockDataFullBlock = { 0, 1, 0, 1, 0, 1, 0,
			1, 0, 1, 0, 1, 0, 1, 0, 1 };

	public static final byte[] testBlockData2Blocks = new byte[2 * (int) BlockSettings.DATA_LENGTH];
	public static final String WINDOWS_FILE_TEST = "D:/test.c";
	public static final String WINDOWS_OUTPUT_DIR = "D:/test.c";

	public static final String BADASH_FILE_TEST = "/Users/jessebadash/Desktop/AOL_example.txt";
	public static final String BADASH_OUTPUT_DIR = "~\\Desktop";

	public static byte[] bigData = new byte[1000];

	public static void bigDataSetup() {
		for (int i = 0; i < 1000; i++) {
			bigData[i] = (byte) (i % 2);
		}
	}

	public static void twoBlockSetup() {
		for (int i = 0; i < testBlockData2Blocks.length; ++i) {
			testBlockData2Blocks[i] = (byte) (i % 2);
		}
	}
}
