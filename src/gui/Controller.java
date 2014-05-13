package gui;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.DirectoryChooser;
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
	private TextField directoryName;
	@FXML
	private TextField searchBox;
	@FXML
	private Label status;
	@FXML
	private ListView<String> childrenList;
	private ObservableList<String> children = FXCollections
			.observableArrayList();
	@FXML
	private Label cwd;
	@FXML
	private CheckBox caseSensitiveSearch;
	private String moveClipboard = "";
	private String copyClipboard = "";

	/* Context Menu Specific Code */
	final ContextMenu options = new ContextMenu();
	MenuItem delete = new MenuItem(GUIMessages.DELETE_BUTTON);
	MenuItem rename = new MenuItem(GUIMessages.RENAME_BUTTON);
	MenuItem move = new MenuItem(GUIMessages.MOVE_BUTTON);
	MenuItem moveHere = new MenuItem(GUIMessages.MOVE_HERE_BUTTON);
	MenuItem importFiles = new MenuItem(GUIMessages.IMPORT_BUTTON);
	MenuItem exportFiles = new MenuItem(GUIMessages.EXPORT_BUTTON);
	MenuItem copy = new MenuItem(GUIMessages.COPY_BUTTON);
	MenuItem copyHere = new MenuItem(GUIMessages.COPY_HERE_BUTTON);
	private boolean searchMode = false;

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

		exportFiles.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				exportFile();
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

		importFiles.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				importFiles();
			}
		});

		move.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				String itemSelected = childrenList.getSelectionModel()
						.getSelectedItem();
				if (itemSelected == null)
					status.setText(GUIMessages.SELECT_ERROR);
				else
					moveClipboard = fileSystem.getFilePath(itemSelected);
			}
		});

		copy.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				String itemSelected = childrenList.getSelectionModel()
						.getSelectedItem();
				if (itemSelected == null)
					status.setText(GUIMessages.SELECT_ERROR);
				else
					copyClipboard = fileSystem.getFilePath(itemSelected);
			}
		});

		copyHere.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				String name;
				if (copyClipboard.trim().isEmpty())
					status.setText(GUIMessages.SELECT_ERROR);

				else if (FileSystemUtilities.isFileName(copyClipboard)) {
					name = copyClipboard.substring(copyClipboard
							.lastIndexOf("/"));
					try {
						fileSystem.copyFile(copyClipboard, fileSystem
								.getCurrentDirectory().concat(name));
						populateListView();
					} catch (Exception e) {
						status.setText(GUIMessages.COPY_FILE_ERROR);
						e.printStackTrace();
					}
					copyClipboard = "";
				}
			}
		});

		moveHere.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				String name;
				if (moveClipboard.trim().isEmpty())
					status.setText(GUIMessages.SELECT_ERROR);

				else if (FileSystemUtilities.isFileName(moveClipboard)) {
					name = moveClipboard.substring(moveClipboard
							.lastIndexOf("/"));
					try {
						fileSystem.moveFile(moveClipboard, fileSystem
								.getCurrentDirectory().concat(name));
						populateListView();
					} catch (Exception e) {
						status.setText(GUIMessages.MOVE_FILE_ERROR);
						e.printStackTrace();
					}
					moveClipboard = "";
				}

				else if (FileSystemUtilities.isDirectoryName(moveClipboard)) {
					name = moveClipboard.substring(moveClipboard
							.lastIndexOf("/"));
					try {
						fileSystem.moveDirectory(moveClipboard, fileSystem
								.getCurrentDirectory().concat(name));
					} catch (Exception e) {
						status.setText(GUIMessages.MOVE_DIRECTORY_ERROR);
						e.printStackTrace();
					}
					moveClipboard = "";
				}
			}
		});

		// This is the order of context menu items
		options.getItems().add(importFiles);
		options.getItems().add(exportFiles);
		options.getItems().add(move);
		options.getItems().add(moveHere);
		options.getItems().add(copy);
		options.getItems().add(copyHere);
		options.getItems().add(delete);
		options.getItems().add(rename);

		// Keyboard Shortcuts are set here

		this.delete.setAccelerator(new KeyCodeCombination(KeyCode.DELETE));

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

	private void exportFile() {
		// get file currently highlighted
		// export to the set application path with fileName.
		String fileSelected = childrenList.getSelectionModel()
				.getSelectedItem();
		DirectoryChooser directoryChooser = new DirectoryChooser();
		directoryChooser.setTitle(GUIMessages.EXPORT_DIALOG);
		File targetDirectory = directoryChooser.showDialog(null);
		String outputFile = targetDirectory.getAbsolutePath().concat(
				fileSelected);
		logger.debug("Exporting file to " + outputFile);
		if (fileSelected != null && targetDirectory != null)
			try {
				byte[] fileData = fileSystem.readFile(fileSystem
						.getFilePath(fileSelected));
				FileSystemUtilities.exportFile(fileData, outputFile);
				status.setText(GUIMessages.EXPORT + fileSelected
						+ GUIMessages.SUCCESS);
			} catch (Exception e) {
				status.setText(GUIMessages.EXPORT_ERROR);
			}
	}

	@FXML
	public void handleBack(ActionEvent event) {
		if (!searchMode) {
			if (fileSystem.getCurrentDirectory()
					.equals(BlockSettings.ROOT_NAME))
				status.setText(GUIMessages.IN_ROOT);
			else {
				String parentDirectory = fileSystem
						.getParentOfCurrentDirectory();
				fileSystem.setCurrentDirectory(parentDirectory);
				populateListView();
			}
		} else {
			populateListView();
			searchMode = false;
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

	@FXML
	public void search(ActionEvent event) {
		String searchTerm = searchBox.getText();
		boolean caseSensitive = caseSensitiveSearch.isSelected();
		logger.debug("Searching for term: " + searchTerm
				+ "with Case - Sensitive " + String.valueOf(caseSensitive));
		ArrayList<String> searchResults = new ArrayList<String>();
		if (searchTerm.trim().isEmpty()
				|| searchTerm.trim().equals(BlockSettings.ROOT_NAME)
				|| searchTerm == null) {
			status.setText(GUIMessages.PROVIDE_SEARCH_TERM);
			return;
		}
		try {
			searchResults = fileSystem.searchCache(searchTerm, caseSensitive);
			if (searchResults.isEmpty())
				status.setText(GUIMessages.SEARCH_NO_RESULTS);
		} catch (Exception e) {
			status.setText(GUIMessages.SEARCH_ERROR);
		}
		updateListView(searchResults);
		searchMode = true;
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

	private void moveFile(String fileName) {
		try {
			logger.debug("Move File " + fileSystem.getFilePath(fileName));
			fileSystem.deleteFile(fileSystem.getFilePath(fileName));
			populateListView();
		} catch (Exception ex) {
			status.setText(GUIMessages.PROVIDE_FILE);
		}

	}

	private void importFiles() {
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

	private void updateListView(List<String> fileList) {
		children.clear();
	}

}