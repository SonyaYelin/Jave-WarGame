package ui;

import java.util.Scanner;
import java.util.Vector;

import mvc.WarController;
import mvc.WarUIEventsListener;
import program.IConstants.LauncherDestructorType;

public class ConsoleView implements WarUI {

	static Scanner s = new Scanner (System.in);

	private Vector<WarUIEventsListener>		allListeners  = new Vector<WarUIEventsListener>();

	public void init(){
		WarController controller = new WarController();
		controller.addView(this);

		showReadFromConfigMenu();
		
		showConsoleMenu();
	}
	
	public void registerListener(WarUIEventsListener listener) {
		allListeners.add(listener);
	}
	
	public void showConsoleMenu( ) {
		boolean exit = false;
		int ans;

		System.out.println(MAIN_MENU_STR);
			
		while( !exit ){
			ans = s.nextInt();
			switch( ans ) {
			case ADD_LAUNCHER_DESTRUCTOR:
				addLauncherDestructorMenu();
				break;
			case ADD_MISSILE_DESTRUCTOR:
				addMissileDestructorMenu();
				break;
			case ADD_LAUNCHER:
				addLauncherMenu();
				break;
			case LAUNCH_MISSILE:
				launchMissileMenu();
				break;
			case DESTRUCT_MISSILE:	
				destructMissileMenu();
				break;
			case DESTRUCT_LAUNCHER:
				destructLauncherMenu();
				break;
			case SHOW_STATISTICS:
				initiateStatistics(false);
				break;
			case EXIT:
				initiateExit();
				exit = true;
				break;
			}
		}
		s.close();	
	}
		
	private void addLauncherMenu(){
		System.out.println(LAUNCHER_ID);
		String id = s.next();
		System.out.println(ENTER_IS_LAUNCHER_HIDDEN);
		int hidden = s.nextInt();
		boolean isHidden = false;
		if ( hidden == YES )
			isHidden = true;
			
		initiateAddMissileLauncher(id, isHidden);
	}
		
	private void addMissileDestructorMenu(){
		System.out.println(DESTRUCTOR_ID);
		String id = s.next();
		
		initiateAddMissileDestructor(id);
	}
	
	private void addLauncherDestructorMenu(){
		System.out.println(DESTRUCTOR_ID);
		String id = s.next();
		System.out.println(TYPE);
		LauncherDestructorType type = LauncherDestructorType.SHIP;
		String strType = s.next();
		try {
			ValidInput.IsInteger(strType);
		} catch (Exception e) {
			showErrorMsg(e.getMessage());
			return;
		}
		if ( Integer.parseInt(strType) != SHIP_IDX )
			type = LauncherDestructorType.PLANE;
		
		initiateAddLauncherDestructor(id, type);
	}
	
	private void launchMissileMenu(){
		System.out.println(LAUNCHER_ID);
		String launcherID = s.next();
		System.out.println(MISSILE_ID);
		String missileID= s.next();
		System.out.println(DESTINATION);
		String destination = s.next();
		System.out.println(POTENTIAL_DAMAGE);
		String strPotentialDamage = s.next();
		System.out.println(FLY_TIME);
		String strFlyTime = s.next();
		
		try {
			ValidInput.IsInteger(strPotentialDamage);
			ValidInput.IsInteger(strFlyTime);
		} catch (Exception e) {
			showErrorMsg(e.getMessage());
			return;
		}
			
		initiateMissileLaunch(launcherID, missileID, Integer.parseInt(strPotentialDamage), destination, Integer.parseInt(strFlyTime));
	}

	private void destructMissileMenu(){
		System.out.println(DESTRUCTOR_ID);
		String destructorID = s.next();
		System.out.println(MISSILE_ID);
		String missileID= s.next();
			
		initiateDestructMissile(destructorID, missileID);
	}
	
	private void destructLauncherMenu(){
		System.out.println(DESTRUCTOR_ID);
		String destructorID = s.next();
		System.out.println(LAUNCHER_ID);
		String launcherID= s.next();
			
		initiateDestructLauncher(destructorID, launcherID);
	}
	
	public void showReadFromConfigMenu(){			
		System.out.println(LOAD_FROM_CONFIG + YES_NO);
		if ( s.nextInt() == YES )
			initiateReadFromConfig();
		initiateStartWar();
	}

	private void showErrorMsg(String msg){
		System.out.println(msg);
	}
	
	//initiate
	@Override
	public void initiateReadFromConfig(){
		fireReadFromConfigEvent();
	}
	
	@Override
	public void initiateAddMissileLauncher(String id, boolean isHidden) {
			fireAddMissileLauncherEvent(id, isHidden);
	}

	@Override
	public void initiateAddMissileDestructor(String id) {
			fireAddMissileDestructorEvent(id);
	}

	@Override
	public void initiateAddLauncherDestructor(String id, LauncherDestructorType type) {
		fireAddLauncherDestructorEvent(id, type);	
	}

	@Override
	public void initiateMissileLaunch(String launcherID, String missileID, int potentialDamage, String destination,
				int flyTime) {
		fireLaunchMissileEvent(launcherID, missileID, potentialDamage, destination, flyTime);
	}

	@Override
	public void initiateDestructLauncher(String destructorID, String launcherID) {
		fireDestructLauncherEvent(destructorID, launcherID);
	}

	@Override
	public void initiateDestructMissile(String destructorID, String missileID) {
		fireDestructMissileEvent(destructorID, missileID);
	}

	@Override
	public void initiateStatistics(boolean exit) {
		fireStatisticsEvent(exit);
	}

	@Override
	public void initiateExit() {
		fireExitEvent();
	}

		
	//show	
	@Override
	public void showAddMissileLauncher(String id, boolean isHidden) {
		System.out.println( "new missile-launcher: #" + id +", hidden: " + isHidden );
	}

	@Override
	public void showAddMissileDestructor(String id) {
		System.out.println( "new missile-destructor: #" + id );
	}

	@Override
	public void showAddLauncherDestructor(String id, LauncherDestructorType type) {
		System.out.println( "new launcher-destrctor: #" + id + ", type: " + type );
	}

	@Override
	public void showStartMissileLaunch(String launcherID, String missileID, int flyTime, String destination) {
		System.out.println("start missile-launch:\n		launcher: #" + launcherID + ", missile: #" + missileID + "\n"
							+ "		destination: " + destination + " flyTime: " + flyTime);	
	}

	@Override
	public void showEndMissileLaunch(String launcherID, String missileID, int damage, String destination) {
		System.out.println("end of missile-launch:\n		launcher: #" + launcherID + ", missile: #" + missileID + "\n"
				+ "		destination " + destination + " damage: " + damage);		
	}

	public void showLauncherStateChanged(String launcherID, boolean isHidden){
		System.out.println("launcher #: " + launcherID + " changed state -> isHidden: " + isHidden);
	}
	
	public void showStartMissileDestruct(String destructorID, String missileID, int duration){
		System.out.println("start missile-destruct:\n		destructor: #" + destructorID + ", missile: #" + missileID);
	}
	
	public void showEndMissileDestruct(String destructorID, String missileID, boolean isDestructed){
		
		System.out.println("end missile-destruct:\n		desructor: #" + destructorID + ", missile: #" + missileID
				+ " success: " + isDestructed );		
	}

	public void showStartLuncherDestruct(String destructorID, String launcherID, int duration){
		System.out.println("start launcher-destruct:\n		destructor: #" + destructorID + ", launcher: #" + launcherID);

	}
	
	public void showEndLuncherDestruct(String destructorID, String launcherID, boolean isDestructed){
		System.out.println("end launcher-destruct:\n		desructor: #" + destructorID + ", launcher: #" + launcherID
				+ " success: " + isDestructed );		
	}

	public void showStatistics(int totalDamage, int launchedMissiles, int hits, int destructedMissiles, int destructedLaunchers, boolean exit) {
		String s = ENTER+ TOTAL_DAMAGE + totalDamage
				+ ENTER + LAUNCHED_MISSILES + launchedMissiles
				+ ENTER + HITS + hits
				+ ENTER + DESTRUCTED_MISSILES + destructedMissiles
				+ ENTER + DESTRUCTED_LAUNCHERS + destructedLaunchers;
		System.out.println(s);
	}

	public void showExit() {
		initiateStatistics(true);
		System.out.println(BYE);		
	}
		
	
	//fire to controller	
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
		
	private void fireLaunchMissileEvent(String launcherID, String missileID, int potentialDamage, String destination, int flyTime) {
		for (WarUIEventsListener l : allListeners)
			l.launchMissileFromUI(launcherID, missileID, potentialDamage, destination, flyTime);
	}

	private void fireDestructMissileEvent(String destructorID, String missileID) {
		for (WarUIEventsListener l : allListeners)
			l.destructMissileFromUI(destructorID, missileID);
	}
		
	private void fireDestructLauncherEvent(String destructorID,String launcherID) {
		for (WarUIEventsListener l : allListeners)
			l.destructLuncherFromUI(destructorID,launcherID);
	}

	private void fireStatisticsEvent(boolean exit) {
		for (WarUIEventsListener l : allListeners)
			l.statisticsFromUI(exit);
	}

	private void fireExitEvent() {
		for (WarUIEventsListener l : allListeners)
			l.exitFromUI();
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