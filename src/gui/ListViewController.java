package gui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;

public class ListViewController implements Initializable {
	@FXML
	private ListView<String> list;
	private ObservableList<String> children;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub

	}
	
	public void addChild(String child) {
		checkChildren();
		children.add(child);
		list.setItems(children);
	}
	
	public void clearChildren() {
		checkChildren();
		children.clear();
		list.setItems(children);
	}
	
	private void checkChildren() {
		if (children == null) {
			children = list.getItems();
		}
	}

}
