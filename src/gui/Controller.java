package gui;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import utils.BlockSettings;
import utils.FileSystemUtilities;
import fileSystem.FileSystem;

public class Controller implements Initializable {

	private FileSystem fileSystem;
	private static Logger logger;

	/* All GUI element initializations go here */
	@FXML
	private TextArea directoryName;
	@FXML
	private Label status;
	@FXML
	private ListView<String> childrenList;
	private ObservableList<String> children = FXCollections
			.observableArrayList();
	@FXML
	private Label cwd;

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		try {
			fileSystem = new FileSystem("test.vdisk");
			logger = Logger.getLogger(Controller.class);
			BasicConfigurator.configure();
			childrenList.setItems(children);
			status.setText("VFS Loaded");
			updateCwdLabel();
			listContents();
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
		listContents();
	}

	@FXML
	public void handleExportFile(ActionEvent event) {
		// get file currently highlighted
		// export to the set application path with fileName.
		String fileSelected = childrenList.getSelectionModel()
				.getSelectedItem();
		logger.debug("Currently selected " + fileSelected);
		if (fileSelected != null)
			try {
				byte[] fileData = fileSystem.readFile(fileSystem
						.getFilePath(fileSelected));
				FileSystemUtilities.exportFile(fileData, fileSelected);
				status.setText("Exported " + fileSelected + " successfully");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				status.setText("Couldn't export file");
			}
	}

	@FXML
	public void handleBack(ActionEvent event) {
		if (fileSystem.getCurrentDirectory().equals(BlockSettings.ROOT_NAME))
			status.setText("Already at highest level");
		else {
			String parentDirectory = fileSystem.getParentOfCurrentDirectory();
			fileSystem.setCurrentDirectory(parentDirectory);
			listContents();
		}
	}

	@FXML
	public void handleOpenDirectory(ActionEvent event) {
		String ItemSelected = childrenList.getSelectionModel()
				.getSelectedItem();
		if (FileSystemUtilities.isDirectoryName(ItemSelected)) {

			fileSystem.setCurrentDirectory(fileSystem.getCurrentDirectory()
					+ "/" + ItemSelected);
			listContents();
		} else {
			status.setText("Please select a valid directory");
		}
	}

	@FXML
	public void handleCopyFile(ActionEvent event) {
	}

	@FXML
	public void handleMoveFile(ActionEvent event) {

	}

	@FXML
	public void handleAddDirectory(ActionEvent event) {

		// App.console.runCommand("", new String[] {App.source,
		// App.destination});
	}

	@FXML
	public void handleCreateDirectory(ActionEvent event) {
		String directoryToCreate = directoryName.getText();
		if (directoryToCreate.trim().isEmpty()
				|| directoryToCreate.trim().equals(BlockSettings.ROOT_NAME)) {
			status.setText("Please provide a valid directory name");
			return;
		}
		try {
			fileSystem.addNewDirectory(directoryToCreate,
					fileSystem.getCurrentDirectory());
		} catch (Exception e) {
			status.setText(e.getMessage());
		}
		status.setText("Created directory " + directoryToCreate + " in "
				+ fileSystem.getCurrentDirectory());
		listContents();
		directoryName.clear();
	}

	@FXML
	public void handleDeleteDirectory(ActionEvent event) {
	}

	private void listContents() {
		children.clear();
		String cwd = fileSystem.getCurrentDirectory();
		try {
			String[] directoryContents = fileSystem.getChildren(cwd);
			children.addAll(directoryContents);
			updateCwdLabel();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void updateCwdLabel() {
		cwd.setText(fileSystem.getCurrentDirectory());
	}
}
