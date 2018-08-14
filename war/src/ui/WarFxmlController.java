package ui;

import java.util.Optional;
import java.util.Random;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import program.IConstants.LauncherDestructorType;

public class WarFxmlController implements Initializable, UIConstants {

	@FXML private BorderPane	border_pane;
	@FXML private Pane 			war_pane;
	@FXML private GridPane 		statistics;

	public void initialize(java.net.URL arg0, ResourceBundle arg1) {

	}
	
	// handle actions
	@FXML
	private void handleAddLauncherAction(final ActionEvent event) {
		TextInputDialog dialog = new TextInputDialog("");
		dialog.setTitle(STR_ADD_MISSILE_LAUNCHER);
		dialog.setHeaderText(null);
		dialog.setContentText(LAUNCHER_ID);
		boolean isHidden = new Random().nextBoolean();

		Optional<String> id = dialog.showAndWait(); 
		if (id.isPresent()){
			try {
				ValidInput.IsInputPresent(id.get());
			} catch (Exception e) {
				showErrorMsg(e.getMessage());
				return;
			}
			
			App.getWarPane().initiateAddMissileLauncher(id.get(), isHidden);
		}
	}

	@FXML
	private void handleAddMissileDestructorAction(final ActionEvent event) {
		TextInputDialog dialog = new TextInputDialog("");
		dialog.setTitle(STR_ADD_MISSILE_DESTRUCTOR);
		dialog.setHeaderText(null);
		dialog.setContentText(DESTRUCTOR_ID);

		Optional<String> id = dialog.showAndWait();
		if (id.isPresent()){
			try {
				ValidInput.IsInputPresent(id.get());
			} catch (Exception e) {
				showErrorMsg(e.getMessage());
				return;
			}
			App.getWarPane().initiateAddMissileDestructor(id.get());
		}
	}

	@FXML
	private void handleAddLauncherDestructorAction(final ActionEvent event) {

		TextInputDialog dialog = new TextInputDialog("");
		dialog.setHeaderText(null);
		dialog.setTitle(STR_ADD_LAUNCHER_DESTRUCTOR);

		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 150, 10, 10));

		TextField destructorID = new TextField();
		ToggleGroup group = new ToggleGroup();

		RadioButton shipBtn = new RadioButton(SHIP);
		shipBtn.setToggleGroup(group);
		shipBtn.setSelected(true);
		RadioButton planeBtn = new RadioButton(PLANE);
		planeBtn.setToggleGroup(group);

		grid.add(new Label(DESTRUCTOR_ID), 0, 0);
		grid.add(destructorID, 1, 0);
		grid.add(shipBtn, 0, 1);
		grid.add(planeBtn, 1, 1);

		dialog.getDialogPane().setContent(grid);

		Optional<String> op = dialog.showAndWait();
		if (op.isPresent()) {
			String id = destructorID.getText();
			try {
				ValidInput.IsInputPresent(id);
			} catch (Exception e) {
				showErrorMsg(e.getMessage());
				return;
			}
			
			LauncherDestructorType type = LauncherDestructorType.SHIP;			
			if (group.getSelectedToggle().equals(planeBtn))
				type = LauncherDestructorType.PLANE;
			App.getWarPane().initiateAddLauncherDestructor(id, type);
		}
	}

	@FXML
	private void handleLaunchMissileAction(final ActionEvent event) {
		TextInputDialog dialog = new TextInputDialog("");
		dialog.setHeaderText(null);
		dialog.setTitle(STR_LAUNCH_MISSILE);

		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 150, 10, 10));

		TextField tfLauncherID = new TextField();
		TextField tfMissileID = new TextField();
		TextField tfDestination = new TextField();
		TextField tfFlyTime = new TextField();
		TextField tfDamage = new TextField();

		grid.add(new Label(LAUNCHER_ID), 0, 0);
		grid.add(tfLauncherID, 1, 0);
		grid.add(new Label(MISSILE_ID), 0, 1);
		grid.add(tfMissileID, 1, 1);
		grid.add(new Label(DESTINATION), 0, 2);
		grid.add(tfDestination, 1, 2);
		grid.add(new Label(FLY_TIME), 0, 3);
		grid.add(tfFlyTime, 1, 3);
		grid.add(new Label(POTENTIAL_DAMAGE), 0, 4);
		grid.add(tfDamage, 1, 4);
		dialog.getDialogPane().setContent(grid);
		Optional<String> idOp = dialog.showAndWait();
		if (idOp.isPresent()) {
			String launcherID = tfLauncherID.getText();
			String missileID = tfMissileID.getText();
			String dest = tfDestination.getText();
			String strDamage = tfDamage.getText();
			String strFlyTime = tfFlyTime.getText();
			try {
				ValidInput.IsInputPresent(launcherID);
				ValidInput.IsInputPresent(missileID);
				ValidInput.IsInputPresent(dest);
				ValidInput.IsInteger(strDamage);
				ValidInput.IsInteger(strFlyTime);

			} catch (Exception e) {
				showErrorMsg(e.getMessage());
				return;
			}
			
			App.getWarPane().initiateMissileLaunch(launcherID, missileID, Integer.parseInt(strDamage),
													dest, Integer.parseInt(strFlyTime));
		}
	}

	@FXML
	private void handleDestructMissileAction(final ActionEvent event) {
		TextInputDialog dialog = new TextInputDialog("");
		dialog.setHeaderText(null);
		dialog.setTitle(STR_DESTRUCT_MISSILE);

		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 150, 10, 10));

		TextField tfDestructorID = new TextField();
		TextField tfMissileID = new TextField();

		grid.add(new Label(DESTRUCTOR_ID), 0, 0);
		grid.add(tfDestructorID, 1, 0);
		grid.add(new Label(MISSILE_ID), 0, 1);
		grid.add(tfMissileID, 1, 1);
		dialog.getDialogPane().setContent(grid);

		Optional<String> op = dialog.showAndWait();
		if (op.isPresent()){
			String destructorID = tfDestructorID.getText();
			String missileID =  tfMissileID.getText();
			try {
				ValidInput.IsInputPresent(destructorID);
				ValidInput.IsInputPresent(missileID);
			} catch (Exception e) {
				showErrorMsg(e.getMessage());
				return;
			}
			App.getWarPane().initiateDestructMissile(destructorID, missileID);
		}
	}

	@FXML
	private void handleDestructLauncherAction(final ActionEvent event) {
		TextInputDialog dialog = new TextInputDialog("");
		dialog.setHeaderText(null);
		dialog.setTitle(STR_DESTRUCT_LAUNCHER);

		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 150, 10, 10));

		TextField tfDestructorID = new TextField();
		TextField tfLauncherID = new TextField();

		grid.add(new Label(DESTRUCTOR_ID), 0, 0);
		grid.add(tfDestructorID, 1, 0);
		grid.add(new Label(LAUNCHER_ID), 0, 1);
		grid.add(tfLauncherID, 1, 1);
		dialog.getDialogPane().setContent(grid);

		Optional<String> idOp = dialog.showAndWait();
		if (idOp.isPresent()){
			String destructorID = tfDestructorID.getText();
			String launcherID =  tfLauncherID.getText();
			try {
				ValidInput.IsInputPresent(destructorID);
				ValidInput.IsInputPresent(launcherID);
			} catch (Exception e) {
				showErrorMsg(e.getMessage());
				return;
			}
			App.getWarPane().initiateDestructLauncher(destructorID, launcherID);
		}
	}

	@FXML
	private void handleStatisticsAction(final ActionEvent event) {
		App.getWarPane().initiateStatistics(false);
	}

	@FXML
	private void handleExitAction(final ActionEvent event) {
		exitApp();
	}
	
	public void exitApp() {
		App.getWarPane().initiateExit();
	}
	
	public void OnExit() {
		App.getWarPane().initiateExit();
	}

	private void showErrorMsg(String msg){
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle(ERROR);
		alert.setHeaderText(null);
		alert.setContentText(msg);
		alert.showAndWait();
	}

	//getters
	public Pane getWarPane() {
		return war_pane;
	}
	
	public GridPane getStatisticsPane() {
		return statistics;
	}
	
}