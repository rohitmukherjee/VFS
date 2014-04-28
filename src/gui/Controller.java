package gui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

public class Controller implements Initializable {

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
	}

	@FXML
	public void handleImportFile(ActionEvent event) {
		App.console.runCommand("import", new String[] { App.source,
				App.destination });
	}

	@FXML
	public void handleExportFile(ActionEvent event) {
		App.console.runCommand("export", new String[] { App.source,
				App.destination });
	}

	@FXML
	public void handleCopyFile(ActionEvent event) {
		App.console.runCommand("cp",
				new String[] { App.source, App.destination });
	}

	@FXML
	public void handleMoveFile(ActionEvent event) {
		App.console.runCommand("mv",
				new String[] { App.source, App.destination });
	}
	
	@FXML
	public void handleAddDirectory(ActionEvent event) {
		App.console.runCommand("", new String[] {App.source, App.destination});
	}

	@FXML
	public void handleCreateDirectory(ActionEvent event) {
		App.console.runCommand("create", new String[] { App.source });
	}

	@FXML
	public void handleDeleteDirectory(ActionEvent event) {
		App.console.runCommand("rm", new String[] { App.source });
	}

}
