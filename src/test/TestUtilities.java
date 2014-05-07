package test;

import utils.BlockSettings;

public class TestUtilities {

	public final static String WINDOWS_PATH_1 = "testFiles/test.vdisk";
	public final static String WINDOWS_PATH_2 = "testFiles/test2.vdisk";
	public final static String WINDOWS_PATH_3 = "testFiles/test3.vdisk";
	public final static String WINDOWS_PATH_4 = "testFiles/test4.vdisk";
	public final static String WINDOWS_PATH_5 = "testFiles/test5.vdisk";
	public final static String WINDOWS_PATH_6 = "testFiles/test6.vdisk";
	public static final String WINDOWS_PATH_7 = "testFiles/test7.vdisk";
	public static final String WINDOWS_PATH_8 = "testFiles/test8.vdisk";
	public static final String WINDOWS_PATH_9 = "testFiles/test9.vdisk";
	public static final String WINDOWS_PATH_10 = "testFiles/test10.vdisk";
	public static final String WINDOWS_PATH_11 = "testFiles/test11.vdisk";
	public static final String WINDOWS_PATH_12 = "testFiles/test12.vdisk";
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
