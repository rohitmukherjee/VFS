package test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Test;

import virtualDisk.VirtualDisk;
import exceptions.IncorrectPathException;

public class VirtualDiskTest {
	private final String WINDOWS_PATH = "D:/test.vdisk";

	@Test
	public void settingUpDiskWithValidPathShouldWork() throws Exception {
		VirtualDisk virtualDisk = new VirtualDisk(WINDOWS_PATH);
		virtualDisk.setupDisk();
		virtualDisk.deleteDisk();
	}

	@Test(expected = IncorrectPathException.class)
	public void settingUpDiskWithInvalidPathShouldThrowAnException()
			throws Exception {
		VirtualDisk virtualDisk = new VirtualDisk("D:/");
		virtualDisk.setupDisk();
	}

	@Test
	public void deleteDiskShouldDeleteTheDiskCreated() throws Exception {
		VirtualDisk virtualDisk = new VirtualDisk(WINDOWS_PATH);
		virtualDisk.setupDisk();
		File f = new File(WINDOWS_PATH);
		assertTrue(f.exists());
		virtualDisk.deleteDisk();
		assertFalse(f.exists());
	}

}
