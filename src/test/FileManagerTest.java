package test;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Test;

import utils.BlockSettings;
import fileManager.FileManager;
import fileManager.MetaData;

public class FileManagerTest {

	static Logger logger;
	private static MetaData rootMetaData = new MetaData(
			BlockSettings.ROOT_NAME, BlockSettings.ROOT_PARENT,
			BlockSettings.ROOT_TYPE, BlockSettings.ROOT_TIMESTAMP);

	@BeforeClass
	public static void setUp() {
		logger = Logger.getLogger(FileManagerTest.class);
		BasicConfigurator.configure();
		rootMetaData.setParent(BlockSettings.ROOT_POSITION);
	}

	@Test
	public void getFreeOccupiedTotalMemoryShouldWork() throws Exception {
		FileManager fileManager = new FileManager(TestUtilities.WINDOWS_PATH);
		fileManager.writeRoot(rootMetaData);
		logger.info(fileManager.getTotalMemory());
		logger.info(fileManager.getOccupiedMemory());
		logger.info(fileManager.getFreeMemory());
	}
}
