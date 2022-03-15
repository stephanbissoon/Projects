/**
* The course class holds the code, name, description and format of a class
* @author Tim McInerney & Rajiv Bissoon (500954799)
*/

public class Course 
{
	private String code;
	private String name;
	private String description;
	private String format;

	/**
	* This default course constructor initializes the course code, name, description and format to blank strings.
	*/
	public Course()
	{
		this.code        = "";
		this.name        = "";
		this.description = "";
		this.format      = "";
	}

	/**
	* This course constructor initializes the course code, name, descrption and format to the variables that are passed as parameters.
	* @param name The course name.
	* @param code The course code.
	* @param descr The course description.
	* @param fmt The course format.
	*/
	public Course(String name, String code, String descr, String fmt)
	{
		this.code        = code;
		this.name        = name;
		this.description = descr;
		this.format      = fmt;
	}

	/**
	* This method gets/returns the course code
	* @return The course code	
	*/
	public String getCode()
	{
		return code;
	}

	/**
	* This method gets/returns the course name.
	* @return The course name.
	*/
	public String getName()
	{
		return name;
	}

	/**
	* This method gets/returns the course format.
	* @return The course format.
	*/
	public String getFormat()
	{
		return format;
	}

	/**
	* This method gets/returns the course description that is the course code, name, description and format.
	* @return The detailed course description.
	*/
	public String getDescription()
	{
		return code +" - " + name + "\n" + description + "\n" + format;
	}

	/**
	* This method gets/returns the plain course description.
	* @return The plain course description.
	*/
	public String getDescr()
	{
		return description;
	}

	/**
	* This method gets/returns the course info that is the course code and name.
	* @return The course info.
	*/
	public String getInfo()
	{
		return code +" - " + name;
	}

	/**
	* This method converts the score/grade passed in and returns the letter grade.
	* @param score The numerical grade.
	* @return The letter grade.
	*/
	public static String convertNumericGrade(double score)
	{
		// fill in code
		if(score >= 90)
		{
			return "A+";
		}
		
		else
		if(score <= 89 && score >= 85)
		{
			return "A";
		}
		
		else
		if(score <= 84 && score >= 80)
		{
			return "A-";
		}
		
		else
		if(score <= 79 && score >= 77)
		{
			return "B+";
		}
		
		else
		if(score <= 76 && score >= 73)
		{
			return "B";
		}
		
		else
		if(score <= 72 && score >= 70)
		{
			return "B-";
		}
		
		else
		if(score <= 69 && score >= 67)
		{
			return "C+";
		}
		
		else
		if(score <= 66 && score >= 63)
		{
			return "C";
		}
		
		else
		if(score <= 62 && score >= 60)
		{
			return "C-";
		}
		
		else
		if(score <= 59 && score >= 57)
		{
			return "D+";
		}
		
		else
		if(score <= 56 && score >= 53)
		{
			return "D";
		}
		
		else
		if(score <= 52 && score >= 50)
		{
			return "D-";
		}
		
		else
		{
			return "F";
		}
	}
}
