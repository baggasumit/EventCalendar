/**
 * EventsCalendar.java
 * Class to print the calendar month on terminal given the month and year.
 * Also marks the days with a '*' besides it given by the list daysToMark
 */

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

public class EventsCalendar {	

	/*
	 * prints a calendar month based on month / year info
	 */
	public static void printCalendarMonthYear(int month, int year, ArrayList<Integer> daysToMark) {
		Calendar cal = new GregorianCalendar();		

		cal.clear();
		cal.set(year, month, 1);

		// calendar header
		System.out.println("\n"
				+ cal.getDisplayName(Calendar.MONTH, Calendar.LONG,
						Locale.US) + " " + cal.get(Calendar.YEAR));

		int firstWeekdayOfMonth = cal.get(Calendar.DAY_OF_WEEK);
		
		int numberOfMonthDays = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

		// print calendar month based on the weekday of the first
		// day of the month and the number of days in month
		printCalendar(numberOfMonthDays, firstWeekdayOfMonth, daysToMark);
	}

	/*
	 * 	prints calendar month based on the weekday of the first
	 *  day of the month and the number of days in month
	 */
	private static void printCalendar(int numberOfMonthDays, 
			int firstWeekdayOfMonth, ArrayList<Integer> daysToMark) {		
		
		int weekdayIndex = 0;

		System.out.println("Su   Mo   Tu   We   Th   Fr   Sa");

		// print space for weekdays before the first day of month
		for (int day = 1; day < firstWeekdayOfMonth; day++) {
			System.out.print("     ");
			weekdayIndex++;
		}

		// print the days of month in tabular format.
		for (int day = 1; day <= numberOfMonthDays; day++) {
			// print day
			System.out.printf("%1$2d", day);
			// Mark the day with a '*' besides it if it is in the daysToMark list
			if(daysToMark.contains(day))
				System.out.print("*");
			else
				System.out.print(" ");
			
			weekdayIndex++;
			// If last weekday go to next line
			if (weekdayIndex == 7) {
				weekdayIndex = 0;
				System.out.println();
			} else {				
				System.out.print("  ");
			}
		}

		System.out.println();
	}
}