package test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import utils.BlockSettings;
import utils.MetaDataUtilities;
import fileManager.MetaData;

public class MetaDataTest {

	static Logger logger;
	private static MetaData rootMeta;
	private static byte[] rootBytes;

	@BeforeClass
	public static void setUpBeforeClass() {
		logger = Logger.getLogger(MetaDataTest.class);
		rootMeta = new MetaData(BlockSettings.ROOT_NAME,
				BlockSettings.ROOT_PARENT, BlockSettings.ROOT_TYPE,
				BlockSettings.ROOT_TIMESTAMP);
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
		assertEquals(meta.getName(), utils.BlockSettings.ROOT_NAME);
		assertEquals(meta.getParent(), utils.BlockSettings.ROOT_PARENT);
		assertTrue(meta.getType() == utils.BlockSettings.ROOT_TYPE);
		assertEquals(meta.getTimestamp(), utils.BlockSettings.ROOT_TIMESTAMP);
		assertArrayEquals(meta.getBytes(), rootMeta.getBytes());
		assertEquals(meta, rootMeta);

		byte[] randomBytes = new byte[utils.BlockSettings.METADATA_LENGTH];
		for (int i = 0; i < randomBytes.length; ++i) {
			// put some random stuff in the byte array that still can be
			// interpreted correctly
			randomBytes[i] = (byte) (i % 8 + '0');
		}
		MetaData meta2 = new MetaData(randomBytes);
		byte[] meta2Bytes = meta2.getBytes();
		assertArrayEquals(meta2Bytes, randomBytes);
		assertTrue(meta2 != meta);
	}

	@Test
	public void testMetaDataArgsCtor() {
		long time = System.currentTimeMillis();
		MetaData meta = new MetaData("metaDataObject", 10, (byte) 1, time);
		assertEquals("metaDataObject", meta.getName());
		assertEquals(10, meta.getParent());
		assertTrue(1 == meta.getType());
		assertEquals(time, meta.getTimestamp());
		assertEquals(-1, meta.getBlockNumber());

	}

	@Test
	public void testMetaDataGettersAndSetters() {
		long time = System.currentTimeMillis();
		MetaData meta = new MetaData("metaDataObject", 10, (byte) 1, time);
		assertEquals("metaDataObject", meta.getName());
		assertEquals(10, meta.getParent());
		assertTrue(1 == meta.getType());
		assertEquals(time, meta.getTimestamp());
		assertEquals(-1, meta.getBlockNumber());

		meta.setBlockNumber(2);
		meta.setParent(23);
		assertEquals(2, meta.getBlockNumber());
		assertEquals(23, meta.getParent());

		meta.setTimestamp(time + 1000);
		meta.setName(null);
		meta.setType((byte) 19);
		assertEquals(time + 1000, meta.getTimestamp());
		assertEquals(null, meta.getName());
		assertTrue(19 == meta.getType());
	}

	@Test
	public void testMetaDataToBytes() throws IOException {
		// some constants to change for this test
		// just want to say this test is super slick
		String name = "aaaaasd";
		long parent = 11212;
		long timestamp = 23;
		byte fileType = 6;

		byte[] Bytes = new byte[utils.BlockSettings.METADATA_LENGTH];
		for (int i = 0; i < Bytes.length; ++i) {
			if (i < 8) {
				Bytes[i] = -1;
			} else if (i == 8) {
				Bytes[i] = fileType;
			} else if (i < 9 + utils.BlockSettings.FILENAME_LENGTH
					- name.length()) {
				Bytes[i] = (byte) ' ';
			} else if (i < 9 + utils.BlockSettings.FILENAME_LENGTH) {
				Bytes[i] = (byte) name.charAt(i
						- (9 + utils.BlockSettings.FILENAME_LENGTH - name
								.length()));
			} else if (i < 17 + utils.BlockSettings.FILENAME_LENGTH) {
				// oh shit some sweet math going on here
				Bytes[i] = (byte) (timestamp
						/ (Math.pow(256, 16
								+ utils.BlockSettings.FILENAME_LENGTH - i)) % 256);
			} else {
				Bytes[i] = (byte) (parent
						/ (Math.pow(256, 24
								+ utils.BlockSettings.FILENAME_LENGTH - i)) % 256);
			}
		}
		MetaData meta = new MetaData(name, parent, fileType, timestamp);
		byte[] metaBytes = meta.getBytes();
		assertArrayEquals(metaBytes, Bytes);
	}

	@Test
	public void testStringToBytes() {
		//change this to test different strings
		String string = "asdlk wlekfjosd 3o2rn";
		
		byte[] bytes = new byte[string.length()];
		for (int i = 0; i < bytes.length; ++i) {
				bytes[i] = (byte) string.charAt(i);
		}
		byte[] stringInBytes = utils.MetaDataUtilities.StringToBytes(string);
		assertArrayEquals(bytes, utils.MetaDataUtilities.StringToBytes(string));
		assertEquals(string, utils.MetaDataUtilities.bytesToString(bytes));
	}

	@Test
	public void testBytesToString() {
		//taken care of in testStringToBytes()
	}

	@Test
	public void testLongToBytes() {
		//test 0
		long zero = 0;
		byte[] zeroBytes = {0,0,0,0,0,0,0,0};
		assertEquals(zero, utils.MetaDataUtilities.bytesToLong(zeroBytes));
		assertArrayEquals(zeroBytes, utils.MetaDataUtilities.longToBytes(zero));
		
		//test any negative number
		long negNum = -2323255;
		byte[] negBytes = new byte[8];
		boolean carry = true;
		for (int i = 0; i < 8; ++i) {
			negBytes[7-i] = (byte) ((byte) ((byte) (-1*negNum/Math.pow(256, i))%256)^ (byte) -1);
				if (carry) {
					if (negBytes[7-i] == -1) {
						negBytes[7-i] = 0;
						carry = true;
					} else {
						
					negBytes[7-i] += 1;
					carry = false;
					}
				}
		}
		byte[] test = utils.MetaDataUtilities.longToBytes(negNum);
		assertEquals(negNum, utils.MetaDataUtilities.bytesToLong(negBytes));
		assertArrayEquals(negBytes, utils.MetaDataUtilities.longToBytes(negNum));
		
		
		//test any positive number
		long number = 255;
		byte[] bytes = new byte[8];
		for (int i = 0; i < 8; ++i) {
			bytes[i] = (byte) ((byte) (number/Math.pow(256, 7-i))%256);
		}
		assertEquals(number, utils.MetaDataUtilities.bytesToLong(bytes));
		assertArrayEquals(bytes, utils.MetaDataUtilities.longToBytes(number));
	}

	@Test
	public void testBytesToLong() {
		//taken care of in testLongToBytes
	}

	@Test
	public void testFileToBytes() {

	}

	@Test
	public void testBytesToFile() {

	}

	// compressed/decompressed

}
