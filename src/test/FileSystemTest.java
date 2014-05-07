package test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import utils.BlockSettings;
import utils.MetaDataUtilities;
import exceptions.FileOrDirectoryAlreadyExistsException;
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
		Runtime.getRuntime().exec("cmd /c start scripts\\cleanup.bat");
	}

	@Test
	public void rootShouldBeWrittenProperly() throws Exception {
		FileSystem fs = new FileSystem(TestUtilities.WINDOWS_PATH_1);
		assertTrue(fs.isValidDirectory(utils.BlockSettings.ROOT_NAME));
	}

	@Test
	public void getFreeOccupiedTotalMemoryShouldWork() throws Exception {
		FileSystem fs = new FileSystem(TestUtilities.WINDOWS_PATH_2);
		logger.info(fs.getTotalMemory());
		logger.info(fs.getOccupiedMemory());
		logger.info(fs.getFreeMemory());
	}

	@Test
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

	@Test(expected = FileOrDirectoryAlreadyExistsException.class)
	public void tryingToAddAnExistingDirectoryShouldThrowException()
			throws Exception {
		FileSystem fs = new FileSystem(TestUtilities.WINDOWS_PATH_4);
		fs.addNewDirectory("directory1", utils.BlockSettings.ROOT_NAME);
		fs.addNewDirectory("directory1", utils.BlockSettings.ROOT_NAME);
	}

	@Test
	public void tryingToWriteAFileToRoot() throws Exception {
		FileSystem fs = new FileSystem(TestUtilities.WINDOWS_PATH_5);
		byte[] rootFileToWrite = MetaDataUtilities
				.fileToBytes("testFiles/root.txt");
		fs.writeFile(utils.BlockSettings.ROOT_NAME + "/fileAtRoot",
				rootFileToWrite);
		byte[] directoryFileToWrite = MetaDataUtilities
				.fileToBytes("testFiles/indir1.txt");
		byte[] nestedFileToWrite = MetaDataUtilities
				.fileToBytes("testFiles/innested1.txt");
		byte[] nestedFileToWrite2 = MetaDataUtilities
				.fileToBytes("testFiles/anotherinnested1.txt");
		// Adding directories
		fs.addNewDirectory("directory1", utils.BlockSettings.ROOT_NAME);
		fs.addNewDirectory("nested1", utils.BlockSettings.ROOT_NAME
				+ "/directory1");

		// Writing Files
		fs.writeFile(utils.BlockSettings.ROOT_NAME + "/directory1/file",
				directoryFileToWrite);
		fs.writeFile(utils.BlockSettings.ROOT_NAME
				+ "/directory1/nested1/nestedfile", nestedFileToWrite);
		fs.writeFile(utils.BlockSettings.ROOT_NAME
				+ "/directory1/nested1/nestedfile2", nestedFileToWrite2);

		byte retrievedFile[] = fs.readFile(BlockSettings.ROOT_NAME
				+ "/fileAtRoot");
		byte retrievedDirectoryFile[] = fs.readFile(BlockSettings.ROOT_NAME
				+ "/directory1/file");
		MetaDataUtilities.bytesToFile(retrievedFile,
				"D:\\Pictures\\root_retr.txt");
		MetaDataUtilities
				.bytesToFile(rootFileToWrite, "D:\\Pictures\\root.txt");
		// length of byte arrays stored and retrieved should be equal
		assertEquals(rootFileToWrite.length, retrievedFile.length);
		assertArrayEquals(rootFileToWrite, retrievedFile);
		assertEquals(directoryFileToWrite.length, retrievedDirectoryFile.length);
		assertArrayEquals(directoryFileToWrite, retrievedDirectoryFile);
	}

	@Test
	public void createAFileAndDeleteShouldWork() throws Exception {
		FileSystem fs = new FileSystem(TestUtilities.WINDOWS_PATH_6);
		byte[] rootFileToWrite = MetaDataUtilities
				.fileToBytes("testFiles/root.txt");
		fs.writeFile(utils.BlockSettings.ROOT_NAME + "/fileAtRoot",
				rootFileToWrite);
		fs.deleteFile(utils.BlockSettings.ROOT_NAME + "/fileAtRoot");
		// Should allow us to write the same file again
		fs.writeFile(utils.BlockSettings.ROOT_NAME + "/fileAtRoot",
				rootFileToWrite);
	}

	@Test
	public void createAFileAndDeleteDirectoryContainingItShouldWork()
			throws Exception {
		FileSystem fs = new FileSystem(TestUtilities.WINDOWS_PATH_7);
		byte[] rootFileToWrite = MetaDataUtilities
				.fileToBytes("testFiles/root.txt");
		fs.addNewDirectory("directory1", utils.BlockSettings.ROOT_NAME);
		fs.addNewDirectory("nested1", utils.BlockSettings.ROOT_NAME
				+ "/directory1");
		fs.writeFile(utils.BlockSettings.ROOT_NAME
				+ "/directory1/nested1/fileAtRoot", rootFileToWrite);
		fs.deleteDirectory(utils.BlockSettings.ROOT_NAME
				+ "/directory1/nested1");
		// Should allow us to write the same file again
		fs.writeFile(utils.BlockSettings.ROOT_NAME + "/fileAtRoot",
				rootFileToWrite);
	}

	@Test
	public void renameDirectoryShouldWork() throws Exception {
		FileSystem fs = new FileSystem(TestUtilities.WINDOWS_PATH_8);
		fs.addNewDirectory("directory1", utils.BlockSettings.ROOT_NAME);
		byte[] directoryFileToWrite = MetaDataUtilities
				.fileToBytes("testFiles/indir1.txt");
		fs.writeFile(utils.BlockSettings.ROOT_NAME + "/directory1/file",
				directoryFileToWrite);
		fs.renameDirectory(BlockSettings.ROOT_NAME + "/directory1",
				BlockSettings.ROOT_NAME + "/directory2");
		assertFalse(fs
				.isValidDirectory(BlockSettings.ROOT_NAME + "/directory1"));
		assertTrue(fs.isValidDirectory(BlockSettings.ROOT_NAME + "/directory2"));
		assertTrue(fs.isValidFile(BlockSettings.ROOT_NAME + "/directory2/file"));
	}

	@Test
	public void renameFileShouldWork() {

	}

	@Test
	public void moveFileShouldWork() {

	}

	@Test
	public void moveDirectoryShouldWork() {

	}
}
