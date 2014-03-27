package test;

import org.junit.Test;

import virtualDisk.VirtualDisk;
import exceptions.IncorrectPathException;

public class VirtualDiskTest {

	@Test
	public void settingUpDiskWithValidPathShouldWork() throws Exception {
		VirtualDisk virtualDisk = new VirtualDisk("D:/hello.vdisk");
		virtualDisk.setupDisk();
	}

	@Test(expected = IncorrectPathException.class)
	public void settingUpDiskWithInvalidPathShouldThrowAnException()
			throws Exception {
		VirtualDisk virtualDisk = new VirtualDisk("D:/");
		virtualDisk.setupDisk();
	}
}
