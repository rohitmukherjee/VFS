package gui;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
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

	/* Context Menu Specific Code */
	final ContextMenu options = new ContextMenu();
	MenuItem delete = new MenuItem(GUIMessages.DELETE_BUTTON);
	MenuItem rename = new MenuItem(GUIMessages.RENAME_BUTTON);

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		try {
			fileSystem = new FileSystem("test.vdisk");
			logger = Logger.getLogger(Controller.class);
			BasicConfigurator.configure();
			initializeMenu();
			childrenList.setItems(children);
			status.setText(GUIMessages.STARTUP_SUCCESS);
			populateListView();
		} catch (Exception e) {
			status.setText(GUIMessages.STARTUP_ERROR);
		}
	}

	private void initializeMenu() {
		delete.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				String itemSelected = childrenList.getSelectionModel()
						.getSelectedItem();
				if (FileSystemUtilities.isDirectoryName(itemSelected))
					deleteDirectory(itemSelected);
				else if (FileSystemUtilities.isFileName(itemSelected))
					deleteFile(itemSelected);
				else if (itemSelected == null)
					status.setText(GUIMessages.SELECT_ERROR);
			}
		});

		rename.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				String itemSelected = childrenList.getSelectionModel()
						.getSelectedItem();
				if (FileSystemUtilities.isDirectoryName(itemSelected))
					renameDirectory(itemSelected, "TEXT");
				else if (FileSystemUtilities.isFileName(itemSelected))
					renameFile(itemSelected, "TEXT");
				else if (itemSelected == null)
					status.setText(GUIMessages.SELECT_ERROR);
			}
		});

		options.getItems().add(delete);
		options.getItems().add(rename);
		childrenList.addEventHandler(MouseEvent.MOUSE_CLICKED,
				new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						if (event.getButton().equals(MouseButton.SECONDARY)) {
							options.show(childrenList, event.getScreenX(),
									event.getScreenY());
						} else if (event.getButton()
								.equals(MouseButton.PRIMARY))
							options.hide();
					}
				});
	}

	@FXML
	public void handleImportFile(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle(GUIMessages.OPEN_DIALOG);
		List<File> files = fileChooser.showOpenMultipleDialog(null);
		for (File file : files) {
			try {
				fileSystem.importFile(file.getAbsolutePath());
			} catch (Exception e) {
				status.setText(e.getMessage());
			}
			logger.info(GUIMessages.IMPORT + file.getName());
		}
		// Update status
		status.setText(GUIMessages.IMPORTED_FILES_MSG);
		populateListView();
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
				status.setText(GUIMessages.EXPORT + fileSelected
						+ GUIMessages.SUCCESS);
			} catch (Exception e) {
				status.setText(GUIMessages.EXPORT_ERROR);
			}
	}

	@FXML
	public void handleBack(ActionEvent event) {
		if (fileSystem.getCurrentDirectory().equals(BlockSettings.ROOT_NAME))
			status.setText(GUIMessages.IN_ROOT);
		else {
			String parentDirectory = fileSystem.getParentOfCurrentDirectory();
			fileSystem.setCurrentDirectory(parentDirectory);
			populateListView();
		}
	}

	@FXML
	public void handleOpenDirectory(ActionEvent event) {
		String ItemSelected = childrenList.getSelectionModel()
				.getSelectedItem();
		if (FileSystemUtilities.isDirectoryName(ItemSelected)) {
			fileSystem.setCurrentDirectory(fileSystem.getCurrentDirectory()
					+ "/" + ItemSelected);
			populateListView();
		} else {
			status.setText(GUIMessages.PROVIDE_DIRECTORY);
		}
	}

	@FXML
	public void handleCopyFile(ActionEvent event) {
	}

	@FXML
	public void handleMoveFile(ActionEvent event) {

	}

	@FXML
	public void handleCreateDirectory(ActionEvent event) {
		String directoryToCreate = directoryName.getText();
		if (directoryToCreate.trim().isEmpty()
				|| directoryToCreate.trim().equals(BlockSettings.ROOT_NAME)) {
			status.setText(GUIMessages.PROVIDE_DIRECTORY_NAME);
			return;
		}
		try {
			fileSystem.addNewDirectory(directoryToCreate,
					fileSystem.getCurrentDirectory());
		} catch (Exception e) {
			status.setText(e.getMessage());
		}
		status.setText(GUIMessages.CREATED_DIRECTORY + directoryToCreate
				+ GUIMessages.IN + fileSystem.getCurrentDirectory());
		populateListView();
		directoryName.clear();
	}

	private void renameFile(String fileName, String newName) {
		try {
			fileSystem.renameFile(fileSystem.getFilePath(fileName),
					fileSystem.getFilePath(newName));
			populateListView();
		} catch (Exception ex) {
			status.setText(GUIMessages.PROVIDE_FILE);
		}
	}

	private void renameDirectory(String directoryName, String newName) {
		try {
			fileSystem.renameDirectory(fileSystem.getFilePath(directoryName),
					fileSystem.getFilePath(newName));
			populateListView();
		} catch (Exception ex) {
			status.setText(GUIMessages.PROVIDE_DIRECTORY);
		}

	}

	private void deleteFile(String fileName) {
		try {
			logger.debug("Deleting File " + fileSystem.getFilePath(fileName));
			fileSystem.deleteFile(fileSystem.getFilePath(fileName));
			populateListView();
		} catch (Exception ex) {
			status.setText(GUIMessages.PROVIDE_FILE);
		}
	}

	private void deleteDirectory(String directoryName) {
		try {
			logger.debug("Deleting File "
					+ fileSystem.getFilePath(directoryName));
			fileSystem.deleteDirectory(fileSystem.getFilePath(directoryName));
			populateListView();
		} catch (Exception ex) {
			status.setText(GUIMessages.PROVIDE_DIRECTORY);
		}
	}

	private void populateListView() {
		children.clear();
		String cwd = fileSystem.getCurrentDirectory();
		try {
			updateCwdLabel();
			String[] directoryContents = fileSystem.getChildren(cwd);
			children.addAll(directoryContents);
		} catch (Exception e) {
			status.setText(GUIMessages.LIST_ERROR);
			e.printStackTrace();
		}
	}

	private void updateCwdLabel() {
		cwd.setText(fileSystem.getCurrentDirectory());
	}
}
