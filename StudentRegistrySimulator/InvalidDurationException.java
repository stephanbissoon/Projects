/**
* Exception class that is thrown when an invalid duration is entered.
* @author Stephan Bissoon
*/

public class InvalidDurationException extends RuntimeException
{
	/**
	* Default constructor
	*/
	public InvalidDurationException()
	{
	
	}
	
	/**
	* Constructor that accepts a message and passes it to the superclass' constructor.
	* @param message The message
	*/
	public InvalidDurationException(String message)
	{
		super(message);
	}
}
