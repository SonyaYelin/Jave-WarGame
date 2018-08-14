package mvc;

import program.IConstants.LauncherDestructorType;

public interface WarUIEventsListener {
	
	void addMissileLauncherFromUI(String id, boolean isHidden);
	void addMissileDestructorFromUI(String id);
	void addLauncherDestructorFromUI(String id, LauncherDestructorType type);
	
	void launchMissileFromUI(String launcherID, String missileID,  int potentialDamage, String destination, int flyTime);
	void destructLuncherFromUI(String destructorID, String launcherID);
	void destructMissileFromUI(String destructorID,String missileID);
	
	void statisticsFromUI(boolean exit);
	void exitFromUI();
	void readFromConfigFromUI();
	void startWarFromUI();
}