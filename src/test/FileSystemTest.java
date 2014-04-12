package test;

import static org.junit.Assert.*;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
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
	
	@AfterClass
	public static void tearDownAfterClass() {
		
	}
	
	@Before
	public void setUp() throws Exception {
		fs = new FileSystem(TestUtilities.POSIX_PATH);
	}
	
	@After
	public void tearDown() {
		File f = new File(TestUtilities.POSIX_PATH);
		f.delete();
	}
	
	
	public void setUpFS() throws Exception{
		fs.addNewDirectory("directory1", utils.BlockSettings.ROOT_NAME);
		fs.addNewDirectory("directory2", utils.BlockSettings.ROOT_NAME);
		fs.addNewDirectory("directory3", utils.BlockSettings.ROOT_NAME);
		fs.addNewDirectory("directory4", utils.BlockSettings.ROOT_NAME);
		fs.addNewDirectory("nested1", utils.BlockSettings.ROOT_NAME+"/directory1");
		fs.addNewDirectory("nested2", utils.BlockSettings.ROOT_NAME+"/directory1");
		fs.addNewDirectory("supernested1", utils.BlockSettings.ROOT_NAME+"/directory1/nested1");
		fs.writeFile(utils.BlockSettings.ROOT_NAME+"/fileAtRoot", 
				Files.readAllBytes(Paths.get("root.txt")));
		fs.writeFile(utils.BlockSettings.ROOT_NAME+"/directory1/file", 
				Files.readAllBytes(Paths.get("indir1.txt")));
		fs.writeFile(utils.BlockSettings.ROOT_NAME+"/directory1/nested1/nestedfile", 
				Files.readAllBytes(Paths.get("innested1.txt")));
		fs.writeFile(utils.BlockSettings.ROOT_NAME+"/directory1/nested1/nestedfile2", 
				Files.readAllBytes(Paths.get("anotherinnested1.txt")));

	}
	
	@Test
	public void testAddDirectory() throws Exception {
		
		//check if root exists
		assertTrue(fs.isValidDirectory(utils.BlockSettings.ROOT_NAME));
		
		//check something doesnt already exist
		assertFalse(fs.isValidDirectory(utils.BlockSettings.ROOT_NAME+"/adirectory"));
		
		//add directory from root
		fs.addNewDirectory("adirectory", utils.BlockSettings.ROOT_NAME);
		assertTrue(fs.isValidDirectory(utils.BlockSettings.ROOT_NAME+"/adirectory"));
		
		//add new directory from root
		fs.addNewDirectory("/anotherone", utils.BlockSettings.ROOT_NAME);
		assertTrue(fs.isValidDirectory(utils.BlockSettings.ROOT_NAME+"/adirectory"));
		assertTrue(fs.isValidDirectory(utils.BlockSettings.ROOT_NAME+"/anotherone"));
		
		//add new directory inside another
		fs.addNewDirectory("nesteddirectory", utils.BlockSettings.ROOT_NAME+"/adirectory");
		
		//
	}
	
	@Test
	public void testDeleteDirectory() throws Exception {
		setUpFS();
		
	}
	
	
}
