/**
* Exception class that is thrown when the user enters an invalid time.
* @author Stephan Bissoon
*/

public class InvalidTimeException extends RuntimeException
{
	/**
	* Default constructor
	*/
	public InvalidTimeException()
	{
	
	}
	
	/**
	* Constructor that accepts a message and passes it to the superclass' constructor.
	* @param message The message
	*/
	public InvalidTimeException(String message)
	{
		super(message);
	}
}
