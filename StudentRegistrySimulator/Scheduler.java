/**
* This class creates and manipulates a schedule of all the courses given to it.
* @author Rajiv Bissoon 500954799
*/

import java.util.ArrayList;
import java.util.Set;
import java.util.TreeMap;

public class Scheduler 
{
    // In main() after you create a Registry object, create a Scheduler object and pass in the courses ArrayList/TreeMap
	// If you do not want to try using a Map then uncomment
	// the line below and comment out the TreeMap line
	
	//ArrayList<Student> students;
	
	private TreeMap<String, ActiveCourse> schedule;
	private String[][] s = new String[9][5]; // 9 time slot, 5 days 
	private String[] days = {"Mon", "Tue", "Wed", "Thu", "Fri"};
	private int[] times = {800, 900, 1000, 1100, 1200, 1300, 1400, 1500, 1600};
		
	/**
	* This constructor accepts a TreeMap of courses to turn into a schedule. The 2D array schedule is also initialized.
	* @param courses The courses to create into a schedule.
	*/
	public Scheduler(TreeMap<String, ActiveCourse> courses)
	{
		schedule = courses;
		
		for(int a = 0; a < s.length; a++)
		{
			for(int b = 0 ; b < s[a].length; b++)
			{
				s[a][b] = "";
			}
		}
	}
	
	/**
	* This method schedules the given course automatically and throws an exception if there are no available spots.
	* @param courseCode The course code of the course to schedule.
	* @param duration The intended duration of the scheduled course.
	*/
	public void setDayAndTime(String courseCode, int duration) // Automatic scheduler
	{
		try
		{
			ActiveCourse course = schedule.get(courseCode.toUpperCase());
			int[] availableIndexes = getAvailable(duration);
			int time = availableIndexes[0], day = availableIndexes[1];
			
			if(course.getTotalHours() == 3)
			{
				throw new LectureDurationException("The class has reached its maximum class time for the week. This class cannot be scheduled.");
			}
			
			else
			if(course.getTotalHours() + duration > 3)
			{
				throw new LectureDurationException("The duration entered will exceed the 3 hour alloted class time for the week. A class can be scheduled for " + (3 - course.getTotalHours()) + " hour(s) instead.");
			}
			
			else
			{
				if(time != -1 && day != -1)
				{
					for(int i = time; i < time + duration; i++)
					{
						s[i][day] = courseCode;
					}
					
					course.addLectureData(times[time], duration, days[day]);
				}
				
				else
				{
					throw new LectureTimeCollisionException("Automatic scheduling failed. No available time slots.");
				}
			}
		}
		
		catch(Exception ex)
		{
			System.out.println(ex.getClass().getSimpleName() + ": " + ex.getMessage());
		}
	}

	/**
	* This method searches the schedule to find a spot that can fit the duration.
	* @param duration The duration of the class.
	* @return The beginning coordinates of the avilable spots. -1 is returned if no spot was found.
	*/
	private int[] getAvailable(int duration)
	{
		int[] coordinates = {-1, -1};
		
		// iterate day by day - columns
		for(int day = 0; day < s[0].length; day++)
		{
			//iterate time by time within a given day - rows
			for(int time = 0; time < s.length; time++)
			{
				//if slot is empty AND duration does not stretch over the amount of legal times (until 4 pm), check that the slots ahead are also clear
				if(s[time][day].isEmpty() && time + duration <= s.length) 
				{
					for(int c = time; c < time + duration; c++)
					{
						if(!s[c][day].isEmpty())
						{
							coordinates[0] = -1;
							coordinates[1] = -1;
							break; //break out of loop and reset variables to default at first sign of blockage
						}

						coordinates[0] = time;
						coordinates[1] = day;
							
						if(c == time + duration - 1 && coordinates[0] != -1 && coordinates[1] != -1) //if the loop is on the last iteration and time and day are set, return the value since it's all clear
						{
							return coordinates; 
						}
					}
				}
			}
		}
		
		return coordinates;
	}
	
	/**
	* This method accepts the following parameters and schedules the course.
	* @param courseCode The course code.
	* @param day_of_week The day the course is to be scheduled for.
	* @param startTime The intended start time for the course.
	* @param duration The length of time in hours the class will run for.
	*/
	public void setDayAndTime(String courseCode, String day_of_week, int startTime, int duration)
	{
		try
		{
			ActiveCourse course = schedule.get(courseCode.toUpperCase());
			int time = getTime(startTime);
			int day = getDay(day_of_week);
		
			if(course.getTotalHours() == 3)
			{
				throw new LectureDurationException("The class has reached its maximum class time for the week. This class cannot be scheduled.");
			}
			
			else
			if(course.getTotalHours() + duration > 3)
			{
				throw new LectureDurationException("The duration entered will exceed the 3 hour alloted class time for the week. A class can be scheduled for " + (3 - course.getTotalHours()) + " hour(s) instead.");
			}
			
			else
			{
				for(int i = time; i < time + duration; i++)
				{
					if(!s[i][day].isEmpty())
					{
						throw new LectureTimeCollisionException("This time slot from " + times[i] + " on " + days[day] + " is unavailable for that duration period. Please choose another slot.", s, i, time, day);
					}
					
					else
					{
						s[i][day] = courseCode;
					}
				}
			
				course.addLectureData(startTime, duration, day_of_week);
			}
		}
		
		catch(Exception ex)
		{
			System.out.println(ex.getClass().getSimpleName() + ": " + ex.getMessage());
		}
	}
	
	/**
	* This method removes any occurrences of a course on the schedule.
	* @param courseCode The course to remove from the schedule.
	*/
	public void clearSchedule(String courseCode)
	{
		ActiveCourse course = schedule.get(courseCode.toUpperCase());
		course.resetLectureData();
		
		for(int a = 0; a < s.length; a++)
		{
			for(int b = 0 ; b < s[a].length; b++)
			{
				if(s[a][b].equalsIgnoreCase(courseCode))
				{
					s[a][b] = "";
				}
			}
		}
	}
		
	/**
	* This method prints the schedule.
	*/
	public void printSchedule()
	{
		String output = "\t";
	
		for(String day : days)
		{
			output += day + "\t";
		}
		
		output += "\n\n";
	
		for(int a = 0; a < s.length; a++)
		{
			output += times[a] + "\t";
		
			for(int b = 0 ; b < s[a].length; b++)
			{
				output += s[a][b] + "\t";
			}
			
			output += "\n\n";
		}
		
		System.out.println(output);
	}
	
	/**
	* This method accepts the String version of the day to locate and returns matching the index in the days array.
	* @param day The day to retrieve.
	* @return The index of the days array.
	*/
	private int getDay(String day)
	{
		int result = -1;
	
		for(int i = 0; i < days.length; i++)
		{
			if(days[i].equalsIgnoreCase(day))
			{
				result = i;
				break;
			}
		}
		
		return result;
	}
	
	/**
	* This method accepts the integer version of the time to locate and returns the matching index in the times array.
	* @param time The time to retrieve.
	* @return The index of the times array.
	*/
	private int getTime(int time)
	{
		int result = -1;
	
		for(int i = 0; i < times.length; i++)
		{
			if(times[i] == time)
			{
				result = i;
				break;
			}
		}
		
		return result;
	}
}

