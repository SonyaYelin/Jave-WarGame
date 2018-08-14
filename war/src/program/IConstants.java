package program;

public interface IConstants {

	/*general constants for the whole program*/ 
	

	public static final String	CONFIGURATION_FILE = "configFile.json";
	
	public static final String	CONSOLE_OR_JFX = "for console menu press ------ 1\n"
			   						   		   + "for javafx menu press ------- 2\n";

	public enum LauncherDestructorType 	{ 
		PLANE,
		SHIP;
	}	
}
