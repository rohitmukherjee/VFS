package gui;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.FileChooser;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import fileSystem.FileSystem;

public class Controller implements Initializable {

	private FileSystem fileSystem;
	private static Logger logger;

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		try {
			fileSystem = new FileSystem("test.vdisk");
			logger = Logger.getLogger(Controller.class);
			BasicConfigurator.configure();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@FXML
	public void handleImportFile(ActionEvent event) throws Exception {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open File");
		List<File> files = fileChooser.showOpenMultipleDialog(null);
		for (File file : files) {
			fileSystem.importFile(file.getAbsolutePath());
			logger.info("Imported " + file.getName() + "into virtual disk");
		}
	}

	@FXML
	public void handleExportFile(ActionEvent event) {

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
		App.listController.addChild("lskdfjsl");
		// App.console.runCommand("", new String[] {App.source,
		// App.destination});
	}

	@FXML
	public void handleCreateDirectory(ActionEvent event) throws Exception {
		fileSystem
				.addNewDirectory("fuck off", fileSystem.getCurrentDirectory());
	}

	@FXML
	public void handleDeleteDirectory(ActionEvent event) {
		App.console.runCommand("rm", new String[] { App.source });
	}

}
