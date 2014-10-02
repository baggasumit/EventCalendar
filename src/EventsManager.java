/**
 * EventsManager.java
 * Class to handle insert/update/delete/search event and 
 * enables the user to browse the calendar for events
 * 
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Calendar;
import java.util.List;
import java.util.PriorityQueue;


public class EventsManager {

	static BufferedReader bufferedReader;
	public static void main(String[] args) {
		bufferedReader = new BufferedReader(new InputStreamReader(System.in));
		
		EventsManager eventsManager = new EventsManager();
		// initialize the data structures to hold the inserted events
		Event.initializeMaps();
		// insert event data for testing
		eventsManager.init();
		// display main menu and get Menu option from user
		eventsManager.readMenuOptionsFromUser();
	}
	
	/*
	 * displays main menu to user
	 */	
	public void displayMenu() {
		System.out.println("\n");
		System.out.println("===========================================================");
  		System.out.println("|   WELCOME TO EVENT SCHEDULING SYSTEM                    |");
  		System.out.println("===========================================================");
  		System.out.println("|   Choose from one of the Options below:                 |");
  		System.out.println("|        1. Add Event                                     |");
  		System.out.println("|        2. Update Event                                  |");
  		System.out.println("|        3. Delete Event                                  |");
  		System.out.println("|        4. Browse Calendar                               |");
  		System.out.println("|        5. Search Event                                  |");
  		System.out.println("|        6. EXIT                                          |");
  		System.out.println("===========================================================");
	}
	
	/*
	 * get menu option from user
	 */
	public void readMenuOptionsFromUser(){	
		
		int userInput = 0;
		
		while(userInput != 6) {
			displayMenu();			
			
			userInput = Utility.getIntegerFromTerminal("Enter option (1-6): ");
			
			switch(userInput) {
			case 1:				
				addEvent();
				break;
			case 2:
				updateEvent();
				break;
			case 3:
				deleteEvent();
				break;
			case 4:
				browseCalendar();
				break;
			case 5:
				searchEvent();
				break;
			case 6:				
				break;
			default:
				System.out.println("Invalid Input");
			}			
		}		
	}
	
	/*
	 * adds an event through user input data via terminal
	 */
	public void addEvent() {
		System.out.println("\n");
		System.out.println("===========================================================");
  		System.out.println("|   Add Event Module                                      |");
  		System.out.println("===========================================================");
  		
  		// get event data from user
		String eventName = Utility.getStringFromTerminal("Enter event name: ");
		Date eventDate = Utility.getDateAndTimeFromUser("Enter date for the event (MM/dd/yyyy): ",
				"Enter time for the event in 24 hour format (HH:mm): ");
		String eventNotes = Utility.getStringFromTerminal("Enter event notes: ");
		int reminder = Utility.getIntegerFromTerminal("Enter event reminder in minutes (0 for no reminder): ");
		boolean isRecurring = Utility.getYesNoInputFromUser("Is this a recurring event? (yes/no): ");
		Event.Frequency frequency;
		if (isRecurring) {
			frequency = Utility.getEventFrequencyFromUser();
		} else {
			frequency = Event.Frequency.NONE;
		}
		
		Event event = new Event(eventDate, eventName, eventNotes, reminder, frequency);
		System.out.println("\nEvent Created Successfully");
		
		// add the event to relevant data structures
		addToMaps(event);
		
		Utility.pressEnterToContinue();				
	}
	/*
	 * adds an event to the relevant data structures
	 */
	public void addToMaps(Event event) {
		// Add to nameMap
		String eventName = event.getName();
		if (Event.nameMap.containsKey(eventName.toLowerCase())) {
			Event.nameMap.get(eventName.toLowerCase()).add(event);
		} else {
			ArrayList<Event> eventList = new ArrayList<Event>();
			eventList.add(event);
			Event.nameMap.put(eventName.toLowerCase(), eventList);
		}
		
		// Add event to the frequency map/queue according to the frequency of the event
		addToFrequencyMaps(event);
	}
	
	/*
	 * Adds event to the frequency map/queue according to the frequency of the event
	 */
	public void addToFrequencyMaps(Event event) {
		
		Calendar eventDate = new GregorianCalendar();
		eventDate.setTime(event.getDate());		
		
		Event.Frequency eventFrequency = event.getFrequency();
		switch(eventFrequency) {
		case DAILY:
			Event.dailyEvents.add(event);
			break;
		case WEEKLY:
			Event.weeklyEvents.add(event);
			break;
		case MONTHLY:
			Event.monthlyEvents.add(event);
			break;
		case YEARLY:
			Event.yearlyEvents.get(eventDate.get(Calendar.MONTH)).add(event);
			break;
		case NONE:
			String key = Utility.makeMonthYearKey
					(eventDate.get(Calendar.MONTH), eventDate.get(Calendar.YEAR));
			if (Event.nonRecurringEvents.containsKey(key)) {
				Event.nonRecurringEvents.get(key).add(event);
			}
			else {
				ArrayList<Event> eventList = new ArrayList<Event>();
				eventList.add(event);
				Event.nonRecurringEvents.put(key, eventList);
			}
			
		default:
			break;
		
		}
	}
	
	/*
	 * Updates an event through user input data via terminal
	 */
	public boolean updateEvent() {
		System.out.println("\n");
		System.out.println("===========================================================");
  		System.out.println("|   Update Event Module                                   |");
  		System.out.println("===========================================================");
  		ArrayList<Event> eventList = listEventsFromName();
		if(eventList != null) {
			int eventCount = eventList.size();
			// If only one event is found with the given name then update the event otherwise
			// ask the user which event he wants to update
			if (eventCount == 1) {
				boolean userConfirmation = Utility.getYesNoInputFromUser("Update this event? (yes/no): ");
				if(userConfirmation) {
					updateEvent(eventList.get(0));
					System.out.println("Event Updated.");
					
				} 
			} else if (eventCount > 1) {
				int updateIndex = -1;
				boolean validInput = false;
				while(!validInput) {
					updateIndex = Utility.
							getIntegerFromTerminal("Select the event to update (1-" + String.valueOf(eventCount) + "): ");
					if (updateIndex >= 1 && updateIndex <= eventCount) {
						updateEvent(eventList.get(updateIndex-1));
						validInput = true;
						System.out.println("Event Updated.");
					}
				}
			}
		} 
		Utility.pressEnterToContinue();
		return true;
	}

	/*
	 * Deletes an event through user chosen event via terminal
	 */
	public void deleteEvent() {
		System.out.println("\n");
		System.out.println("===========================================================");
  		System.out.println("|   Delete Event Module                                   |");
  		System.out.println("===========================================================");
		ArrayList<Event> eventList = listEventsFromName();
		if(eventList != null) {
			int eventCount = eventList.size();
			// If only one event is found with the given name then delete the event otherwise
			// ask the user which event he wants to delete
			if (eventCount == 1) {
				boolean userConfirmation = 
						Utility.getYesNoInputFromUser("Are you sure you want to delete? (yes/no): ");
				if(userConfirmation) {
					Event event = eventList.get(0);
					String eventName = event.getName();
					Event.nameMap.remove(eventName.toLowerCase());
					deleteEventFromFrequencyMaps(event);
					System.out.println("Event Deleted.");
					
				} 
			} else if (eventCount > 1) {
				int deleteIndex = -1;
				boolean validInput = false;
				while(!validInput) {
					deleteIndex = Utility.getIntegerFromTerminal
							("Select the event to delete (1-" + String.valueOf(eventCount) + "): ");
					if (deleteIndex >= 1 && deleteIndex <= eventCount) {
						Event event = eventList.get(deleteIndex-1);
						eventList.remove(deleteIndex-1);
						deleteEventFromFrequencyMaps(event);
						validInput = true;
						System.out.println("Event Deleted.");
					}
				}
			}
		} 
		Utility.pressEnterToContinue();
	}
	
	/*
	 * deletes an event from frequency maps/queues according
	 * to the frequency of the event
	 */
	public void deleteEventFromFrequencyMaps(Event event) {		
		Calendar eventDate = new GregorianCalendar();
		eventDate.setTime(event.getDate());
		Event.Frequency eventFrequency = event.getFrequency();
		switch(eventFrequency) {
		case DAILY:
			Event.dailyEvents.remove(event);
			break;
		case WEEKLY:
			Event.weeklyEvents.remove(event);
			break;
		case MONTHLY:
			Event.monthlyEvents.remove(event);
			break;
		case YEARLY:
			Event.yearlyEvents.get(eventDate.get(Calendar.MONTH)).remove(event);
			break;
		case NONE:
			String key = Utility.makeMonthYearKey
					(eventDate.get(Calendar.MONTH), eventDate.get(Calendar.YEAR));
			Event.nonRecurringEvents.get(key).remove(event);			
		default:
			break;
		
		}
	}
	
	/*
	 * Enables the user to browse the calendar by displaying a calendar month and events
	 * occurring in that month. User can go to next or previous month or can go to a
	 * particular month of his choice
	 */
	public void browseCalendar() {
		System.out.println("\n");
		System.out.println("===========================================================");
  		System.out.println("|   Browse Calendar Module                                |");
  		System.out.println("===========================================================");
		
		String user_input = "";
		Calendar cal = new GregorianCalendar();
		int month = cal.get(Calendar.MONTH);
		int year = cal.get(Calendar.YEAR);
		
		System.out.println();
		while(!user_input.toLowerCase().equals("x")) {
			System.out.println("\n");
			System.out.println("-----------------------------------------------------------");
			System.out.println("-----------------Events this month-------------------------");
			System.out.println("-----------------------------------------------------------");
			ArrayList<Integer> daysToMark = listEvents(month, year);
			EventsCalendar.printCalendarMonthYear(month, year, daysToMark);
			System.out.print("\n\nEnter option ('n' for next month, 'p' for previous month,\n"
					+ "  'g' to goto particular month and year, 'x' for exit): ");
			
			
			try {
				user_input = bufferedReader.readLine();
			} catch (IOException e) {				
				e.printStackTrace();
			}
			
			switch(user_input) {
			case "n":				
				if (month == Calendar.DECEMBER) {
					month = Calendar.JANUARY;
					year += 1;
				} else {
					month += 1;
				}
				break;
			case "p":
				if (month == Calendar.JANUARY) {
					month = Calendar.DECEMBER;
					year -= 1;
				} else {
					month -= 1;
				}
				break;
			case "g":
				month = Utility.getIntegerFromTerminal("Enter month number (1-12): ", 1, 12) - 1;
				year = Utility.getIntegerFromTerminal("Enter year (1-9999): ", 1, 9999);
			default:
				break;
			}
			
			
			
		}
	}
	
	/*
	 * displays events occurring in a particular calendar month
	 * returns a list containing the days on which the events are occurring 
	 */
	public ArrayList<Integer> listEvents(int month, int year) {
		// List non recurring events
		ArrayList<Integer> daysToMark = new ArrayList<Integer>();
		boolean showHeader = true;
		String key = Utility.makeMonthYearKey(month, year);
		if(Event.nonRecurringEvents.containsKey(key)) {
			ArrayList<Event> nonRecurringEventsList = Event.nonRecurringEvents.get(key);
			
			for(Event event : nonRecurringEventsList) {
				daysToMark.add(Utility.getEventDayOfMonth(event));
				if(showHeader) {
					System.out.println("------ Non-recurring Events -------");
					showHeader = false;
				}
				event.print();
			}
		}
		
		// List yearly events
		showHeader = true;	
		for(Event event : Event.yearlyEvents.get(month)) {
			if(eventOccursInCalendarMonth(event, month, year)) {
				daysToMark.add(Utility.getEventDayOfMonth(event));
				if(showHeader) {
					System.out.println("------ Yearly Events --------");
					showHeader = false;
				}
				event.print();
			}
			else
				break;
		}
		
		// List monthly events
		showHeader = true;		
		for(Event event : Event.monthlyEvents) {
			if(eventOccursInCalendarMonth(event, month, year)) {
				daysToMark.add(Utility.getEventDayOfMonth(event));
				if(showHeader) {
					System.out.println("------ Monthly Events -------");
					showHeader = false;
				}
				event.print();
			}
			else
				break;				
		}		

		// List weekly events
		showHeader = true;	
		for(Event event : Event.weeklyEvents) {
			if(eventOccursInCalendarMonth(event, month, year)) { 
				if(showHeader) {
					System.out.println("------ Weekly Events --------");
					showHeader = false;
				}
				event.print();
			}
			else
				break;
		}

		// List daily events	
		showHeader = true;	
		for(Event event : Event.dailyEvents) {
			if(eventOccursInCalendarMonth(event, month, year)) {
				if(showHeader) {
					System.out.println("------ Daily Events ---------");
					showHeader = false;
				}
				event.print();
			}
			else
				break;
		}
		return daysToMark;
		
	}
	
	/*
	 * determines if a given event occurs in a particular calendar month
	 */
	public boolean eventOccursInCalendarMonth(Event event, int month, int year) {
		Calendar eventDate = new GregorianCalendar();
		eventDate.setTime(event.getDate());
		if(eventDate.get(Calendar.YEAR) > year) {
			return false;
		} else if (eventDate.get(Calendar.YEAR) == year) {
			return eventDate.get(Calendar.MONTH) <= month;
		}
		return true;
		
	}
	
	/*
	 * displays the list of events according to the search query on event name
	 */
	public void searchEvent() {
		System.out.println("\n");
		System.out.println("===========================================================");
  		System.out.println("|   Search Event Module                                   |");
  		System.out.println("===========================================================");
		boolean continueSearch = true;
		while(continueSearch) {
			listEventsFromName();
			continueSearch = Utility.getYesNoInputFromUser("Search again? (yes/no): ");
		}
		Utility.pressEnterToContinue();
	}
	
	/*
	 * lists all the events having the event name entered by user
	 */
	public ArrayList<Event> listEventsFromName() {
		ArrayList<Event> eventList = null;
		
		String eventName = Utility.getStringFromTerminal("Enter event name: ");
		
		if(Event.nameMap.containsKey(eventName.toLowerCase())) {
			eventList = Event.nameMap.get(eventName.toLowerCase());
			String eventCount = String.valueOf(eventList.size());;
			System.out.println(eventCount + " event(s) found");
			System.out.println("-----------------------------------------------------------");
			int count = 1;
			for(Event event : eventList) {
				System.out.println(String.valueOf(count)+".");
				event.print();
				count++;
			}				
		} else {
			System.out.println("No events found\n");
		}		
		
		return eventList;
	}
	
	/*
	 * Updates an event through user input data via terminal
	 */
	public void updateEvent(Event event) {
		boolean doneUpdating = false;
		while(!doneUpdating) {
			int attributeIndex = Utility.getAttributeToUpdateFromUser();
			switch(attributeIndex) {
			case 1:
				System.out.print("Current Value: ");
				System.out.println(event.getDate());
				Date newDate = Utility.
					getDateAndTimeFromUser("Enter date: ", "Enter time: ");
				event.setDate(newDate);
				break;
			case 2:
				System.out.print("Current Value: ");
				System.out.println(event.getNotes());
				String newNotes = Utility.getStringFromTerminal("Enter notes: ");
				event.setNotes(newNotes);
				break;
			case 3:
				System.out.print("Current Value: ");
				System.out.println(event.getReminder());
				int newReminder = Utility.getIntegerFromTerminal("Enter reminder in minutes: ");
				event.setReminder(newReminder);
				break;
			case 4:
				System.out.print("Current Value: ");
				System.out.println(event.getFrequency());
				Event.Frequency newFrequency = Utility.getEventFrequencyFromUser();
				if(event.getFrequency() != newFrequency) {
					deleteEventFromFrequencyMaps(event);
					event.setFrequency(newFrequency);
					addToFrequencyMaps(event);
				} else {
					event.setFrequency(newFrequency);
				}
				break;
			case 5:
				break;
			default:
				System.out.println("Invalid Input.");
								
			}
			doneUpdating = Utility.getYesNoInputFromUser("Done updating? (yes/no): ");
		}
	}
	
	/*
	 * Inserts data for testing
	 */
	public void init()  {
		SimpleDateFormat dateTimeFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm");
		try {
			Event e1 = new Event(dateTimeFormat.
					parse("4/4/2014 13:00"), "a", "bb", 30, Event.Frequency.DAILY);
			Event e2 = new Event(dateTimeFormat.
					parse("11/14/2014 13:00"), "a", "bbbb", 40, Event.Frequency.NONE);
			Event e3 = new Event(dateTimeFormat.
					parse("11/24/2014 13:00"), "b", "bbbb", 40, Event.Frequency.NONE);
			Event e4 = new Event(dateTimeFormat.
					parse("5/4/2014 13:00"), "b", "bbsdbb", 40, Event.Frequency.WEEKLY);
			Event e5 = new Event(dateTimeFormat.
					parse("6/6/2014 13:00"), "c", "bbsdsdbb", 0, Event.Frequency.YEARLY);
			Event e6 = new Event(dateTimeFormat.
					parse("8/6/2014 13:00"), "c", "bbsdsdbb", 0, Event.Frequency.YEARLY);
			Event e7 = new Event(dateTimeFormat.
					parse("9/6/2014 13:00"), "c", "bbsdsdbb", 0, Event.Frequency.YEARLY);
			Event e8 = new Event(dateTimeFormat.
					parse("10/24/2014 13:00"), "b", "bbbb", 40, Event.Frequency.NONE);
			ArrayList<Event> l1 = new ArrayList<Event>();
			l1.add(e1);
			l1.add(e2);
			ArrayList<Event> l2 = new ArrayList<Event>();
			l2.add(e3);
			l2.add(e4);
			ArrayList<Event> l3 = new ArrayList<Event>();
			l3.add(e5);
			l3.add(e6);
			l3.add(e7);
			ArrayList<Event> nonRecurListNov = new ArrayList<Event>();
			nonRecurListNov.add(e2);
			nonRecurListNov.add(e3);
			ArrayList<Event> nonRecurListOct = new ArrayList<Event>();
			nonRecurListOct.add(e8);
			Event.nonRecurringEvents.put("201410", nonRecurListNov);
			Event.nonRecurringEvents.put("201409", nonRecurListOct);
			
			Event.nameMap.put("a", l1 );
			Event.nameMap.put("b", l2 );
			Event.nameMap.put("c", l3 );
			Event.dailyEvents.add(e1);
			Event.weeklyEvents.add(e4);
			Event.yearlyEvents.get(Calendar.JUNE).add(e5);
			Event.yearlyEvents.get(Calendar.AUGUST).add(e6);
			Event.yearlyEvents.get(Calendar.SEPTEMBER).add(e7);
		} catch (ParseException pe) {
			System.out.println(pe.toString());
		}		
	}
}
