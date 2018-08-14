package ui;


public interface UIConstants {
	
	public static final String 	ERROR = "Error!";
	public static final String 	ENTER = "\n";
	public static final String 	TOTAL_DAMAGE = "Total damage: ";
	public static final String 	LAUNCHED_MISSILES = "Launched missiles: ";
	public static final String 	HITS = "Hits: ";
	public static final String 	DESTRUCTED_MISSILES = "Destructed missiles: ";
	public static final String 	DESTRUCTED_LAUNCHERS = "Destructed launchers: ";
	public final static String	STR_ADD_MISSILE_LAUNCHER = "Add Missile Launcher";
	public final static String 	STR_ADD_MISSILE_DESTRUCTOR = "Add Missile Destructor";
	public final static String 	STR_ADD_LAUNCHER_DESTRUCTOR = "Add Launcher Destructor";
	public final static String 	STR_LAUNCH_MISSILE = "Launche Missile";
	public final static String 	STR_DESTRUCT_LAUNCHER = "Destruct a Launcher";
	public final static String 	STR_DESTRUCT_MISSILE = "Destruct a Missile";
	public final static String 	STATISTICS = "statistics";
	public final static String 	STR_EXIT = "EXIT";
	public final static String 	ADD = "Add";
	public static final String 	SHIP = "ship";
	public static final String 	PLANE = "plane";
	public final static String 	LAUNCHER_ID = "Launcher's ID: ";
	public final static String 	MISSILE_ID = "Missile's ID: ";
	public final static String 	DESTRUCTOR_ID = "Destructor's ID: ";
	public final static String 	POTENTIAL_DAMAGE = "Potential Damage: ";
	public final static String 	DESTINATION = "Destination: ";
	public final static String 	FLY_TIME = "Fly Time:  ";
	public final static String 	BYE = "Bye Bye... ";
	public final static String 	WAR_IS_OVER = "War is over ... ";

	
	//paths
	public final static String 	LAUNCHER_IMAGE_PATH = "images/launcher.jpg";
	public final static String 	SHIP_IMAGE_PATH = "images/ship.png";
	public final static String 	PLANE_IMAGE_PATH = "images/plane.png";
	public final static String 	MISSILE_DESTRUCTOR_IMAGE_PATH = "images/missileDestructor.jpg";
	public final static String 	MISSILE_IMAGE_PATH = "images/missile.png";
	public final static String 	D_MISSILE_IMAGE_PATH = "images/dMissile.png";
	public final static String 	EXPLODE_IMAGE_PATH = "images/explode.png";
	public final static String 	MAIN_PANE_FXML_PATH = "ui/mainPane.fxml";
	
	public final static int 	WAR_PANE_HIGHT = 500;
	public final static int 	WAR_OBJECT_WIDTH = 30;
	public final static int 	WAR_OBJECT_HIGHT = 40;
	public final static int 	START_IDX = 5;

	//console
	public static final int 	SHIP_IDX = 1;
	public static final int 	YES = 1;
	public static final int		ADD_LAUNCHER_DESTRUCTOR = 1;
	public static final int 	ADD_MISSILE_DESTRUCTOR = 2;
	public static final int 	ADD_LAUNCHER = 3;
	public static final int 	LAUNCH_MISSILE = 4;
	public static final int 	DESTRUCT_MISSILE = 5;
	public static final int 	DESTRUCT_LAUNCHER = 6;
	public static final int 	SHOW_STATISTICS = 7;
	public static final int 	EXIT = 0;
	public static final String	YES_NO = "YES --- 1\nNO ---- 2\n";
	public static final String	TYPE = "Ship ----- 1\n" + "Plane ---- 2\n";
	public static final String	ENTER_IS_LAUNCHER_HIDDEN = "is launcher hidden?\n" + YES_NO;
	public static final String 	LOAD_FROM_CONFIG = "Do you want to read from config file?\n";
	public static final String 	MAIN_MENU_STR = "Welcome to THE WAR !!!\n"
												+ "Please choose one of the above:\n"
												+ "Add launcher destractor ------ 1\n"
												+ "Add missile destractor ------- 2\n"
												+ "Add launcher ----------------- 3\n"
												+ "Launch missile --------------- 4\n"
												+ "Destruct a launcher ---------- 5\n"
												+ "Destruct a missile ----------- 6\n"	
												+ "Show statistics -------------- 7\n"
												+ "Exit ------------------------- 0\n";
	
}