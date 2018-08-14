package bl;

import java.util.Optional;
import java.util.Vector;

import Logging.WarLogsGenerator;
import mvc.WarModelEventsListener;
import program.IConstants;

public class WarModel implements IWar, BLConstants, IConstants {

	private static WarModel 				war;

	private WarTime							warTime;
	
	private int 							hits;
	private int 							totalDamage;
	private int 							launchedMissiles;
	private int 							destructedMissiles;
	private int 							destructedLaunchers;
	
	private Vector<Missile>					allMissiles = new Vector<>();
	private Vector<MissileLauncher>			allLaunchers = new Vector<>();
	private Vector<MissileDestructor>		allMissileDestructors = new Vector<>();
	private Vector<LauncherDestructor>		allLauncherDestructors = new Vector<>();
		
	private Vector<WarModelEventsListener>	allListeners = new Vector<WarModelEventsListener>();

	
	private WarModel() {		
		
	}

	public static WarModel getInstance(){
		if (war == null)
			war = new WarModel();
		return war;
	}

	public void startWar() {
		warTime = new WarTime();
		Thread t = new Thread(warTime);
		t.start();
	}
	
	//add
	public void addMissileLauncher(String id, boolean isHidden) {
		MissileLauncher l;
		if ( isHidden )
			l = new HiddenMissileLauncher(id, war);
		else
			l = new MissileLauncher(id, war);
		
		allLaunchers.add(l);
		Thread launcherThread = new Thread(l);
		launcherThread.start();	
		
		fireAddMissileLuncherEvent(id, isHidden);	
	}

	public void addMissileDestructor(String id) {
		MissileDestructor d = new MissileDestructor(id, war);
		allMissileDestructors.add(d);
		Thread missileDestructorThread = new Thread(d);
		missileDestructorThread.start();	
		
		fireAddMissileDestructorEvent(id);
	}

	public void addLauncherDestructor(String id, LauncherDestructorType type) {
		LauncherDestructor d = new LauncherDestructor(id, type, war);
		allLauncherDestructors.add(d);
		Thread launcherDestructorThread = new Thread(d);
		launcherDestructorThread.start();	
		
		fireAddLauncherDestructorEvent(id, type);
	}

	
	// launch
	public void addMissileLaunch(String launcherID, String missileID, int potentialDamage, String destination,
			int flyTime) {
		MissileLauncher launcher = getLauncherById(launcherID);
		if ( launcher == null )
			return;
		
		Missile m = new Missile(missileID, potentialDamage, destination, flyTime, launcher);
		launcher.addMissileToLaunchQueue(m);
		allMissiles.add(m);
	}

	public synchronized void launchStarted(String launcherID, String missileID, String destination, int flyTime) {
		launchedMissiles++;
		fireStartMissileLaunchEvent(launcherID, missileID, destination, flyTime);
	}

	public synchronized void launchEnded(MissileLauncher l, String missileID, boolean success, String destination, int damage,
			int flightTime) {
		
		if( success ){
			hits++;
			totalDamage += damage;
		}
		
		fireEndMissileLaunchEvent(l.getID(), missileID, destination, damage);
	}

	public void launcherStateChanged(String launcherID, boolean isHidden){
		fireLauncherStateChangedEvent(launcherID, isHidden);
	}
	
	// missile-destruct
	public void addMissileDestruct(String destructorID, String missileID) {
		Missile m = getMissileById(missileID);
		if (m == null)
			return;
		
		MissileDestructor destructor = getMissileDestructorById(destructorID);
		if ( destructor == null )
			return;
			
		destructor.addTargetToQueue(m);
	}

	public synchronized void missileDestructStarted(String destructorID, String missileID, int duration) {
		fireStartDestructMissileEvent(destructorID, missileID, duration);
	}

	public synchronized void missileDestructEnded(String destructorID, String missileID, boolean isDestructed) {
		if( !isDestructed )
			return;
		destructedMissiles++;
		Missile m = getMissileById(missileID);
		if ( m != null )
			allMissiles.remove(m);
		fireEndDestructMissileEvent(destructorID, missileID, isDestructed);
		
	}

	// launcher - destruct
	public void addLauncherDestruct(String destructorID, String launcherID) {
		MissileLauncher l = getLauncherById(launcherID);
		if (l == null)
			return;
		LauncherDestructor destructor = getLauncherDestructorById(destructorID);
		if ( destructor == null )
			return;
		
		destructor.addTargetToQueue(l);
	}

	public synchronized void launcherDestructStarted(String destructorID, String launcherID, int duration) {
		fireStartDestructLauncherEvent(destructorID, launcherID, duration);
	}

	public	synchronized void launcherDestructEnded(String destructorID, String launcherID, boolean isDestructed) {
		if ( isDestructed ){
			destructedLaunchers++;
			MissileLauncher l = getLauncherById(launcherID);
			if ( l != null )
				allLaunchers.remove(l);
			fireEndDestructLauncherEvent(destructorID, launcherID, isDestructed);
		}
		
		if ( isWarOver() )
			exit();
	}

	public void statistics(boolean exit){					
		fireStatisticsEvent(totalDamage, launchedMissiles, hits, destructedMissiles, destructedLaunchers, exit);
	}

	public void exit() {
		endWar();
		warTime.killTimer();
		WarLogsGenerator.getInstance().close();
		fireExitEvent();
	}
	
	public void registerListener(WarModelEventsListener listener) {
		allListeners.add(listener);
	}
	
	// fire to controller
	private void fireAddMissileLuncherEvent(String id, boolean isHidden) {
		for (WarModelEventsListener l : allListeners)
			l.addMissileLauncherInModel(id, isHidden);
	}

	private void fireAddMissileDestructorEvent(String id) {
		for (WarModelEventsListener l : allListeners) {
			l.addMissileDestructorInModel(id);
		}
	}

	private void fireAddLauncherDestructorEvent(String id, LauncherDestructorType type) {
		for (WarModelEventsListener l : allListeners) {
			l.addLauncherDestructorInModel(id, type);
		}
	}

	public void fireStartMissileLaunchEvent(String launcherID, String missileID, String destination, int flyTime) {
		for (WarModelEventsListener l : allListeners) 
			l.startMissileLaunchInModel(launcherID, missileID, flyTime, destination);
	}

	public void fireEndMissileLaunchEvent(String launcherID, String missileID, String destination, int damage) {
		for (WarModelEventsListener l : allListeners) {
			l.endMissileLaunchInModel(launcherID, missileID, damage, destination);
		}
	}

	public void fireLauncherStateChangedEvent(String launcherID, boolean isHidden){
		for (WarModelEventsListener l : allListeners) {
			l.launcherStateChangedInModel(launcherID, isHidden);
		}
	}

	private void fireStartDestructMissileEvent(String destructorID, String missileID, int duration) {
		for (WarModelEventsListener l : allListeners) {
			l.startDestructMissileInModel(destructorID, missileID, duration);
		}
	}

	private void fireEndDestructMissileEvent(String destructorID, String missileID, boolean isDestructed) {
		for (WarModelEventsListener l : allListeners) {
			l.endDestructMissileInModel( destructorID, missileID, isDestructed);
		}
	}

	private void fireStartDestructLauncherEvent(String destructorID, String launcherID, int duration) {
		for (WarModelEventsListener l : allListeners) {
			l.startDestructLauncherInModel(destructorID, launcherID, duration);
		}
	}

	private void fireEndDestructLauncherEvent(String destructorID, String launcherID, boolean isDestructed) {
		for (WarModelEventsListener l : allListeners) {
			l.endDestructLauncherInModel(destructorID, launcherID, isDestructed);
		}
	}

	private void fireStatisticsEvent(int totalDamage, int launchedMissiles, int hits, int destructedMissiles, int destructedLaunchers, boolean exit) {
		for (WarModelEventsListener l : allListeners)
			l.statisticsInModel(totalDamage, launchedMissiles, hits, destructedMissiles, destructedLaunchers, exit);
	}
	
	private void fireExitEvent() {
		for (WarModelEventsListener l : allListeners) {
			l.exitInModel();
		}
	}
	
	public void endWar(){
		for ( MissileLauncher l : allLaunchers)
			l.terminate();
		for( MissileDestructor md : allMissileDestructors )
			md.terminate();
		for( LauncherDestructor ld : allLauncherDestructors )
			ld.terminate();
	}
	
	//getters
	public MissileLauncher getLauncherById(String id){
		Optional<MissileLauncher> matchingObject = allLaunchers.stream().
				filter(l -> l.getID().equals(id)).findFirst();
		return matchingObject.orElse(null);		
	}
	
	public Missile getMissileById(String id){
		Optional<Missile> matchingObject = allMissiles.stream().
				filter(m -> m.getID().equals(id)).findFirst();
		return matchingObject.orElse(null);		
	}
	
	public MissileDestructor getMissileDestructorById(String id){
		Optional<MissileDestructor> matchingObject = allMissileDestructors.stream().
				filter(d -> d.getID().equals(id)).findFirst();
		return matchingObject.orElse(null);		
	}
	
	public LauncherDestructor getLauncherDestructorById(String id){
		Optional<LauncherDestructor> matchingObject = allLauncherDestructors.stream().
				filter(d -> d.getID().equals(id) ).findFirst();
		return matchingObject.orElse(null);		
	}
	
	public boolean isWarOver(){
		for ( MissileLauncher l : allLaunchers){
			if ( !l.isDestructed() )
				return false;
		}
		return true;
	}

	public int getWarTimeInSeconds(){
		return warTime.getTime();
	}
}
