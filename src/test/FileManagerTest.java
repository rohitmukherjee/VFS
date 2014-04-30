package test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import utils.BlockSettings;
import exceptions.FileOrDirectoryNotFoundException;
import fileManager.FileManager;
import fileManager.MetaData;
import fileManager.MetaDataUtilities;

public class FileManagerTest {

	static Logger logger;
	private static MetaData rootMetaData = new MetaData(
			BlockSettings.ROOT_NAME, BlockSettings.ROOT_PARENT,
			BlockSettings.ROOT_TYPE, BlockSettings.ROOT_TIMESTAMP);

	@After
	public void cleanUp() {
		File f = new File(TestUtilities.WINDOWS_PATH_1);
		f.delete();
	}

	@BeforeClass
	public static void setUp() {
		logger = Logger.getLogger(FileManagerTest.class);
		BasicConfigurator.configure();
		rootMetaData.setParent(BlockSettings.ROOT_POSITION);
	}

	/**
	 * Add some directories and files to the system so we can test different
	 * things Call this method before most of tests so we dont have to add stuff
	 * every time
	 * 
	 * @throws Exception
	 */
	public static FileManager setUpDisk() throws Exception {
		FileManager fm = new FileManager(TestUtilities.POSIX_PATH);
		fm.writeRoot(rootMetaData);
		MetaData meta1 = new MetaData("dir1", 0,
				utils.BlockSettings.DIRECTORY_TYPE, System.currentTimeMillis());
		fm.createDirectory(meta1);
		MetaData meta2 = new MetaData("nestedDir1", meta1.getBlockNumber(),
				utils.BlockSettings.DIRECTORY_TYPE, System.currentTimeMillis());
		fm.createDirectory(meta2);
		MetaData meta3 = new MetaData("dir2", 0,
				utils.BlockSettings.DIRECTORY_TYPE, System.currentTimeMillis());
		fm.createDirectory(meta3);
		MetaData metaFile1 = new MetaData("file1", meta1.getBlockNumber(),
				utils.BlockSettings.FILE_TYPE, System.currentTimeMillis());
		fm.createFile(metaFile1, Files.readAllBytes(Paths.get("indir1.txt")));
		MetaData metaFile2 = new MetaData("nestedfile1",
				meta2.getBlockNumber(), utils.BlockSettings.FILE_TYPE,
				System.currentTimeMillis());
		fm.createFile(metaFile2, Files.readAllBytes(Paths.get("innested1.txt")));
		MetaData metaFile3 = new MetaData("file2", meta1.getBlockNumber(),
				utils.BlockSettings.FILE_TYPE, System.currentTimeMillis());
		fm.createFile(metaFile3, Files.readAllBytes(Paths.get("indir1.txt")));

		return fm;
	}

	@After
	public void tearDown() {
		File f = new File(TestUtilities.POSIX_PATH);
		f.delete();
	}

	@Ignore
	public void getFreeOccupiedTotalMemoryShouldWork() throws Exception {
		FileManager fileManager = new FileManager(TestUtilities.POSIX_PATH);
		fileManager.writeRoot(rootMetaData);
		logger.info(fileManager.getTotalMemory());
		logger.info(fileManager.getOccupiedMemory());
		logger.info(fileManager.getFreeMemory());
	}

	@Ignore
	public void testDeleteDirectory() throws Exception {
		FileManager fm = setUpDisk();

		// try deleting the root
		try {
			fm.deleteDirectory(rootMetaData);
			logger.error("Allowed deleting of the root. Should throw somewhere or not allow");
		} catch (Exception e) {

		}

		// delete a nonexistant directory
		try {
			MetaData fakeMeta = new MetaData("fakeDir", 0,
					utils.BlockSettings.DIRECTORY_TYPE,
					System.currentTimeMillis());
			fm.deleteDirectory(fakeMeta);
			logger.error("Allowed deleting of a nonexistant directory without throwing");
		} catch (Exception e) {

		}

		// delete an empty directory
		MetaData meta = fm.search(BlockSettings.ROOT_NAME + "/dir2");
		fm.deleteDirectory(meta);
		assertEquals(null, fm.search(BlockSettings.ROOT_NAME + "/dir2"));

	}

	@Test(expected = FileOrDirectoryNotFoundException.class)
	public void testDeleteDirectoryWithFile() throws Exception {
		FileManager fileManager = new FileManager(TestUtilities.WINDOWS_PATH_1);
		fileManager.writeRoot(rootMetaData);
		MetaData meta1 = new MetaData("dir1", 0,
				utils.BlockSettings.DIRECTORY_TYPE, System.currentTimeMillis());
		fileManager.createDirectory(meta1);
		MetaData retrievedDir1 = fileManager.search("root/dir1");
		MetaData meta2 = new MetaData("nestedDir1",
				retrievedDir1.getBlockNumber(),
				utils.BlockSettings.DIRECTORY_TYPE, System.currentTimeMillis());
		fileManager.createDirectory(meta2);
		MetaData retrievedNestedDir1 = fileManager
				.search("root/dir1/nestedDir1");
		MetaData metaFile2 = new MetaData("nestedfile1",
				retrievedNestedDir1.getBlockNumber(),
				utils.BlockSettings.FILE_TYPE, System.currentTimeMillis());
		fileManager.createFile(metaFile2,
				Files.readAllBytes(Paths.get("innested1.txt")));

		fileManager.deleteDirectory(meta2);
		retrievedNestedDir1 = fileManager.search(BlockSettings.ROOT_NAME
				+ "/dir1/nestedDir1");
		assertTrue(fileManager.search(BlockSettings.ROOT_NAME + "/dir1") != null);
		assertEquals(
				null,
				fileManager.search(BlockSettings.ROOT_NAME
						+ "/dir1/nestedDir1/nestedFile1"));

	}

	@Ignore
	public void testDeleteMetaDirectory() throws Exception {
		FileManager fm = setUpDisk();
		MetaData meta = fm.search(BlockSettings.ROOT_NAME + "/dir1");
		fm.deleteDirectory(meta);
		assertEquals(null,
				fm.search(BlockSettings.ROOT_NAME + "/dir1/nestedDir1"));
		assertEquals(
				null,
				fm.search(BlockSettings.ROOT_NAME
						+ "/dir1/nestedDir1/nestedFile1"));
		assertEquals(null, fm.search(BlockSettings.ROOT_NAME + "/dir1/file1"));
		assertEquals(null, fm.search(BlockSettings.ROOT_NAME + "/dir1/file2"));
		assertEquals(null, fm.search(BlockSettings.ROOT_NAME + "/dir1"));
	}

	@Ignore
	public void searchingForFilesAndFolders() throws Exception {
		FileManager fileManager = new FileManager(TestUtilities.WINDOWS_PATH_1);
		fileManager.writeRoot(rootMetaData);
		MetaData meta1 = new MetaData("dir1", 0,
				utils.BlockSettings.DIRECTORY_TYPE, System.currentTimeMillis());
		fileManager.createDirectory(meta1);
		MetaData meta3 = new MetaData("dir2", 0,
				utils.BlockSettings.DIRECTORY_TYPE, System.currentTimeMillis());
		fileManager.createDirectory(meta3);
		MetaData retrievedDir1 = fileManager.search("root/dir1");
		MetaData meta2 = new MetaData("nestedDir1",
				retrievedDir1.getBlockNumber(),
				utils.BlockSettings.DIRECTORY_TYPE, System.currentTimeMillis());
		fileManager.createDirectory(meta2);

		MetaData metaFile1 = new MetaData("file1",
				retrievedDir1.getBlockNumber(), utils.BlockSettings.FILE_TYPE,
				System.currentTimeMillis());
		fileManager.createFile(metaFile1,
				Files.readAllBytes(Paths.get("indir1.txt")));
		MetaData retrievedNestedDir1 = fileManager
				.search("root/dir1/nestedDir1");
		MetaData metaFile2 = new MetaData("nestedfile1",
				retrievedNestedDir1.getBlockNumber(),
				utils.BlockSettings.FILE_TYPE, System.currentTimeMillis());
		fileManager.createFile(metaFile2,
				Files.readAllBytes(Paths.get("innested1.txt")));
		MetaData metaFile3 = new MetaData("file2",
				retrievedDir1.getBlockNumber(), utils.BlockSettings.FILE_TYPE,
				System.currentTimeMillis());
		fileManager.createFile(metaFile3,
				Files.readAllBytes(Paths.get("indir1.txt")));

		// search for nonexistant file
		try {
			MetaData meta = fileManager.search(BlockSettings.ROOT_NAME
					+ "/dir2/file1");
		} catch (FileOrDirectoryNotFoundException ex) {
			System.out.println("Caught exception");
		}

		// search for nonexistant directory
		try {
			MetaData meta = fileManager.search(BlockSettings.ROOT_NAME
					+ "/dir2/nestedDir1");
		} catch (FileOrDirectoryNotFoundException ex) {
			System.out.println("Caught exception");
		}

		// search for partially correct path name
		try {
			MetaData meta = fileManager.search(BlockSettings.ROOT_NAME
					+ "/dir1/nestedDir1/nestedfile2");
		} catch (FileOrDirectoryNotFoundException ex) {
			System.out.println("Caught exception");
		}

		// search for valid directory
		assertEquals("dir1", retrievedDir1.getName());
		assertEquals(rootMetaData.getBlockNumber(), retrievedDir1.getParent());

		// search for the root
		MetaData retrievedRoot = fileManager.search(BlockSettings.ROOT_NAME);
		assertEquals(rootMetaData.getName(), retrievedRoot.getName());
		assertArrayEquals(rootMetaData.getBytes(), retrievedRoot.getBytes());

		// search for valid file
		MetaData retrievedFile1 = fileManager.search(BlockSettings.ROOT_NAME
				+ "/dir1/file1");
		assertArrayEquals(metaFile1.getBytes(), retrievedFile1.getBytes());

		// search for nested file1
		MetaData retrievedNestedFile1 = fileManager
				.search(BlockSettings.ROOT_NAME
						+ "/dir1/nestedDir1/nestedfile1");
		assertArrayEquals(metaFile2.getBytes(), retrievedNestedFile1.getBytes());

	}

	@Ignore
	public void checkIfRootIsProperlyWritten() throws Exception {
		FileManager fileManager = new FileManager(TestUtilities.POSIX_PATH);
		fileManager.writeRoot(rootMetaData);
		long zero = 0;
		MetaData rootMetaReadFromDisk = fileManager.getMetaData(zero);
		assertEquals(BlockSettings.ROOT_NAME, rootMetaReadFromDisk.getName());
		assertEquals(BlockSettings.ROOT_PARENT,
				rootMetaReadFromDisk.getParent());
		assertEquals(BlockSettings.ROOT_TIMESTAMP,
				rootMetaReadFromDisk.getTimestamp());
		assertEquals(BlockSettings.ROOT_POSITION,
				rootMetaReadFromDisk.getBlockNumber());
		assertEquals((int) BlockSettings.ROOT_TYPE,
				(int) rootMetaReadFromDisk.getType());
	}

	@Ignore
	public void writingAFileAfterRootShouldBeReadCorrectly() throws Exception {
		FileManager fileManager = new FileManager(TestUtilities.WINDOWS_PATH_1);
		fileManager.writeRoot(rootMetaData);
		MetaData fileMeta = new MetaData("test.c", 0, (byte) 1,
				new Date().getTime());
		byte[] fileData = MetaDataUtilities
				.fileToBytes(TestUtilities.WINDOWS_FILE_TEST);
		fileManager.createFile(fileMeta, fileData);
		MetaData retrieved = fileManager
				.search(TestUtilities.WINDOWS_FILE_TEST);
		/* This bit checks the Meta Data retrieval */
		assertEquals(fileMeta.getBlockNumber(), retrieved.getBlockNumber());
		assertEquals(fileMeta.getType(), retrieved.getType());
		assertEquals(fileMeta.getName(), retrieved.getName());
		assertEquals(fileMeta.getTimestamp(), retrieved.getTimestamp());
		assertEquals(fileMeta.getParent(), retrieved.getParent());
		assertArrayEquals(fileMeta.getBytes(), retrieved.getBytes());
	}

	@Ignore
	public void writingDirectoriesFilesAndNestedDirectoriesShouldBeRetrievedCorrectly()
			throws Exception {
		FileManager fileManager = new FileManager(TestUtilities.WINDOWS_PATH_1);
		fileManager.writeRoot(rootMetaData);
		MetaData meta1 = new MetaData("dir1", 0,
				utils.BlockSettings.DIRECTORY_TYPE, System.currentTimeMillis());
		fileManager.createDirectory(meta1);
		MetaData retrieved1 = fileManager.search("root/dir1");
		// creating a nested directory
		MetaData meta2 = new MetaData("nestedDir1",
				retrieved1.getBlockNumber(),
				utils.BlockSettings.DIRECTORY_TYPE, System.currentTimeMillis());
		fileManager.createDirectory(meta2);
		// creating another directory at the same level as nestedDir1
		MetaData meta3 = new MetaData("dir2", 0,
				utils.BlockSettings.DIRECTORY_TYPE, System.currentTimeMillis());
		fileManager.createDirectory(meta3);
		MetaData retrieved3 = fileManager.search("root/dir2");
		MetaData retrieved2 = fileManager.search("root/dir1/nestedDir1");
		// checking dir1
		assertEquals(meta1.getName(), retrieved1.getName());
		assertEquals(meta1.getType(), retrieved1.getType());
		assertEquals(meta1.getName(), retrieved1.getName());
		assertEquals(meta1.getTimestamp(), retrieved1.getTimestamp());
		assertEquals(meta1.getParent(), retrieved1.getParent());
		assertArrayEquals(meta1.getBytes(), retrieved1.getBytes());
		// checking dir2
		assertEquals(meta3.getName(), retrieved3.getName());
		assertEquals(meta3.getType(), retrieved3.getType());
		assertEquals(meta3.getName(), retrieved3.getName());
		assertEquals(meta3.getTimestamp(), retrieved3.getTimestamp());
		assertEquals(meta3.getParent(), retrieved3.getParent());
		assertArrayEquals(meta1.getBytes(), retrieved1.getBytes());
		// checking nestedDir1
		assertEquals(meta2.getName(), retrieved2.getName());
		assertEquals(meta2.getType(), retrieved2.getType());
		assertEquals(meta2.getName(), retrieved2.getName());
		assertEquals(meta2.getTimestamp(), retrieved2.getTimestamp());
		assertEquals(meta2.getParent(), retrieved2.getParent());
		assertArrayEquals(meta2.getBytes(), retrieved2.getBytes());
		// Trying to create a file inside a directory should fail
		MetaData metaFile = new MetaData("file1", retrieved1.getBlockNumber(),
				utils.BlockSettings.FILE_TYPE, System.currentTimeMillis());
		fileManager.createFile(metaFile,
				Files.readAllBytes(Paths.get("indir1.txt")));
		MetaData retrievedFile = fileManager.search("root/dir1/file1");
		assertEquals(metaFile.getName(), retrievedFile.getName());
		MetaData meta4 = new MetaData("fail", retrievedFile.getBlockNumber(),
				utils.BlockSettings.DIRECTORY_TYPE, System.currentTimeMillis());
		try {
			fileManager.createDirectory(meta4);
			logger.error("Created a directory in a file. Should throw");
		} catch (Exception e) {
			System.out.println("It works ;)");
		}

	}
}
