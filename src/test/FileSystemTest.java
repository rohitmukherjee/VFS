package test;

import static org.junit.Assert.*;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import fileSystem.FileSystem;

public class FileSystemTest {

	static Logger logger;
	private static FileSystem fs;
	

	@BeforeClass
	public static void setUpBeforeClass() {
		logger = Logger.getLogger(FileManagerTest.class);
		BasicConfigurator.configure();
	}
	
	@Before
	public void setUp() throws Exception {
		fs = new FileSystem(TestUtilities.POSIX_PATH);
	}
	
	@Test
	public void TestAddDirectory() throws Exception {
		
		//check something doesnt already exist
		assertFalse(fs.isValidDirectory(utils.BlockSettings.ROOT_NAME+"/adirectory"));
		
		//add directory from root
		fs.addNewDirectory("adirectory", utils.BlockSettings.ROOT_NAME);
		assertTrue(fs.isValidDirectory(utils.BlockSettings.ROOT_NAME+"/adirectory"));
		
		//add new directory from root
		fs.addNewDirectory("/anotherone", utils.BlockSettings.ROOT_NAME);
		assertTrue(fs.isValidDirectory(utils.BlockSettings.ROOT_NAME+"/anotherone"));
		
		//add new directory inside another
		fs.addNewDirectory("nesteddirectory", utils.BlockSettings.ROOT_NAME+"/adirectory");
		
	}
}
