package gui;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import fileSystem.FileSystem;

public class Controller implements Initializable {

	private FileSystem fileSystem;
	private static Logger logger;

	/* All GUI element initializations go here */
	@FXML
	private TextArea directoryName;
	@FXML
	private Label status;

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		try {
			fileSystem = new FileSystem("test.vdisk");
			logger = Logger.getLogger(Controller.class);
			BasicConfigurator.configure();
		} catch (Exception e) {
			status.setText("Your virtual Disk could not be initialized");
		}
	}

	@FXML
	public void handleImportFile(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open File");
		List<File> files = fileChooser.showOpenMultipleDialog(null);
		for (File file : files) {
			try {
				fileSystem.importFile(file.getAbsolutePath());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				status.setText(e.getMessage());
			}
			logger.info("Imported " + file.getName() + "into virtual disk");
		}
		// Update status
		status.setText("Imported " + files.size() + " files into virtualDisk");
	}

	@FXML
	public void handleExportFile(ActionEvent event) {

	}

	@FXML
	public void handleCopyFile(ActionEvent event) {
	}

	@FXML
	public void handleMoveFile(ActionEvent event) {
	}

	@FXML
	public void handleAddDirectory(ActionEvent event) {
		App.listController.addChild("lskdfjsl");
		// App.console.runCommand("", new String[] {App.source,
		// App.destination});
	}

	@FXML
	public void handleCreateDirectory(ActionEvent event) {
		String directoryToCreate = directoryName.getText();
		try {
			fileSystem.addNewDirectory(directoryToCreate,
					fileSystem.getCurrentDirectory());
		} catch (Exception e) {
			status.setText(e.getMessage());
		}
		status.setText("Created directory " + directoryToCreate + " in "
				+ fileSystem.getCurrentDirectory());
		directoryName.clear();
	}

	@FXML
	public void handleDeleteDirectory(ActionEvent event) {
	}

}
