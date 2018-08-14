package bl;

import mvc.WarModelEventsListener;
import program.IConstants.LauncherDestructorType;

public interface IWar {
	void addMissileLauncher(String id, boolean isHidden);
	void addMissileDestructor(String id);
	void addLauncherDestructor(String destructorID, LauncherDestructorType type);

	void addMissileDestruct(String destructorID, String missileID);
	void addLauncherDestruct(String destructorID, String launcherID);
	
	void launchStarted(String launcherID, String missileID, String destination, int flyTime);
	void launchEnded(MissileLauncher l,String missileID, boolean success, String destination, int damage, int flightTime);
	
	void launcherStateChanged(String launcherID, boolean isHidden);
	
	void missileDestructStarted(String destructorID, String MissileID, int duration);
	void missileDestructEnded(String destructorID, String MissileID, boolean isDestructed);
	
	void launcherDestructStarted(String destructorID, String launcherID,  int duration);
	void launcherDestructEnded(String destructorID, String launcherID, boolean isDestructed);
	void statistics(boolean exit);
	void exit();
	
	void registerListener(WarModelEventsListener listener);
}
