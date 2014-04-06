package test;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import utils.BlockSettings;
import fileManager.FileManager;
import fileManager.MetaData;

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

	@Ignore
	public void getFreeOccupiedTotalMemoryShouldWork() throws Exception {
		FileManager fileManager = new FileManager(TestUtilities.WINDOWS_PATH);
		fileManager.writeRoot(rootMetaData);
		logger.info(fileManager.getTotalMemory());
		logger.info(fileManager.getOccupiedMemory());
		logger.info(fileManager.getFreeMemory());
	}

	@Test
	public void checkIfRootIsProperlyWritten() throws Exception {
		FileManager fileManager = new FileManager(TestUtilities.WINDOWS_PATH);
		fileManager.writeRoot(rootMetaData);
		MetaData rootMetaReadFromDisk = fileManager.getMetaData(0);
		assertEquals(BlockSettings.ROOT_NAME, rootMetaReadFromDisk.getName());
		assertEquals(BlockSettings.ROOT_PARENT,
				rootMetaReadFromDisk.getParent());
		assertEquals(BlockSettings.ROOT_TIMESTAMP,
				rootMetaReadFromDisk.getTimestamp());
		assertEquals(BlockSettings.ROOT_POSITION,
				rootMetaReadFromDisk.getPosition());
	}
}
