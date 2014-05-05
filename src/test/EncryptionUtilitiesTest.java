package test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

import utils.EncryptionUtilities;

public class EncryptionUtilitiesTest {

	@Test
	public void encryptedTextShouldNotBeEqualToText() throws Exception {
		byte[] array = new byte[] { 127, -128, 0 };
		byte[] arrayEncrypted = EncryptionUtilities.encrypt(array);
		assertFalse(array.equals(arrayEncrypted));
	}

	@Test
	public void decryptionShouldWork() throws Exception {
		byte[] array = new byte[] { 127, -128, 0 };
		byte[] arrayEncrypted = EncryptionUtilities.encrypt(array);
		byte[] arrayDecrypted = EncryptionUtilities.decrypt(arrayEncrypted);
		assertArrayEquals(arrayDecrypted, array);
	}
}
