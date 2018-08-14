package Logging;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import bl.LauncherDestructor;
import bl.MissileDestructor;
import bl.MissileLauncher;
import bl.WarObject;

public class WarLogsGenerator{

	private static WarLogsGenerator 	logsGen;
	private static Logger				logger;

	private WarLogsGenerator(){
		logger = Logger.getLogger("warLogger");
		logger.setUseParentHandlers(false);

		try {
			FileHandler warHandler = new FileHandler("war.txt");
			warHandler.setFormatter(new WarFormatter()); 
			logger.addHandler(warHandler);
		} catch (SecurityException e) { e.printStackTrace(); 
		} catch (IOException e) { e.printStackTrace(); }
	}
	
	public static WarLogsGenerator getInstance(){
		if (logsGen == null)
			logsGen = new WarLogsGenerator();
		return logsGen;
	}
	
	public synchronized void endLaunch(String missileID, String launcherID, String destination, int damage, int flyTime, boolean success, MissileLauncher l, int launchTime){
		String msg = "End of missile-launch\n" +"launcher: #" + launcherID + " missile: #" + missileID + " launchTime: " +launchTime;
		if ( success )
			msg += ", destination: " + destination + ", landTime: " + (flyTime + launchTime) + ", demage: " +  damage + "\n";
		else
			msg += "destructTime: " + (flyTime + launchTime) + "\n";
		
		logger.log(Level.INFO, msg, l);
	}
	
	public synchronized void afterLauncherDestruct(LauncherDestructor d, String launcherID , boolean success){
		String msg = "End of launcher #" + launcherID + " destruct\n" + "success: " + success + "\n";
	
		logger.log(Level.INFO, msg, d);
	}
	
	public synchronized void afterMissileDestruct(MissileDestructor d, String missileID , boolean success, int damage){
		String msg = "End of missile #" + missileID + "destruct\n" + "success: " + success + "\n";
		if ( !success )
			msg += "damage: " + damage;
		logger.log(Level.INFO, msg, d);
	}
	
	public void addWarObject(WarObject o){
		addHandler(o, o.getClass().getSimpleName(), o.getID());	
	}

	private void addHandler(Object obj, String className, String id){
		FileHandler launcherHandler;
		try {
			launcherHandler = new FileHandler(className +"_" + id + ".txt");
			launcherHandler.setFilter(new ObjectFilter(obj));
			launcherHandler.setFormatter(new WarFormatter()); 
			logger.addHandler(launcherHandler);
			
		} catch (SecurityException e) { e.printStackTrace(); 
		} catch (IOException e) { e.printStackTrace(); }
	}
	
	public void close() {
		for( Handler h: logger.getHandlers() )
    	    h.close();
	}
}
