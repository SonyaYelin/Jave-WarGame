package bl;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class HiddenMissileLauncher extends MissileLauncher {

	private boolean	isHiddenNow; 
	private boolean	isLaunching; 
	
	
	public HiddenMissileLauncher(String id, WarModel war) {
		super(id, war);
		isHiddenNow = true;
		isLaunching = false;
	}
	
	public synchronized void launchMissile() {
		isLaunching = true;
		emerge();

		super.launchMissile();
		
		isLaunching = false; 
		hide();
	}
	
	public void emerge(){
		isHiddenNow = false;
		war.launcherStateChanged(id, isHiddenNow);
	}
	
	public synchronized void hide(){	
		if (isDestructed()) 
			return;
		//takes X time to hide
		//if after X time launcher not launching missile -> hide
		new Timer().schedule(new TimerTask() {
			public void run() {
				if ( !isLaunching )
					isHiddenNow = true;
				war.launcherStateChanged(id, isHiddenNow);
			}
		}, new Random().nextInt(MAX_TIME*ONE_SEC+1) );	
		
	}

	public void destructLauncher(){
		if ( !isHiddenNow )
			super.destructLauncher();
	}
	
	public boolean isHidden(){
		return true;
	}
}