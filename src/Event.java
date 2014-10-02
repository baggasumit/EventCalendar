/**
 * Event.java
 * Class to hold the event info.
 * Also contains data structures to hold all the events inserted by user
 */

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;


public class Event {
	private Date date;
	private String name;
	private String notes;
	private int reminder;
	private Frequency frequency;
	
	public static enum Frequency {
	    DAILY, WEEKLY, MONTHLY, YEARLY, NONE 
	}
	
	public static Map<String, ArrayList<Event>> nameMap;
	public static Map<String, ArrayList<Event>> nonRecurringEvents;
	public static PriorityQueue<Event> dailyEvents;
	public static PriorityQueue<Event> weeklyEvents;
	public static PriorityQueue<Event> monthlyEvents;
	public static Map<Integer, PriorityQueue<Event>> yearlyEvents;
	
	/*
	 * Initializes the data structures that hold the events inserted by user
	 */
	public static void initializeMaps() {
		Comparator<Event> eventDateComparator = new EventDateComparator();
		nameMap = new HashMap<String, ArrayList<Event>>();
		nonRecurringEvents = new HashMap<String, ArrayList<Event>>();
		dailyEvents = new PriorityQueue<Event>(1, eventDateComparator);
		weeklyEvents = new PriorityQueue<Event>(1, eventDateComparator);
		monthlyEvents = new PriorityQueue<Event>(1, eventDateComparator);
		yearlyEvents = new HashMap<Integer, PriorityQueue<Event>>();		
		
		// Initialize yearly events
		for( int month = Calendar.JANUARY; month <= Calendar.DECEMBER; month++) {
			yearlyEvents.put(month, new PriorityQueue<Event>(1, eventDateComparator));
		}
		
	}
	/**
	 * @param date
	 * @param name
	 * @param notes
	 * @param reminder
	 * @param frequency
	 */
	public Event(Date date, String name, String notes, int reminder,
			Frequency frequency) {
		
		this.date = date;
		this.name = name;
		this.notes = notes;
		this.reminder = reminder;
		this.frequency = frequency;
	} 
	
	/*
	 * Updates the event attributes
	 */
	public void update(Date date, String name, String notes, int reminder,
			Frequency frequency) {
		
		this.date = date;
		this.name = name;
		this.notes = notes;
		this.reminder = reminder;
		this.frequency = frequency;
	}

	/*
	 * Prints the event info
	 */
	public void print() {
		System.out.println("Name : " + this.name);
		System.out.println("Date : " + this.date.toString());
		System.out.println("Frequency : " + this.frequency);
		System.out.println("Reminder : " + String.valueOf(reminder) + " minutes");
		System.out.println("---");
	}
	
	/**
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the notes
	 */
	public String getNotes() {
		return notes;
	}

	/**
	 * @return the reminder
	 */
	public int getReminder() {
		return reminder;
	}

	/**
	 * @return the frequency
	 */
	public Frequency getFrequency() {
		return frequency;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param notes the notes to set
	 */
	public void setNotes(String notes) {
		this.notes = notes;
	}

	/**
	 * @param reminder the reminder to set
	 */
	public void setReminder(int reminder) {
		this.reminder = reminder;
	}

	/**
	 * @param frequency the frequency to set
	 */
	public void setFrequency(Frequency frequency) {
		this.frequency = frequency;
	}

		
}

/*
 * Comparator class for Priority Queues to sort the events by date
 */
class EventDateComparator implements Comparator<Event> {
	@Override
    public int compare(Event event1, Event event2) {
		return event1.getDate().compareTo(event2.getDate());
    }
    
}
