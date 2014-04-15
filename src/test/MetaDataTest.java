package test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import utils.BlockSettings;
import fileManager.MetaData;
import fileManager.MetaDataUtilities;

public class MetaDataTest {
	
	static Logger logger;
	private static MetaData rootMeta;
	private static byte[] rootBytes;

	@BeforeClass
	public static void setUpBeforeClass() {
		logger = Logger.getLogger(MetaDataTest.class);
		rootMeta = new MetaData(
				BlockSettings.ROOT_NAME, BlockSettings.ROOT_PARENT,
				BlockSettings.ROOT_TYPE, BlockSettings.ROOT_TIMESTAMP);
		rootBytes = MetaDataUtilities.metaDataToBytes(rootMeta);
	}
	
	@AfterClass
	public static void tearDownAfterClass() {
		
	}
	
	@Before
	public void setUp() {
		
	}
	
	@After
	public void tearDown() {
		
	}
	
	
	@Test
	public void testMetaDataByteArrayCtor() throws IOException {
		MetaData meta = new MetaData(rootBytes);
		assertEquals(meta.getBlockNumber(), rootMeta.getBlockNumber());
		assertArrayEquals(meta.getBytes(), rootMeta.getBytes());
		assertEquals(meta.getName(), rootMeta.getName());
		assertEquals(meta.getParent(), rootMeta.getParent());
		assertEquals(meta.getTimestamp(), rootMeta.getTimestamp());
		assertEquals(meta.getType(), rootMeta.getType());
		
		byte[] randomBytes = new byte[utils.BlockSettings.METADATA_LENGTH];
		for (int i = 0; i < randomBytes.length; ++i) {
			//put some random stuff in the byte array that still can be interpreted correctly
			randomBytes[i] = (byte) (i%8 +'0');
		}
		MetaData meta2 = new MetaData(randomBytes);
		byte[] meta2Bytes = meta2.getBytes();
		assertArrayEquals(meta2Bytes, randomBytes);
	}
	
	@Test
	public void testMetaDataArgsCtor() {
		
	}
	
	@Test
	public void testMetaDataToBytes() {
		
	}
	
	@Test
	public void testStringToBytes() {
		
	}
	
	@Test
	public void testBytesToString() {
		
	}
	
	@Test
	public void testLongToBytes() {
		
	}
	
	@Test
	public void testBytesToLong() {
		
	}
	
	@Test
	public void testFileToBytes() {
		
	}
	
	@Test
	public void testBytesToFile() {
		
	}
	
	//compressed/decompressed
	
	
	
}
