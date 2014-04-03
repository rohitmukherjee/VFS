package cli;

import java.util.HashMap;
import java.util.Map;

import commands.ChangeDirectory;
import commands.Command;

public class CommandMap {

	Map<String, Command> myMap;

	public CommandMap(Console console) {
		myMap = new HashMap<String, Command>();
		addCommands(console);
	}

	private void addCommands(Console console) {
		// can change the keywords here or add more
		myMap.put("cd", new ChangeDirectory(console));
		myMap.put("copy", new CopyFile(console));
		myMap.put("create", new CreateDirectory(console));
		myMap.put("delete", new DeleteDirectory(console));
		myMap.put("export", new ExportFile(console));
		myMap.put("getfree", new GetFreeSpace(console));
		myMap.put("getocc", new GetOccupiedSpace(console));
		myMap.put("import", new ImportFile(console));
		myMap.put("list", new List(console));
		myMap.put("move", new MoveFile(console));
		myMap.put("pwd", new PWD(console));
		myMap.put("rename", new RenameDirectory(console));
	}

	public Command getCommandWithKey(String command) {
		return myMap.get(command);
	}
}
