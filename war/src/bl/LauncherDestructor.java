package bl;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import program.IConstants.LauncherDestructorType;

public class LauncherDestructor extends WarObject {

	private LauncherDestructorType 	type;
	private Queue<MissileLauncher>	targets = new LinkedList<>();

	
	public LauncherDestructor(String id, LauncherDestructorType type, WarModel war) {
		super(id, war);
		this.type = type;
	}

	public synchronized void addTargetToQueue(MissileLauncher l) {
		targets.add(l);
		notifyCheck();
	}

	public void run() {
		while (inWar) {
			if (!targets.isEmpty())
				destructLauncher();
			else {
				synchronized (this) {
					try {
						waitingForWork = true;
						wait();
						waitingForWork = false;
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	public void destructLauncher() {
		try {
			MissileLauncher l = targets.poll();
			if (l == null || !l.inWar)
				return;
			
			int duration = new Random().nextInt(MAX_TIME) + 1;
			war.launcherDestructStarted(id, l.getID(), duration);
			Thread.sleep(duration*ONE_SEC);
			
			//war ended
			if ( !this.inWar )
				return;
			
			boolean success = false;
			if (!l.isDestructed()) {
				l.destructLauncher();

				if (l.isDestructed())
					success = true;
			}
			updateResults(l.getID(), success);

		} catch (InterruptedException e) {e.printStackTrace();}
	}

	public void updateResults(String launcherID, boolean success) {
		war.launcherDestructEnded(id, launcherID, success);
		logsGen.afterLauncherDestruct(this, launcherID, success);
	}
	
	
	//getters
	public LauncherDestructorType getType(){
		return type;
	}

	
	// hashCode & equals
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}
	
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		LauncherDestructor other = (LauncherDestructor) obj;
		if (type != other.type)
			return false;
		return true;
	}
}
