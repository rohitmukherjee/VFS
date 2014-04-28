package gui;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.Reader;

import cli.Console;
import fileSystem.FileSystem;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
 
public class App extends Application {
    
	public static String source = "";
	public static String destination = "";
	private static FileSystem fs;
	public static Console console;
	
    @Override
    public void start(Stage primaryStage) throws Exception {
    	fs = new FileSystem(null);//FIX THIS IT WILL NOT WORK AT ALL
    	PrintWriter pw = new PrintWriter(System.out);
    	BufferedReader reader = new BufferedReader(null);
    	console = new Console(reader, pw, fs);
    	
        Button btn = new Button();
        btn.setText("Import File");
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Hello World!");
            }
        });
        
        StackPane root = new StackPane();
        root.getChildren().add(btn);

        Scene scene = new Scene(root, 300, 250);

        primaryStage.setTitle("Hello World!");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
	
 public static void main(String[] args) {
        launch(args);
    }
}
