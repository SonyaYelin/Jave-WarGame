package ui;

import java.util.Optional;
import java.util.Random;
import java.util.Vector;

import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import mvc.WarUIEventsListener;
import program.IConstants.LauncherDestructorType;

public class War implements WarUI {

	private Pane 						pane;
	private GridPane 					statisticsPane;
	
	private int 						leftX;
	private int 						leftY;
	private int 						rightX;
	private int 						rightY;

	private int 						launchedMissiles;
	private int 						hits;
	private int 						destructedMissiles;
	private int 						destructedLaunchers;
	private int 						damage;
	
	private Label 						lblDamage = new Label();
	private Label 						lblLaunchedMissiles = new Label();
	private Label 						lblHits = new Label();
	private Label 						lblDestructedMissiles = new Label();
	private Label 						lblDestructedLaunchers = new Label();
	
	private Vector<WarObject> 			allStaticObjects = new Vector<WarObject>();
	private Vector<WarMovingObject> 	allMovingObjects = new Vector<WarMovingObject>();

	private Vector<WarUIEventsListener>	allListeners = new Vector<WarUIEventsListener>();

	
	public War(Pane pane, GridPane statisticsPane) {
		this.pane = pane;
		this.statisticsPane = statisticsPane;
		initStatisticsPane();
		
		leftX = START_IDX;
		leftY = START_IDX;
		rightX = (int) pane.getWidth() - WAR_OBJECT_WIDTH - START_IDX;
		rightY = START_IDX;
	}
	
	private void initStatisticsPane(){
		statisticsPane.setHgap(5);
		statisticsPane.setVgap(5);
		updateStatistics();	
		
		statisticsPane.add(lblDamage, 0, 0);
		statisticsPane.add(lblLaunchedMissiles, 1, 0);
		statisticsPane.add(lblHits, 2, 0);
		statisticsPane.add(lblDestructedMissiles, 3, 0);
		statisticsPane.add(lblDestructedLaunchers, 4, 0);
	}
	
	private void updateStatistics(){
		lblDamage.setText(TOTAL_DAMAGE + damage);
		lblLaunchedMissiles.setText(LAUNCHED_MISSILES + launchedMissiles);
		lblHits.setText(HITS + hits);
		lblDestructedMissiles.setText(DESTRUCTED_MISSILES + destructedMissiles);
		lblDestructedLaunchers.setText(DESTRUCTED_LAUNCHERS + destructedLaunchers);
	}
	
	
	// show
	public void showAddMissileLauncher(String id, boolean isHidden) {
		Platform.runLater(new Runnable() {
			public void run() {
				WarObject launcher = new WarObject(id, LAUNCHER_IMAGE_PATH, isHidden);
				allStaticObjects.add(launcher);
				setObjectOnLeft(launcher);
			}
		});
	}

	public void showAddMissileDestructor(String id) {
		Platform.runLater(new Runnable() {
			public void run() {
				WarObject destructor = new WarObject(id, MISSILE_DESTRUCTOR_IMAGE_PATH, false);
				allStaticObjects.add(destructor);
				setObjectOnRight(destructor);
			}
		});
	}

	public void showAddLauncherDestructor(String id, LauncherDestructorType type) {
		Platform.runLater(new Runnable() {
			public void run() {
				String path = getLauncherDestructorPath(type);
				WarObject destructor = new WarObject(id, path, false);
				allStaticObjects.add(destructor);
				setObjectOnRight(destructor);
			}
		});
	}

	public void showStartMissileLaunch(String launcherID, String missileID, int flyTime, String destination) {
		Platform.runLater(new Runnable() {
			public void run() {
				launchedMissiles++;
				updateStatistics();	
				
				WarObject launcher = getStaticObjectById(launcherID);
				double x0 = launcher.getX0();
				double y0 = launcher.getY0();

				double targetX = new Random().nextInt((int) (pane.getWidth() - WAR_OBJECT_WIDTH));
				double targetY = WAR_PANE_HIGHT - WAR_OBJECT_HIGHT;

				WarMovingObject missile = new WarMovingObject(missileID, MISSILE_IMAGE_PATH, false, x0, y0, targetX,
						targetY, flyTime);
				
				synchronized (allMovingObjects) {
					allMovingObjects.add(missile);
				}
				
				setMovingObject( missile , targetX, targetY, flyTime);
				
			}
		});
	}

	public void showEndMissileLaunch(String launcherID, String missileID, int damage, String destination) {
		Platform.runLater(new Runnable() {
			public void run() {
				//update data
				War.this.damage += damage;
				if ( damage > 0 )
					hits++;
				updateStatistics();
			}});
	}

	public void showLauncherStateChanged(String launcherID, boolean isHidden){
		Platform.runLater(new Runnable() {
			public void run() {
				WarObject launcher = getStaticObjectById(launcherID);
				if ( launcher != null )
					launcher.setHidden(isHidden);
			}
		});
	}
	
	public void showStartMissileDestruct(String destructorID, String missileID, int duration) {
		Platform.runLater(new Runnable() {
			public void run() {
				WarObject destructor = getStaticObjectById(destructorID);
				if (destructor == null)
					return;
				double x0 = destructor.getX0();
				double y0 = destructor.getY0();

				WarMovingObject missile = getMovingObjectById(missileID);
				if (missile == null)
					return;
				double targetX = missile.getTranslateX() + missile.getvX() * duration;
				if ( targetX > pane.getWidth() || targetX < 0)
					targetX =  pane.getWidth() / 2;
				double targetY = missile.getTranslateY() + missile.getvY() * duration;
				if ( targetY < 0 || targetY > pane.getHeight())
					targetY =  pane.getHeight() / 2;
				
				WarMovingObject dMissile = new WarMovingObject(destructorID + missileID, D_MISSILE_IMAGE_PATH, false,
						x0, y0, targetX, targetY, duration);
				synchronized (allMovingObjects) {
					allMovingObjects.add(dMissile);
				}
				setMovingObject( dMissile , targetX, targetY, duration);
			}
		});
	}

	public void showEndMissileDestruct(String destructorID, String missileID, boolean isDestructed) {
		Platform.runLater(new Runnable() {
			public void run() {	
				if (!isDestructed) 
					return;
				destructedMissiles++;
				updateStatistics();
				
				WarMovingObject missile = getMovingObjectById(missileID);
				if (missile == null)
					return;
				removeMovingObject(missile);
			}
		});
	}

	public void showStartLuncherDestruct(String destructorID, String launcherID, int duration) {
		Platform.runLater(new Runnable() {
			public void run() {
				WarObject destructor = getStaticObjectById(destructorID);
				if (destructor == null)
					return;
				double x0 = destructor.getX0();
				double y0 = destructor.getY0();

				WarObject launcher = getStaticObjectById(launcherID);
				if (launcher == null)
					return;
				double targetX = launcher.getX0();
				double targetY = launcher.getY0();

				WarMovingObject dMissile = new WarMovingObject(destructorID + launcherID, D_MISSILE_IMAGE_PATH, false,
						x0, y0, targetX, targetY, duration);
				synchronized (allMovingObjects) {
					allMovingObjects.add(dMissile);
				}
				setMovingObject( dMissile , targetX, targetY, duration);
			}
		});
	}

	public void showEndLuncherDestruct(String destructorID, String launcherID, boolean isDestructed) {
		Platform.runLater(new Runnable() {
			public void run() {;
				WarObject launcher = getStaticObjectById(launcherID);
				if (launcher == null)
					return;
				if (isDestructed){
					destructedLaunchers++;
					updateStatistics();
					removeStaticObject(launcher);
				}
			}
		});
	}

	public void showStatistics(int totalDamage, int launchedMissiles, int hits, int destructedMissiles, int destructedLaunchers, boolean exit) {
		Platform.runLater(new Runnable(){
			public void run() {
				String s = ENTER + TOTAL_DAMAGE + totalDamage + ENTER + LAUNCHED_MISSILES + launchedMissiles + ENTER + HITS + hits + ENTER + 
						DESTRUCTED_MISSILES + destructedMissiles + ENTER + DESTRUCTED_LAUNCHERS + destructedLaunchers;

				Alert dialog = new Alert(AlertType.INFORMATION);
				dialog.setTitle(STATISTICS);
				dialog.setHeaderText(null);
				if ( exit )
					s = WAR_IS_OVER + ENTER + s + ENTER + BYE;
				dialog.setContentText( s);
				dialog.showAndWait();
				if ( dialog.getResult() == ButtonType.OK && exit )
					Platform.exit();
			}});
	}

	public void showExit() {
		initiateStatistics(true);
	}

	public void showReadFromConfigMenu(){			
		Alert alert = new Alert(AlertType.CONFIRMATION, LOAD_FROM_CONFIG, ButtonType.YES, ButtonType.NO);
		alert.showAndWait();

		if (alert.getResult() == ButtonType.YES) {
			initiateReadFromConfig();
		}
		initiateStartWar();
	}
	
	
	// initiate
	public void initiateAddMissileLauncher(String id, boolean isHidden) {
		fireAddMissileLauncherEvent(id, isHidden);
	}

	public void initiateAddMissileDestructor(String id) {
		fireAddMissileDestructorEvent(id);
	}

	public void initiateAddLauncherDestructor(String id, LauncherDestructorType type) {
		fireAddLauncherDestructorEvent(id, type);
	}

	public void initiateMissileLaunch(String launcherID, String missileID, int potentialDamage, String destination,
			int flyTime) {
		fireLaunchMissileEvent(launcherID, missileID, potentialDamage, destination, flyTime);
	}

	public void initiateDestructLauncher(String destructorID, String launcherID) {
		fireDestructLauncherEvent(destructorID, launcherID);
	}

	public void initiateDestructMissile(String destructorID, String missileID) {
		fireDestructMissileEvent(destructorID, missileID);
	}

	public void initiateStatistics(boolean exit) {
		fireStatisticsEvent(exit);
	}

	public void initiateExit() {
		fireExitEvent();
	}

	public void initiateReadFromConfig() {
		fireReadFromConfigEvent();
	}

	
	// listener
	public void registerListener(WarUIEventsListener listener) {
		allListeners.add(listener);
	}

	
	// fire to controller
	private void fireReadFromConfigEvent() {
		for (WarUIEventsListener l : allListeners)
			l.readFromConfigFromUI();
	}
	
	private void fireAddMissileLauncherEvent(String id, boolean isHidden) {
		for (WarUIEventsListener l : allListeners)
			l.addMissileLauncherFromUI(id, isHidden);
	}

	private void fireAddMissileDestructorEvent(String id) {
		for (WarUIEventsListener l : allListeners)
			l.addMissileDestructorFromUI(id);
	}

	private void fireAddLauncherDestructorEvent(String id, LauncherDestructorType type) {
		for (WarUIEventsListener l : allListeners)
			l.addLauncherDestructorFromUI(id, type);
	}

	private void fireLaunchMissileEvent(String launcherID, String missileID, int potentialDamage, String destination,
			int flyTime) {
		for (WarUIEventsListener l : allListeners)
			l.launchMissileFromUI(launcherID, missileID, potentialDamage, destination, flyTime);
	}

	private void fireDestructMissileEvent(String destructorID, String missileID) {
		for (WarUIEventsListener l : allListeners)
			l.destructMissileFromUI(destructorID, missileID);
	}

	private void fireDestructLauncherEvent(String destructorID, String launcherID) {
		for (WarUIEventsListener l : allListeners)
			l.destructLuncherFromUI(destructorID, launcherID);
	}

	private void fireStatisticsEvent(boolean exit) {
		for (WarUIEventsListener l : allListeners)
			l.statisticsFromUI(exit);
	}

	private void fireExitEvent() {
		for (WarUIEventsListener l : allListeners)
			l.exitFromUI();
	}

	
	//general
	private void removeStaticObject(WarObject o) {
		if ( allMovingObjects.contains(o))
			allStaticObjects.remove(o);
		
		if ( pane.getChildren().contains(o) )
			pane.getChildren().remove(o);
	}
	
	private void removeMovingObject(WarMovingObject o) {
		if ( allMovingObjects.contains(o))
			allMovingObjects.remove(o);
		
		if ( pane.getChildren().contains(o) )
			pane.getChildren().remove(o);
	}

	private void setObjectOnLeft(WarObject o){
		o.setTranslateX(leftX);
		o.setTranslateY(leftY);
		o.setX0(leftX);
		o.setY0(leftY);
		pane.getChildren().add(o);

		leftY += WAR_OBJECT_HIGHT;
		if (leftY >= pane.getHeight()) {
			leftY = 0;
			leftX += WAR_OBJECT_WIDTH;
		}
	}
	
	private void setObjectOnRight(WarObject o){
		o.setTranslateX(rightX);
		o.setTranslateY(rightY);
		o.setX0(rightX);
		o.setY0(rightY);
		pane.getChildren().add(o);

		rightY += WAR_OBJECT_HIGHT;	
		if (rightY >= pane.getHeight()) {
			rightY = 0;
			rightX += WAR_OBJECT_WIDTH;
		}
	}
	
	private void setMovingObject(WarMovingObject o, double x, double y, int time){
		o.setTranslateX(o.getX0());
		o.setTranslateY(o.getY0());
		TranslateTransition t = new TranslateTransition();
		t.setDuration(Duration.seconds(time));
		t.setToX(x);
		t.setToY(y);
		t.setNode(o);
		t.play();
		t.setOnFinished(new EventHandler(){
			public void handle(Event arg0) {
				removeMovingObject(o);
			}});				
		pane.getChildren().add(o);
	}
	
	
	// getters
	private WarObject getStaticObjectById(String id) {
		Optional<WarObject> matchingObject = allStaticObjects.stream().filter(o -> o.getID().equals(id)).findFirst();
		return matchingObject.orElse(null);
	}

	private WarMovingObject getMovingObjectById(String id) {
		Optional<WarMovingObject> matchingObject = allMovingObjects.stream().filter(l -> l.getID().equals(id))
				.findFirst();
		return matchingObject.orElse(null);
	}

	private String getLauncherDestructorPath(LauncherDestructorType type) {
		switch (type) {
		case SHIP:
			return SHIP_IMAGE_PATH;
		case PLANE:
			return PLANE_IMAGE_PATH;
		default:
			return SHIP_IMAGE_PATH;
		}
	}

	@Override
	public void initiateStartWar() {
		fireStartWar();
	}
	
	public void fireStartWar() {
		for (WarUIEventsListener l : allListeners)
			l.startWarFromUI();
	}

}
