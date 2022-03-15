import java.util.ArrayList;

/**
* The student class holds the information of the student that is their name, ID and courses they are currently taking and have already taken.
* @author Rajiv Bissoon 500954799
*/
public class Student implements Comparable<Student>
{
	private String name;
	private String id;
	public  ArrayList<CreditCourse> courses;

	/**
	* This constructor initializes the name and ID variables to the parameters that are passed in. The courses are initialized to an empty list.
	* @param name The student's name.
	* @param id The Student's ID number.
	*/
	public Student(String name, String id)
	{
		this.name = name;
		this.id   = id;
		courses   = new ArrayList<CreditCourse>();
	}

	/**
	* This method returns the student's ID.
	* @return The student's ID.
	*/
	public String getId()
	{
		return id;
	}

	/**
	* This method returns the student's name.
	* @return The student's name.
	*/
	public String getName()
	{
		return name;
	}

	/**
	* This method adds a new credit course to a student's list of courses.
	*/
	public void addCourse(String courseName, String courseCode, String descr, String format, String sem, double grade)
	{
		// create a CreditCourse object
		// set course active
		// add to courses array list
		
		courses.add(new CreditCourse(courseName, courseCode, descr, format, sem, grade));
	}

	/**
	* This method returns a student's grade.
	* @return The student's grade.
	*/
	public double getGrade(String code)
	{
		for(CreditCourse course : courses)
		{
			if(code.equalsIgnoreCase(course.getCode()))
			{
				return course.getGrade();
			}
		}
		
		return 0;
	}

	/**
	* This method prints the student's transcript.
	*/
	public void printTranscript()
	{
		for(CreditCourse course : courses)
		{
			if(!course.getActive())
			{
				System.out.println(course.displayGrade());
			}
		}
	}

	/**
	* This method prints all the courses (active) that the student is currently enrolled in.
	*/
	public void printActiveCourses()
	{
		for(CreditCourse course : courses)
		{
			if(course.getActive())
			{
				System.out.println(course.getDescription());
			}
		}
	}

	/**
	* This method checks if the passed course code is an active credit course or not.
	* @param courseCode The course code to check.
	* @return True if the course is active, false if it is not.
	*/
	public boolean checkActiveCreditCourse(String courseCode)
	{
		for(CreditCourse course : courses)
		{
			if(course.getActive() && course.getCode().equalsIgnoreCase(courseCode))
			{
				return true;
			}
		}

		return false;
	}

	/**
	* This method removes a student from an active course which they are currently enrolled in.
	*/
	public void removeActiveCourse(String courseCode)
	{	
		for(CreditCourse course : courses)
		{
			if(course.getCode().equalsIgnoreCase(courseCode))
			{
				courses.remove(course);
				System.out.println("The course has been removed from the list of student's courses.");
				break;
			}
		}
	}

	/**
	* This method returns a String value of the student's ID and their name.
	* @return The student's ID and name.
	*/
	public String toString()
	{
		return "Student ID: " + id + "\tName: " + name;
	}

	/**
	* This method checks if two student objects are equal using their name and ID.
	* @return True if they are equal and false if they are not.
	*/
	public boolean equals(Object other)
	{
		Student otherStudent = (Student)other;
	
		return this.getName().equalsIgnoreCase(otherStudent.getName()) && this.getId().equalsIgnoreCase(otherStudent.getId());
	}
	
	/**
	* This method compares two student objects using their name to lexographically sort them.
	* @return -1 if the first object is lexographically smaller, 1 if the first object is lexographically larger and 0 if they are lexographically equal.
	*/
	public int compareTo(Student other)
	{
		return this.getName().toUpperCase().compareTo(other.getName().toUpperCase());
	}
}
