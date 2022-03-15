/**
* This class is the credit course for which a particular student is enrolled it. It is a subclass of the Course class. It holds the course's semester, grade and if it is active or not.
* @author Rajiv Bissoon 500954799
*/

public class CreditCourse extends Course
{
	private String semester;
	private double grade;
	private boolean active;
	
	/**
	* This constructor initalizes the variables from the superclass and the semester and grarde variables using the parameters. The course is set to active.
	* @param name The course name.
	* @param code The course code.
	* @param descr The course description.
	* @param fmt The course format.
	* @param semester The semester the course is being offered.
	* @param grade The grade a student current has for the course.
	*/
	public CreditCourse(String name, String code, String descr, String fmt, String semester, double grade)
	{
		super(name, code, descr, fmt);
		this.semester = semester;
		this.grade = grade;
		this.setActive();
	}

	/**
	* This method returns the active status of this course.
	* @return The active status of this course.	
	*/
	public boolean getActive()
	{
		// add code and remove line below
		return active;
	}

	/**
	* This method sets the active status of a class to true.
	*/
	public void setActive()
	{
		// add code
		active = true;
	}

	/**
	* This method sets the active status of a class to false.
	*/
	public void setInactive()
	{
		// add code
		active = false;
	}
	
	/**
	* This method sets the grade using the parameter that is passed.
	* @param grade The new grade.
	*/
	public void setGrade(double grade)
	{
		// add code
		this.grade = grade;
	}

	/**
	* This method sets the grade using the parameter that is passed.
	* @return The student's grade for this course.
	*/
	public double getGrade()
	{
		// add code
		return grade;
	}

	/**
	* This method displays the course information as well as the student's grade for this course.
	* @return The course information and student's grade for this course.
	*/
	public String displayGrade()
	{
		// Change line below and print out info about this course plus which semester and the grade achieved
		// make use of inherited method in super class
		return super.getInfo() + "\tSemester - " + semester + "\tGrade - " + this.convertNumericGrade(grade) + " [" + grade + "]";
	}
}
