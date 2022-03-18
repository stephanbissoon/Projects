/**
* Exception class that is thrown when a duration might exceed the 3 hour weekly maximum.
* @author Stephan Bissoon
*/

public class LectureDurationException extends RuntimeException
{
	/**
	* Default constructor
	*/
	public LectureDurationException()
	{
	
	}
	
	/**
	* Constructor that accepts a message and passes it to the superclass' constructor.
	* @param message The message
	*/
	public LectureDurationException(String message)
	{
		super(message);
	}
}
