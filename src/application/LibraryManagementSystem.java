package application;
	
import GUI.MainViewController;
import GUI.utils.Utils;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


public class LibraryManagementSystem extends Application {
	
	private static Stage primaryStage;
	
	@Override
	public void start(Stage primaryStage) {
		try {
			LibraryManagementSystem.primaryStage = primaryStage;
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/MainView.fxml"));
			AnchorPane root = loader.load();
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setTitle("Library Management System");
			primaryStage.getIcons().add(Utils.getWindowIcon());
			primaryStage.setHeight(900);
			primaryStage.setWidth(1200);
			((MainViewController)loader.getController()).init();
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static Stage getPrimaryStage() {
		return primaryStage;
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
