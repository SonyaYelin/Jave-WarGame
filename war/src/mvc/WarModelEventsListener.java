package mvc;

import program.IConstants.LauncherDestructorType;

public interface WarModelEventsListener {

	void addMissileLauncherInModel(String id, boolean isHidden);
	void addMissileDestructorInModel(String id);
	void addLauncherDestructorInModel(String id, LauncherDestructorType type) ;
	
	void launcherStateChangedInModel(String launcherID, boolean isHidden);
	
	void startMissileLaunchInModel(String launcherID, String missileID, int flyTime, String destination);
	void endMissileLaunchInModel(String launcherID, String missileID, int damage, String destination);
	
	void startDestructLauncherInModel(String destructorID, String missileID, int duration);
	void endDestructLauncherInModel(String destructorID, String launcherID, boolean isDestructed);

	void startDestructMissileInModel(String destructorID, String launcherID, int duration);
	void endDestructMissileInModel(String destructorID, String missileID, boolean isDestructed);
	
	void statisticsInModel(int totalDamage, int launchedMissiles, int hits, int destructedMissiles, int destructedLaunchers, boolean exit);
	void exitInModel();	
}