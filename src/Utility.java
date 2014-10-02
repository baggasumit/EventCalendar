/**
 * Utility.java
 * Class to hold the utility functions used by the application
 *
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


public class Utility {	
	/*
	 * Makes a key from month and year for hash map 
	 */
	public static String makeMonthYearKey(int month, int year) {
		// pad with zeroes in front
		return String.format("%04d", year) + String.format("%02d", month);
	}
	
	/*
	 * gets date and time from user through terminal
	 */
	public static Date getDateAndTimeFromUser(String datePrompt, String timePrompt) {
		Date date = new Date();
		boolean validInput = false;
		while(!validInput) {
			try {			
				SimpleDateFormat dateTimeFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm");
				String dateString = getDateFromUser(datePrompt);
				String timeString = getTimeFromUser(timePrompt);
				date = dateTimeFormat.parse(dateString + " " + timeString);
				validInput = true;
			} catch (ParseException p) {
				System.out.println("Date or time format not valid. Enter again.");
			}
		}
		return date;
	}
	
	/*
	 * gets date from user through terminal
	 */
	public static String getDateFromUser(String prompt) {
		String dateString = "";
		boolean validInput = false;
		while(!validInput) {
			try {
				dateString = getStringFromTerminal(prompt);
				SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
				// This makes sure parser doesn't try to convert invalid dates into valid dates
				// 14/20/2001 will be converted to 2/20/2002 otherwise
				dateFormat.setLenient(false);
				dateFormat.parse(dateString);
				validInput = true;
			} catch (ParseException pe) {
				System.out.println("Invalid date. Please enter in MM/dd/yyyy format. Example: 3/17/2015");
			}
		}
		return dateString;
	}
	
	/*
	 * gets time from user through terminal
	 */
	public static String getTimeFromUser(String prompt) {
		String timeString = "";
		boolean validInput = false;
		while(!validInput) {
			try {
				timeString = getStringFromTerminal(prompt);
				SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
				// This makes sure parser doesn't try to convert invalid time into valid time
				// 15:76 will be converted to 16:16 otherwise
				dateFormat.setLenient(false);
				dateFormat.parse(timeString);
				validInput = true;
			} catch (ParseException pe) {
				System.out.println("Invalid time. Please enter in HH:mm format. Example: 15:30");
			}
		}
		return timeString;
	}
	
	/*
	 * gets a string from user through terminal
	 */
	public static String getStringFromTerminal(String prompt) {
		String userInput = "";
		while(userInput.equals("")) {
		    try {
		      System.out.print(prompt);
		      userInput = new BufferedReader(new InputStreamReader(System.in))
		        .readLine();
		      if (userInput.equals(""))
		    	  System.out.println("Field cannot be blank.");
		    } catch (IOException e) {
		      e.printStackTrace();
		    }
		}
	    return userInput;
	}
	
	/*
	 * gets an integer from user through terminal
	 */
	public static int getIntegerFromTerminal(String prompt) {
	    String line = "";
	    int num = 0;
	    boolean validInteger = false;
	    while (!validInteger) {
	      line = getStringFromTerminal(prompt);
	      try {
	        num = Integer.parseInt(line);
	        validInteger = true;
	      } catch (NumberFormatException e) {
	        System.out.println("Invalid number.");
	      }
	    }
	    return num;
	}
	
	/*
	 * gets an integer from user between min and max through terminal
	 */
	public static int getIntegerFromTerminal(String prompt, int min, int max) {
		boolean validInput = false;		
		while(!validInput) {
			int userInput = getIntegerFromTerminal(prompt);
			if (userInput >= min && userInput <= max) {
				return userInput;
			} else {
				System.out.println("Number out of input range.");
			}
		}
		return 0;
	}
	
	/*
	 * prompts user for a yes/no question and returns true if yes, no otherwise
	 */
	public static boolean getYesNoInputFromUser(String prompt) {
		String userInput = "";
		
		while(true) {
			userInput = getStringFromTerminal(prompt);
			if(userInput.equalsIgnoreCase("yes") || userInput.equalsIgnoreCase("y"))
				return true;
			if(userInput.equalsIgnoreCase("no") || userInput.equalsIgnoreCase("n"))
				return false;
		}
	    
	}
	
	/*
	 * asks the user which event attribute he wants to operate on
	 */
	public static int getAttributeToUpdateFromUser() {
		System.out.println("===========================================================");
  		System.out.println("|   Choose attribute to update:                           |");
  		System.out.println("|        1. Date/Time                                     |");
  		System.out.println("|        2. Notes                                         |");
  		System.out.println("|        3. Reminder                                      |");
  		System.out.println("|        4. Frequency                                     |");
  		System.out.println("|        5. None                                          |");
  		System.out.println("===========================================================");
  		
  		int userInput = 0;
  		boolean validInput = false;
  		while(!validInput) {			
			userInput = Utility.getIntegerFromTerminal("Enter option (1-5): ");
			if(userInput >= 1 && userInput <= 5) {
				return userInput;
			} else
				System.out.println("Invalid Input.");			
		}
  		return 0;
	}
	
	/*
	 * gets the event frequency from user 
	 */
	public static Event.Frequency getEventFrequencyFromUser() {
  		System.out.println("===========================================================");
  		System.out.println("|   Choose frequency of the recurring event:              |");
  		System.out.println("|        1. Daily                                         |");
  		System.out.println("|        2. Weekly                                        |");
  		System.out.println("|        3. Monthly                                       |");
  		System.out.println("|        4. Yearly                                        |");
  		System.out.println("|        5. Not recurring                                 |");
  		System.out.println("===========================================================");
  		
  		int userInput = 0;
  		boolean validInput = false;
  		while(!validInput) {						
			
			userInput = Utility.getIntegerFromTerminal("Enter option (1-5): ");
			
			switch(userInput) {
			case 1:				
				return Event.Frequency.DAILY;
			case 2:
				return Event.Frequency.WEEKLY;
			case 3:
				return Event.Frequency.MONTHLY;
			case 4:
				return Event.Frequency.YEARLY;
			case 5:
				return Event.Frequency.NONE;
			default:
				System.out.println("Invalid Input.");
			}			
		}
  		return Event.Frequency.NONE;
	}
	
	/*
	 * prompts the user to press Enter to continue
	 */
	public static void pressEnterToContinue() { 
	    System.out.println("Press Enter to continue...");	        
	    try {
	    	String userInput = new BufferedReader(new InputStreamReader(System.in))
	        .readLine();
	    }  
	    catch(IOException e) {
	    	System.err.println(e.getMessage());
	    }  
	}
	
	/*
	 * returns the day of the month for a given event
	 */
	public static int getEventDayOfMonth(Event event) {
		Calendar eventDate = new GregorianCalendar();
		eventDate.setTime(event.getDate());
		return eventDate.get(Calendar.DAY_OF_MONTH);
	}
}
