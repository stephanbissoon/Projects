/**
* Exception class that is thrown when there is a possible clash of courses in the schedule
* @author Stephan Bissoon
*/

public class LectureTimeCollisionException extends RuntimeException
{
	/**
	* Default constructor
	*/	
	public LectureTimeCollisionException()
	{
	
	}
	
	/**
	* Constructor that accepts a message and passes it to the superclass' constructor. It also frees any time slots which may have been reserved before the exception.
	* @param message The message
	* @param schedule The current class schedule
	* @param index The index of the schedule blocks that are to be cleared.
	* @param time The time index of the 2D array
	* @param day The day index of the 2D array
	*/
	public LectureTimeCollisionException(String message, String[][] schedule, int index, int time, int day)
	{
		super(message);
	
		for(int j = time; j < index; j++) // Clear the previous time slots that may have been set.
		{
			schedule[j][day] = "";
		}
	}

	/**
	* Constructor that accepts a message and passes it to the superclass' constructor.
	* @param message The message
	*/
	public LectureTimeCollisionException(String message)
	{
		super(message);
	}
}
