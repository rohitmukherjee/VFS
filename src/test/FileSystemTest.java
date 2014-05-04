package test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import utils.BlockSettings;
import fileManager.MetaDataUtilities;
import fileSystem.FileSystem;

public class FileSystemTest {

	static Logger logger;

	@BeforeClass
	public static void setUpBeforeClass() {
		logger = Logger.getLogger(FileSystemTest.class);
		BasicConfigurator.configure();
	}

	@AfterClass
	public static void tearDown() throws IOException {
		Runtime.getRuntime().exec(
				"cmd /c start D:\\ETH_Dev\\teambatman\\scripts\\cleanup.bat");
	}

	public void setUpFS() throws Exception {

		// fs.writeFile(utils.BlockSettings.ROOT_NAME + "/directory1/file",
		// Files.readAllBytes(Paths.get("indir1.txt")));
		// fs.writeFile(utils.BlockSettings.ROOT_NAME
		// + "/directory1/nested1/nestedfile",
		// Files.readAllBytes(Paths.get("innested1.txt")));
		// fs.writeFile(utils.BlockSettings.ROOT_NAME
		// + "/directory1/nested1/nestedfile2",
		// Files.readAllBytes(Paths.get("anotherinnested1.txt")));
		//
	}

	@Ignore
	public void rootShouldBeWrittenProperly() throws Exception {
		FileSystem fs = new FileSystem(TestUtilities.WINDOWS_PATH_1);
		assertTrue(fs.isValidDirectory(utils.BlockSettings.ROOT_NAME));
	}

	@Ignore
	public void getFreeOccupiedTotalMemoryShouldWork() throws Exception {
		FileSystem fs = new FileSystem(TestUtilities.WINDOWS_PATH_2);
		logger.info(fs.getTotalMemory());
		logger.info(fs.getOccupiedMemory());
		logger.info(fs.getFreeMemory());
	}

	@Ignore
	public void directoriesShouldBeAddedAndRetrievedCorrectly()
			throws Exception {
		FileSystem fs = new FileSystem(TestUtilities.WINDOWS_PATH_3);

		// Check for non - existent directory
		assertFalse(fs.isValidDirectory(utils.BlockSettings.ROOT_NAME
				+ "/adirectory"));

		// Creating directories under root
		fs.addNewDirectory("directory1", utils.BlockSettings.ROOT_NAME);
		fs.addNewDirectory("directory2", utils.BlockSettings.ROOT_NAME);
		assertTrue(fs.isValidDirectory(utils.BlockSettings.ROOT_NAME
				+ "/directory1"));
		assertTrue(fs.isValidDirectory(utils.BlockSettings.ROOT_NAME
				+ "/directory2"));

		// add new directory inside another
		fs.addNewDirectory("nested1", utils.BlockSettings.ROOT_NAME
				+ "/directory1");
		fs.addNewDirectory("nested2", utils.BlockSettings.ROOT_NAME
				+ "/directory1");
		fs.addNewDirectory("supernested1", utils.BlockSettings.ROOT_NAME
				+ "/directory1/nested1");
		fs.addNewDirectory("supernested2", utils.BlockSettings.ROOT_NAME
				+ "/directory1/nested1");
		assertTrue(fs.isValidDirectory(utils.BlockSettings.ROOT_NAME
				+ "/directory1/nested1"));
		assertTrue(fs.isValidDirectory(utils.BlockSettings.ROOT_NAME
				+ "/directory1/nested2"));
		assertTrue(fs.isValidDirectory(utils.BlockSettings.ROOT_NAME
				+ "/directory1/nested1/supernested1"));
		assertTrue(fs.isValidDirectory(utils.BlockSettings.ROOT_NAME
				+ "/directory1/nested1/supernested2"));

	}

	// @Test(expected = FileOrDirectoryAlreadyExistsException.class)
	public void tryingToAddAnExistingDirectoryShouldThrowException()
			throws Exception {
		FileSystem fs = new FileSystem(TestUtilities.WINDOWS_PATH_4);
		fs.addNewDirectory("directory1", utils.BlockSettings.ROOT_NAME);
		fs.addNewDirectory("directory1", utils.BlockSettings.ROOT_NAME);
	}

	@Test
	public void tryingToWriteAFileToRoot() throws Exception {
		FileSystem fs = new FileSystem(TestUtilities.WINDOWS_PATH_5);
		byte[] fileToWrite = Files.readAllBytes(Paths.get("root.txt"));
		fs.writeFile(utils.BlockSettings.ROOT_NAME + "/fileAtRoot", fileToWrite);
		byte retrievedFile[] = fs.readFile(BlockSettings.ROOT_NAME
				+ "/fileAtRoot");
		MetaDataUtilities.bytesToFile(retrievedFile,
				"D:\\Pictures\\root_retr.txt");
		// length of byte arrays stored and retrieved should be equal
		assertEquals(fileToWrite.length, retrievedFile.length);
		assertArrayEquals(fileToWrite, retrievedFile);
	}
}
