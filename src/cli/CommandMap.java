package cli;

import java.util.HashMap;
import java.util.Map;

import commands.*;

public class CommandMap {

	Map<String, Command> myMap;

	/**
	 * Creates a new CommandMap object which maps specific strings to command objects
	 * @param console
	 */
	public CommandMap(Console console) {
		myMap = new HashMap<String, Command>();
		addCommands(console);
	}

	/**
	 * adds (string, command) pairs to the instance variable map
	 * command objects created here are given console object that created this 
	 * CommandObject as a parameter in their constructors 
	 * @param console
	 */
	private void addCommands(Console console) {
		// can change the keywords here or add more
		myMap.put("cd", new ChangeDirectory(console));
		myMap.put("cp", new CopyFile(console));
		myMap.put("create", new CreateDirectory(console));
		myMap.put("rm", new DeleteDirectory(console));
		myMap.put("export", new ExportFile(console));
		myMap.put("getfree", new GetFreeSpace(console));
		myMap.put("getocc", new GetOccupiedSpace(console));
		myMap.put("import", new ImportFile(console));
		myMap.put("ls", new List(console));
		myMap.put("mv", new MoveFile(console));
		myMap.put("pwd", new PWD(console));
		myMap.put("rename", new RenameDirectory(console));
	}

	/**
	 * 
	 * @param command - string to look for in map
	 * @return Command object associated with that string, or null if not found
	 */
	public Command getCommandWithKey(String command) {
		return myMap.get(command);
	}
}
