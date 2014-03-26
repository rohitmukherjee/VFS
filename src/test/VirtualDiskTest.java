package test;

import org.junit.Ignore;
import org.junit.Test;

import virtualDisk.VirtualDisk;
import exceptions.IncorrectPathException;

public class VirtualDiskTest {

	@Ignore
	public void settingUpDiskWithValidPathShouldWork() throws Exception {
		// TODO: RM
	}

	@Test(expected = IncorrectPathException.class)
	public void settingUpDiskWithInvalidPathShouldThrowAnException()
			throws Exception {
		VirtualDisk virtualDisk = new VirtualDisk("D:/");
		virtualDisk.setupDisk();
	}
}
