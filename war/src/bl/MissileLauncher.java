package bl;

import java.util.LinkedList;
import java.util.Queue;

public class MissileLauncher extends WarObject{
	
	private boolean			isDestructed;
	private Queue<Missile>	missilesToLaunch = new LinkedList<>();
	
	public MissileLauncher(String id, WarModel war){
		super(id, war);
		isDestructed = false;
	}
		
	public synchronized void addMissileToLaunchQueue(Missile m){
		missilesToLaunch.add( m );
		notifyCheck();
	}

	public void run() {
		while ( inWar ) {
			if ( !missilesToLaunch.isEmpty()) 
				launchMissile();
			else {
				synchronized (MissileLauncher.this) {
					try {
						waitingForWork = true;
						wait(); 
						waitingForWork = false;
					} catch (InterruptedException e) {e.printStackTrace();}
				}
			}
		}
	}
	
	public void launchMissile() {	
		Missile m = missilesToLaunch.poll();
		if (m == null) 
			return;
		
		war.launchStarted(id, m.getID(), m.getDestination(), m.getFlyTime());	
		m.start();
		
		try { m.join(); } catch (InterruptedException e) { e.printStackTrace(); }
 	}

	public void updateResults(int launchTime, boolean success, String destination, int damage, int flyTime, String missileID){
		war.launchEnded(this, missileID, success, destination, damage, flyTime);
		
		//int launchTime = war.getWarTimeInSeconds() - flyTime;
		logsGen.endLaunch(missileID, id, destination, damage, flyTime, success, this, launchTime);
	}
	
	public void destructLauncher(){
		isDestructed = true;
		terminate();
	}
		
	
	//getters
	public boolean isDestructed(){
		return isDestructed;
	}
	
	public WarModel getWar() {
		return war;
	}
	
	// hashCode & equals
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MissileLauncher other = (MissileLauncher) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}