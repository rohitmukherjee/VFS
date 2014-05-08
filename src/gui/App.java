package gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

	private Stage primaryStage;

	public static ListViewController listController;

	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		primaryStage.setTitle("Batman VFS Browser");
		FXMLLoader loader = new FXMLLoader(
				gui.ListViewController.class
						.getResource("children_list_view.fxml"));
		Parent root = FXMLLoader.load(gui.Controller.class
				.getResource("gui_root.fxml"));
		loader.load();
		listController = loader.getController();
		// ListViewController listController = new ListViewController(root.get)
		/*
		 * Button btn = new Button(); btn.setText("Import File");
		 * btn.setOnAction(new EventHandler<ActionEvent>() {
		 * 
		 * @Override public void handle(ActionEvent event) {
		 * System.out.println("Hello World!"); } });
		 * 
		 * StackPane root = new StackPane(); root.getChildren().add(btn);
		 */

		Scene scene = new Scene(root, 300, 250);
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
