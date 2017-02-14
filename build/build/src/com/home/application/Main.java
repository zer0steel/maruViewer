package com.home.application;
	
import java.io.IOException;
import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class Main extends Application {
	
	public static URL mainFXML = null;
	public static URL viewFXML = null;
	public static URL dialogFXML = null;
	
	@Override
	public void start(Stage primaryStage) throws IOException {
		mainFXML = getClass().getResource("fxml/Main.fxml");
		viewFXML = getClass().getResource("fxml/ComicsView.fxml");
		dialogFXML = getClass().getResource("fxml/SearchResultDialog.fxml");
		
		Parent root = FXMLLoader.load(mainFXML);
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("application.css").toString());
		primaryStage.setScene(scene);
		primaryStage.show();
		primaryStage.setTitle("MaruViewer");
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
