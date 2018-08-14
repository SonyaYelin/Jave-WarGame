package ui;

import mvc.WarUIEventsListener;
import program.IConstants.LauncherDestructorType;
   
public interface WarUI extends UIConstants{
	
	void showReadFromConfigMenu();
	void showAddMissileLauncher (String id, boolean isHidden);
	void showAddMissileDestructor(String id);
	void showAddLauncherDestructor(String id, LauncherDestructorType type);
	void showLauncherStateChanged(String launcherID, boolean isHidden);
	
	void showStartMissileLaunch(String launcherID, String missileID, int flyTime, String destination);
	void showEndMissileLaunch(String launcherID, String missileID, int damage, String destination);
	
	void showStartMissileDestruct(String destructorID, String missileID, int duration);
	void showEndMissileDestruct(String destructorID, String missileID, boolean isDestructed);

	void showStartLuncherDestruct(String destructorID, String launcherID, int duration);
	void showEndLuncherDestruct(String destructorID, String launcherID, boolean isDestructed);
	
	void showStatistics(int totalDamage, int launchedMissiles, int hits, int destructedMissiles, int destructedLaunchers, boolean exit);
	void showExit();
	
	void initiateAddMissileLauncher(String id, boolean isHidden);
	void initiateAddMissileDestructor(String id);
	void initiateAddLauncherDestructor(String id, LauncherDestructorType type) ;
	
	void initiateMissileLaunch(String launcherID,String missileID, int potentialDamage, String destination, int flyTime);
	void initiateDestructLauncher(String destructorID, String launcherID);
	void initiateDestructMissile(String destructorID, String missileID);

	void initiateStatistics(boolean exit);
	void initiateExit();

	void initiateReadFromConfig();
	void initiateStartWar();

	void registerListener(WarUIEventsListener listener);
	
	//void showFailedAdding..
}