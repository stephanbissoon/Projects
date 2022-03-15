/**
* Exception class that is thrown when a user enters a course that does not exist.
* @author Rajiv Bissoon 500954799
*/

public class UnknownCourseException extends RuntimeException
{
	/**
	* Default constructor
	*/
	public UnknownCourseException()
	{
	
	}
	
	/**
	* Constructor that accepts a message and passes it to the superclass' constructor.
	* @param message The message
	*/
	public UnknownCourseException(String message)
	{
		super(message);
	}
}
