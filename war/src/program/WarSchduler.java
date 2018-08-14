package program;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import bl.MissileDestructor;
import bl.WarModel;

public class WarSchduler{

	private static WarSchduler 					warSched;

	private WarModel 							war;
	private static ScheduledExecutorService		scheduledExecutorService;
	private Map<String, Entry<String, Integer>>	missilesToDestruct;

	//static ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(5);
	final static List<MissileDestructor> launchList = new ArrayList<MissileDestructor>();

	//Static maps to hold objects for scheduling 
	final static Map<String, Integer> missiles = new LinkedHashMap<>();
	final static Map<String, Integer> missileDestructors = new LinkedHashMap<>();
	final static Map<String, Integer> launcherDestructors = new LinkedHashMap<>();
	
	static java.lang.reflect.Method method;
	
	private WarSchduler() {
		war = WarModel.getInstance();
		scheduledExecutorService = Executors.newScheduledThreadPool(5);
		missilesToDestruct = new HashMap<>();
	}

	
	public static WarSchduler getInstance() {
		if (warSched == null)
			warSched = new WarSchduler();
		return warSched;
	}

	
	public boolean startMissileLaunchInModel(String launcherID, String missileID, int flyTime, String destination) {
		if (missilesToDestruct.containsKey(missileID)) {

			Entry<String, Integer> destructorData = missilesToDestruct.get(missileID);
			String destructorID = (String) destructorData.getKey();
			int destructAfterLaunch = (int) destructorData.getValue();
			scheduleMissileDestruct(war, destructorID, missileID, destructAfterLaunch);
			
			missilesToDestruct.remove(missileID, destructorData);
			if (missilesToDestruct.isEmpty())
					return false;
		}
		return true;
	}

	
	public void setMissileDestructs( Map<String, Entry<String, Integer>> missilesToDestruct ) {
		this.missilesToDestruct.putAll(missilesToDestruct); 
	}

	
	public void scheduleMissileLaunch(WarModel war, String launcherID, int launchTime, String missileID,
			int potentialDamage, String destination, int flyTime) {
		scheduledExecutorService.schedule(new Callable<Object>() {
			public Object call() {
				war.addMissileLaunch(launcherID, missileID, potentialDamage, destination, flyTime);
				return "Complete";
			}
		}, launchTime, TimeUnit.SECONDS);
	}

	
	public void scheduleLauncherDestruct(WarModel war, String destructorID, String launcherID, int destructTime) {
		scheduledExecutorService.schedule(new Callable<Object>() {
			public Object call() {
				war.addLauncherDestruct(destructorID, launcherID);
				return "Complete";
			}
		}, destructTime, TimeUnit.SECONDS);
	}

	
	public void scheduleMissileDestruct(WarModel war, String destructorID, String missileID,
			int destructAfterLaunch) {
		scheduledExecutorService.schedule(new Callable<Object>() {
			public Object call() {
				war.addMissileDestruct(destructorID, missileID);
				return "Complete";
			}
		}, destructAfterLaunch, TimeUnit.SECONDS);
	}

	private static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
        List<Entry<K, V>> list = new ArrayList<>(map.entrySet());
        list.sort(Entry.comparingByValue());

        Map<K, V> result = new LinkedHashMap<>();
        for (Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }
}
