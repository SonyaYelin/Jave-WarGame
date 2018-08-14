 package mvc;

import java.util.Vector;
import bl.WarModel;
import program.IConstants;
import program.WarSchduler;
import program.JsonParser;
import ui.WarUI;


public class WarController implements WarModelEventsListener, WarUIEventsListener, IConstants{


	private WarModel 		war;
	private Vector<WarUI> 	warView = new Vector<WarUI>();
	private boolean 		config;
	
	public WarController( ) {
		this.war = WarModel.getInstance();
		war.registerListener(this);
	}
	
	public void addView( WarUI view ){
		warView.add(view);
		view.registerListener(this);
	}
	
	
	//view
	public void readFromConfigFromUI(){
		JsonParser.getInstance().readFromConfigFile(war);
		config = true;
	}
	
	public void addMissileLauncherFromUI(String id, boolean isHidden) {
		war.addMissileLauncher(id, isHidden);
	}

	public void addMissileDestructorFromUI(String id) {
		war.addMissileDestructor(id);
	}

	public void addLauncherDestructorFromUI(String id, LauncherDestructorType type) {
		war.addLauncherDestructor(id, type);
	}	

	public void launchMissileFromUI(String launcherID, String missileID,  int potentialDamage, String destination, int flyTime ) {
		war.addMissileLaunch(launcherID, missileID, potentialDamage, destination, flyTime);
	}
	
	public void destructLuncherFromUI(String destructorID, String launcherID) {
		war.addLauncherDestruct(destructorID, launcherID);
	}
	
	public void destructMissileFromUI(String destructorID,String missileID) {
		war.addMissileDestruct(destructorID, missileID);
	}
	
	public void statisticsFromUI(boolean exit) {
		war.statistics(exit);
	}
	
	public void exitFromUI() {
		war.exit();
	}
	
	
	//model
	public void addMissileLauncherInModel(String id, boolean isHidden) {
		for ( WarUI view : warView)
			view.showAddMissileLauncher(id, isHidden);
	}
	
	public void addMissileDestructorInModel(String id) {
		for ( WarUI view : warView)
			view.showAddMissileDestructor(id);
	}
	
	public void addLauncherDestructorInModel(String id, LauncherDestructorType type) {
		for ( WarUI view : warView)
			view.showAddLauncherDestructor(id, type);
	}
	
	public void startMissileLaunchInModel(String launcherID, String missileID, int flyTime, String destination) {
		for ( WarUI view : warView)
			view.showStartMissileLaunch( launcherID, missileID, flyTime, destination );
		if(config)
			config = WarSchduler.getInstance().startMissileLaunchInModel( launcherID, missileID, flyTime, destination);
	}
	
	public void endMissileLaunchInModel(String launcherID, String missileID, int damage, String destination){
		for ( WarUI view : warView)
			view.showEndMissileLaunch( launcherID, missileID, damage, destination);
	}
	
	public void launcherStateChangedInModel(String launcherID, boolean isHidden){
		for ( WarUI view : warView)
			view.showLauncherStateChanged(launcherID, isHidden);
	}
	
	public void startDestructLauncherInModel(String destructorID, String launcherID, int duration) {
		for ( WarUI view : warView)
			view.showStartLuncherDestruct(destructorID, launcherID, duration);
	}
	
	public void endDestructLauncherInModel(String destructorID, String launcherID, boolean isDestructed) {
		for ( WarUI view : warView)
			view.showEndLuncherDestruct(destructorID, launcherID, isDestructed);
	}
	
	public void startDestructMissileInModel(String destructorID, String missileID, int duration) {
		for ( WarUI view : warView)
			view.showStartMissileDestruct(destructorID, missileID, duration);
	}
	
	public void endDestructMissileInModel(String destructorID, String missileID, boolean isDestructed) {
		for ( WarUI view : warView)
			view.showEndMissileDestruct(destructorID, missileID, isDestructed);
	}
	
	public void statisticsInModel(int totalDamage, int launchedMissiles, int hits, int destructedMissiles, int destructedLaunchers, boolean exit) {
		for ( WarUI view : warView)
			view.showStatistics(totalDamage, launchedMissiles, hits, destructedMissiles, destructedLaunchers, exit);
	}

	public void exitInModel() {
		for ( WarUI view : warView) {
			view.showExit();
		}
	}

	@Override
	public void startWarFromUI() {
		war.startWar();
	}
}