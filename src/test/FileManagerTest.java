package test;


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
		File f = new File(TestUtilities.WINDOWS_PATH);
		f.delete();
	}

	@BeforeClass
	public static void setUp() {
		logger = Logger.getLogger(FileManagerTest.class);
		BasicConfigurator.configure();
		rootMetaData.setParent(BlockSettings.ROOT_POSITION);
	}
	/**
	 * Add some directories and files to the system so we can test different things
	 * Call this method before most of tests so we dont have to add stuff every time
	 * @throws Exception 
	 */
	public static FileManager setUpDisk() throws Exception {
		FileManager fm = new FileManager(TestUtilities.POSIX_PATH);
		fm.writeRoot(rootMetaData);
		MetaData meta1 = new MetaData("dir1", 0,
				utils.BlockSettings.DIRECTORY_TYPE, System.currentTimeMillis());
		fm.createDirectory(meta1);
		MetaData meta2 = new MetaData("nestedDir1", meta1.getPosition(),
				utils.BlockSettings.DIRECTORY_TYPE, System.currentTimeMillis());
		fm.createDirectory(meta2);
		MetaData meta3 = new MetaData("dir2", 0,
				utils.BlockSettings.DIRECTORY_TYPE, System.currentTimeMillis());
		fm.createDirectory(meta3);
		MetaData metaFile1 = new MetaData("file1", meta1.getPosition(),
				utils.BlockSettings.FILE_TYPE, System.currentTimeMillis());
		fm.createFile(metaFile1, Files.readAllBytes(Paths.get("indir1.txt")));
		MetaData metaFile2 = new MetaData("nestedfile1", meta2.getPosition(),
				utils.BlockSettings.FILE_TYPE, System.currentTimeMillis());
		fm.createFile(metaFile2, Files.readAllBytes(Paths.get("innested1.txt")));
		MetaData metaFile3 = new MetaData("file2", meta1.getPosition(),
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
	public void testCreateDirectory() throws Exception {
		FileManager fm = new FileManager(TestUtilities.POSIX_PATH);
		fm.writeRoot(rootMetaData);

		// test that a directory not already present
		assertEquals(null, fm.search("root/dir1"));

		// create a directory in root
		MetaData meta1 = new MetaData("dir1", 0,
				utils.BlockSettings.DIRECTORY_TYPE, System.currentTimeMillis());
		fm.createDirectory(meta1);
		assertEquals(meta1, fm.search(BlockSettings.ROOT_NAME+"/dir1"));

		// create a directory inside another directory
		MetaData meta2 = new MetaData("nestedDir1", meta1.getPosition(),
				utils.BlockSettings.DIRECTORY_TYPE, System.currentTimeMillis());
		fm.createDirectory(meta2);
		assertEquals(meta1, fm.search(BlockSettings.ROOT_NAME+"/dir1"));
		assertEquals(meta2, fm.search(BlockSettings.ROOT_NAME+"/dir1/nestedDir1"));

		// create 2 directories on the same level
		MetaData meta3 = new MetaData("dir2", 0,
				utils.BlockSettings.DIRECTORY_TYPE, System.currentTimeMillis());
		fm.createDirectory(meta3);
		assertEquals(meta1, fm.search(BlockSettings.ROOT_NAME+"/dir1"));
		assertEquals(meta3, fm.search(BlockSettings.ROOT_NAME+"/dir2"));

		// try to create a directory inside a file
		MetaData metaFile = new MetaData("file1", meta1.getPosition(),
				utils.BlockSettings.FILE_TYPE, System.currentTimeMillis());
		fm.createFile(metaFile, Files.readAllBytes(Paths.get("indir1.txt")));
		MetaData meta4 = new MetaData("fail", metaFile.getPosition(),
				utils.BlockSettings.DIRECTORY_TYPE, System.currentTimeMillis());
		try {
			fm.createDirectory(meta4);
			logger.error("Created a directory in a file. Should throw");
		} catch (Exception e) {

		}

	}
	
	public void testDeleteDirectory() throws Exception {
		FileManager fm = setUpDisk();
		
		//try deleting the root
		try {
			fm.deleteRecursively(rootMetaData);
			logger.error("Allowed deleting of the root. Should throw somewhere or not allow");
		}catch (Exception e) {
			
		}
		
		//delete a nonexistant directory
		try {
			MetaData fakeMeta = new MetaData("fakeDir", 0,
					utils.BlockSettings.DIRECTORY_TYPE, System.currentTimeMillis());
			fm.deleteRecursively(fakeMeta);
			logger.error("Allowed deleting of a nonexistant directory without throwing");
		}catch (Exception e) {
			
		}
		
		//delete an empty directory
		MetaData meta = fm.search(BlockSettings.ROOT_NAME+"/dir2");
		fm.deleteRecursively(meta);
		assertEquals(null, fm.search(BlockSettings.ROOT_NAME+"/dir2"));
		
	}
	
	public void testDeleteDirectoryWithFile() throws Exception {
		FileManager fm = setUpDisk();
		MetaData meta = fm.search(BlockSettings.ROOT_NAME+"/dir1/nestedDir1");
		fm.deleteRecursively(meta);
		assertEquals(null, fm.search(BlockSettings.ROOT_NAME+"/dir1/nestedDir1"));
		assertEquals(null, fm.search(BlockSettings.ROOT_NAME+"/dir1/nestedDir1/nestedFile1"));
		assertTrue(fm.search(BlockSettings.ROOT_NAME+"/dir1") != null);
	}
	
	public void testDeleteMetaDirectory() throws Exception {
		FileManager fm = setUpDisk();
		MetaData meta = fm.search(BlockSettings.ROOT_NAME+"/dir1");
		fm.deleteRecursively(meta);
		assertEquals(null, fm.search(BlockSettings.ROOT_NAME+"/dir1/nestedDir1"));
		assertEquals(null, fm.search(BlockSettings.ROOT_NAME+"/dir1/nestedDir1/nestedFile1"));
		assertEquals(null, fm.search(BlockSettings.ROOT_NAME+"/dir1/file1"));
		assertEquals(null, fm.search(BlockSettings.ROOT_NAME+"/dir1/file2"));
		assertEquals(null, fm.search(BlockSettings.ROOT_NAME+"/dir1"));
	}
	
	public void testSearch() throws Exception {
		FileManager fm = setUpDisk();
		
		//search for nonexistant file
		//search for nonexistant directory
		//search for partially correct path name
		//search for valid directory
		//search for valid file
		
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
				rootMetaReadFromDisk.getPosition());
		assertEquals((int) BlockSettings.ROOT_TYPE,
				(int) rootMetaReadFromDisk.getType());
	}

	@Test
	public void writingABigFileAfterRootShouldBeReadCorrectly()
			throws Exception {
		FileManager fileManager = new FileManager(TestUtilities.POSIX_PATH);
		fileManager.writeRoot(rootMetaData);
		MetaData[] children = fileManager.getChildrenMeta(rootMetaData);
		MetaData fileMeta = new MetaData("test.c", 0, (byte) 1,
				new Date().getTime());
		byte[] fileData = MetaDataUtilities
				.fileToBytes(TestUtilities.BADASH_FILE_TEST);
		fileManager.createFile(fileMeta, fileData);
		MetaData rootMeta = fileManager.getMetaData(0);
		children = fileManager.getChildrenMeta(rootMeta);
	    for(int i = 0; i < children.length; ++i) {logger.warn(children[i].getName());}
//		MetaData retrieved = fileManager.search("root/test.c");
//		assertEquals(fileMeta.getName(), retrieved.getName());
	}
}
