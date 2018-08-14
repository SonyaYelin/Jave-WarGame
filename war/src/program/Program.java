package program;

import java.util.Scanner;

import bl.WarTime;
import ui.App;
import ui.ConsoleView;


public class Program implements IConstants {

	static Scanner s = new Scanner(System.in);
	
	public static void main(String[] args) {
		
		System.out.println(CONSOLE_OR_JFX);
		
		if ( s.nextInt() != 1 )
			javafx.application.Application.launch(App.class);
		else
			new ConsoleView().init();
		
	}
}
