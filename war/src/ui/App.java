package ui;

import java.io.FileNotFoundException;
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import mvc.WarController;

public class App extends Application {

	private static WarUI warUI;
	private WarFxmlController warUIController;

	@Override
	public void start(final Stage stage) throws Exception {
		try {
			// get layout from fxml-file
			FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(UIConstants.MAIN_PANE_FXML_PATH));
			BorderPane borderPane = (BorderPane) loader.load();
			warUIController = (WarFxmlController) loader.getController();
			
			// set scene on the stage
			Scene scene = new Scene(borderPane);
			scene.getStylesheets().add("ui/war.css");
			Stage primaryStage = new Stage();
			primaryStage.setScene(scene);
			primaryStage.show();
			
			warUI = new War(warUIController.getWarPane(), warUIController.getStatisticsPane() );
			WarController controller = new WarController();
			controller.addView(warUI);
			warUI.showReadFromConfigMenu();
			
			
		} catch (FileNotFoundException e) {e.printStackTrace();
		} catch (IOException e) {e.printStackTrace();}
	}
	
	public static WarUI getWarPane(){
		return warUI;
	}
	
	public void stop() {
		System.exit(1);
	}
}