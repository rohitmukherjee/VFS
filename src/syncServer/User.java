package syncServer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

import fileSystem.FileSystem;

public class User {
	private String username;
	private String password;
	private FileSystem fs;
	private String path;
	private List<String> devices;
	private Map<String, LinkedBlockingQueue<String>> changeList;
	
	public User(String username, String password) throws Exception {
		this.username = username;
		this.password = password;
		devices = new ArrayList<String>();
		changeList = new HashMap<String, LinkedBlockingQueue<String>>();
		path = "~/" + username;
		fs = new FileSystem(path);
	}
	
	//adds a device
	public void addDevice(String uid) {
		
	}
	
	//adds command to the change list (of all other devices)
	//need to locally store every file to update incase the current VFS 
	//doesnt have it. need to think aobut speciic implamentation
	public void propagateUpdate(String device, String command){
		
	}
	
	public LinkedBlockingQueue<String> getChangeList(String device) {
		return changeList.get(device);
	}
}
