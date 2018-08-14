package ui;

public class ValidInput {

	public static final String 	INVALID_INPUT = "invalid input !!!";


	public static boolean IsInputPresent(String input) throws Exception{
		if (input.equals("") || input == null)
			throw new Exception(INVALID_INPUT);
		
		return true;
	}
	
	public static boolean IsInteger(String num) throws Exception{
		try { 
	        Integer.parseInt(num); 
	    } catch(Exception e) { throw new Exception(INVALID_INPUT);  } 
	   
	    return true;
	}
}
