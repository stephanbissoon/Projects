/**
* Exception class that is thrown when a invalid day is entered by the user.
* @author Rajiv Bissoon 500954799
*/

public class InvalidDayException extends RuntimeException
{
	/**
	* Default constructor
	*/
	public InvalidDayException()
	{
	
	}
	
	/**
	* Constructor that accepts a message and passes it to the superclass' constructor.
	* @param message The message
	*/
	public InvalidDayException(String message)
	{
		super(message);
	}
}
