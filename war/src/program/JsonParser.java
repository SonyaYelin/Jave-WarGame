package program;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map.Entry;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import bl.MissileDestructor;
import bl.WarModel;
import program.IConstants;
import program.IConstants.LauncherDestructorType;


public class JsonParser{
	
	private static JsonParser	parser;
	private WarSchduler 		warSched;
	
	private JsonParser(){
		warSched = WarSchduler.getInstance();
	}
	
	public static JsonParser getInstance(){
		if (parser == null)
			parser = new JsonParser();
		return parser;
	}
	
	public static Object returnSubObject(String[] objectPath){
		JSONParser parser = new JSONParser();		
		Object o = null;
		try {
			Object obj = parser.parse(new FileReader(IConstants.CONFIGURATION_FILE));
	        JSONObject jsonObject = (JSONObject) obj;
	        o = (JSONObject) jsonObject.get("war");
	        if(objectPath[0] != "")
				for(String path : objectPath){
					if(o instanceof JSONObject)
						o = ((JSONObject) o).get(path);
					else if(o instanceof JSONArray)
						//Cannot parse array, return array to caller for parsing
						break;
				}
		} catch (FileNotFoundException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
	    } catch (ParseException e) {
	        e.printStackTrace();
	    } catch (IllegalArgumentException e) {
	        e.printStackTrace();
	    }
		
		return o;
	}

	public void readFromConfigFile(WarModel war) {
	
		try {

			JSONParser parser = new JSONParser();
			Object obj = parser.parse(new FileReader(IConstants.CONFIGURATION_FILE));
			JSONObject jsonObject = (JSONObject) obj;
			JSONObject jsonWar = (JSONObject) jsonObject.get("war");

			readLaunchers(jsonWar, war);
			readMissileDestructors(jsonWar, war);
			readLauncherDestructors(jsonWar, war);

		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
	}

	private void readLaunchers(JSONObject jsonWar, WarModel war) {

		JSONObject missileLaunchers = (JSONObject) jsonWar.get("missileLaunchers");
		JSONArray launchers = (JSONArray) missileLaunchers.get("launcher");
		Iterator<JSONObject> iterator = launchers.iterator();
		while (iterator.hasNext()) {
			JSONObject launcher = iterator.next();

			String launcherID = (String) launcher.get("id");
			String isHiddenStr = (String) launcher.get("isHidden");
			boolean isHidden = isHiddenStr.equals("true") ;
			
			war.addMissileLauncher(launcherID, isHidden);
			readMissilesToLaunch(launcher, launcherID, war);
		}
	}

	private void readMissilesToLaunch(JSONObject launcher, String launcherID, WarModel war) {
		JSONArray missiles = (JSONArray) launcher.get("missile");
		Iterator<JSONObject> itr = missiles.iterator();

		while (itr.hasNext()) {
			
			JSONObject missile = itr.next();
			String missileID = (String) missile.get("id");
			String destination = (String) missile.get("destination");
			int launchTime = Integer.parseInt((String) missile.get("launchTime"));
			int flyTime = Integer.parseInt((String)missile.get("flyTime"));
			int potentialDamage = Integer.parseInt((String) missile.get("damage"));

			
			warSched.scheduleMissileLaunch(war, launcherID, launchTime, missileID, potentialDamage, destination, flyTime);
		}
	}

	private void readMissileDestructors(JSONObject theWar, WarModel war) {

		JSONObject missileDestructors = (JSONObject) theWar.get("missileDestructors");
		JSONArray destructors = (JSONArray) missileDestructors.get("destructor");
		Iterator<JSONObject> iterator = destructors.iterator();
		while (iterator.hasNext()) {
			JSONObject destructor = iterator.next();
			String id = (String) destructor.get("id");

			war.addMissileDestructor(id);

			readMissilesToDestruct(destructor, id, war);
		}
	}

	private void readMissilesToDestruct(JSONObject destructor, String destructorID, WarModel war) {
		Object destructdMissile = destructor.get("destructdMissile");
		Map<String, Entry<String, Integer>> missilesToDestruct = new HashMap<>();
		
		if (destructdMissile instanceof JSONArray){
			Iterator<JSONObject> itr = ( (JSONArray) destructdMissile).iterator();
			while (itr.hasNext()) {
				JSONObject missile = itr.next();
				String missileID = (String) missile.get("id");
				int destructAfterLaunch = Integer.parseInt((String)missile.get("destructAfterLaunch"));

				Entry<String, Integer> entry = new SimpleEntry<String, Integer>(destructorID, destructAfterLaunch);
				missilesToDestruct.put(missileID, entry);
			}
		}
		else{
			JSONObject missile = (JSONObject) destructdMissile;
			String missileID = (String) missile.get("id");
			int destructAfterLaunch = Integer.parseInt((String)missile.get("destructAfterLaunch"));
			Entry<String, Integer> entry = new SimpleEntry<String, Integer>(destructorID, destructAfterLaunch);
			missilesToDestruct.put(missileID, entry);
		}
		WarSchduler.getInstance().setMissileDestructs(missilesToDestruct); 
	}

	private void readLauncherDestructors(JSONObject theWar, WarModel war) {

		JSONObject missileLauncherDestructors = (JSONObject) theWar.get("missileLauncherDestructors");
		JSONArray destructors = (JSONArray) missileLauncherDestructors.get("destructor");
		Iterator<JSONObject> iterator = destructors.iterator();
		int idGen = 0;

		while (iterator.hasNext()) {
 
			JSONObject destructor = iterator.next();
			String t = (String) destructor.get("type");
			LauncherDestructorType type = LauncherDestructorType.SHIP;
			if (t.equals(type.name().toLowerCase()))
				type = LauncherDestructorType.PLANE;
			String destructorID = "" + idGen++;
			
			war.addLauncherDestructor(destructorID, type);

			readLaunchersToDestruct(destructor, destructorID, type, war);
		}
	}

	private void readLaunchersToDestruct(JSONObject destructor, String destructorID, LauncherDestructorType type, WarModel war) {
		Object destructedLanucher = (Object) destructor.get("destructedLanucher");
		if (destructedLanucher instanceof JSONArray){
			Iterator<JSONObject> itr = ((JSONArray)destructedLanucher).iterator();
			while (itr.hasNext()) {
				JSONObject launcher = itr.next();
				String launcherID = (String) launcher.get("id");
				int destructTime = Integer.parseInt((String)launcher.get("destructTime"));

				warSched.scheduleLauncherDestruct(war, destructorID, launcherID, destructTime);
			}
		}
		else{
			JSONObject launcher = (JSONObject) destructedLanucher;
			String launcherID = (String) launcher.get("id");
			int destructTime = Integer.parseInt((String)launcher.get("destructTime"));

			warSched.scheduleLauncherDestruct(war, destructorID, launcherID, destructTime);
		}
	}
}
