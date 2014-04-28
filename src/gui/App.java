package gui;

import java.io.BufferedReader;
import java.io.PrintWriter;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import cli.Console;
import fileSystem.FileSystem;
 
public class App extends Application {
    
	public static String source = "";
	public static String destination = "";
	private static FileSystem fs;
	public static Console console;
	
	public static ListViewController listController;
	
    @Override
    public void start(Stage primaryStage) throws Exception {
    	
    	/*fs = new FileSystem(null);//FIX THIS IT WILL NOT WORK AT ALL
    	PrintWriter pw = new PrintWriter(System.out);
    	BufferedReader reader = new BufferedReader(null);
    	console = new Console(reader, pw, fs);
    	*/
    	
        FXMLLoader loader = new FXMLLoader(gui.ListViewController.class.getResource("children_list_view.fxml")); 
        
        Parent root = FXMLLoader.load(gui.Controller.class.getResource("gui_root.fxml"));

        loader.load();
        listController = loader.getController();
       // ListViewController listController = new ListViewController(root.get)
        /*Button btn = new Button();
        btn.setText("Import File");
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Hello World!");
            }
        });
        
        StackPane root = new StackPane();
        root.getChildren().add(btn); */

        Scene scene = new Scene(root, 300, 250);

        //primaryStage.setTitle("Hello World!");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
	
 public static void main(String[] args) {
        launch(args);
    }
}
