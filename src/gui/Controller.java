package gui;

import javafx.event.ActionEvent;

public class Controller {
	public class MyController {
	    public void handleImportFile(ActionEvent event) {
	    	App.console.runCommand("import",
	    			new String[] {App.source, App.destination});
	    }
	    
	    public void handleExportFile(ActionEvent event) {
	    	App.console.runCommand("export",
	    			new String[] {App.source, App.destination});
	    }
	    
	    public void handleCopyFile(ActionEvent event) {
	    	App.console.runCommand("cp",
	    			new String[] {App.source, App.destination});
	    }
	    
	    public void handleMoveFile(ActionEvent event) {
	    	App.console.runCommand("mv",
	    			new String[] {App.source, App.destination});
	    }

	    public void handleCreateDirectory(ActionEvent event) {
	    	App.console.runCommand("create",
	    			new String[] {App.source});
	    }
	    
	    public void handleDeleteDirectory(ActionEvent event) {
	    	App.console.runCommand("rm",
	    			new String[] {App.source});
	    }

	}
}
