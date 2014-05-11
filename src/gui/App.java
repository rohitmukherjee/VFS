package gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

	private Stage primaryStage;

	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		primaryStage.setTitle("Batman VFS Browser");
		Parent root = FXMLLoader.load(gui.Controller.class
				.getResource("gui_root.fxml"));
		Scene scene = new Scene(root, 500, 550);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public Stage getPrimaryStage() {
		return primaryStage;
	}

	public static void main(String[] args) {
		launch(args);
	}
}
