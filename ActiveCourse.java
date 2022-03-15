import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
* This class is a subclass of the Course class. It holds the students that are enrolled into a course and which semester the course is being offered.
* @author Rajiv Bissoon 500954799
*/
 
public class ActiveCourse extends Course
{
	private ArrayList<Student> students; 
	private String             semester;

	/**
	* This constructor initializes the variables in the super class, the semester the class is being offered and the students which are enrolled into this class.
	*/
	public ActiveCourse(String name, String code, String descr, String fmt, String semester, ArrayList<Student> students)
	{
		super(name, code, descr, fmt);
		this.semester = semester;
		this.students = new ArrayList<>();
		this.students.addAll(students);
	}

	/**
	* This method returns the semester the course is being offered in.
	* @return The semester the course is being offered in.
	*/
	public String getSemester()
	{
		return semester;
	}

	/**
	* This method prints the list of students which are enrolled in a course.
	*/
	public void printClassList()
	{
		for(Student student : students)
		{
			System.out.println("ID: " + student.getId() + "\tName: " + student.getName());
		}
	}

	/**
	* This method prints the grades for all the students enrolled in this class.
	*/
	public void printGrades()
	{
		for(Student student : students)
		{
			System.out.println("ID: " + student.getId() + "\tName: " + student.getName() + "\tGrade: " + this.getGrade(student.getId()));
		}
	}

	/**
	* This method returns the matching student's grade
	* @param studentId The student's ID
	* @return The student's grade
	*/
	public double getGrade(String studentId)
	{
		// Search the student's list of credit courses to find the course code that matches this active course
		// see class Student method getGrade() 
		// return the grade stored in the credit course object
		
		for(Student student : students)
		{
			if(student.getId().equalsIgnoreCase(studentId))
			{
				return student.getGrade(this.getCode());
			}
		}
		
		return 0;
	}

	/**
	* This method overrides the method in the Course superclass and returns a padded string of the superclass' getDescription method, semester and amount of students.
	* @return Superclass' getDescription method, semester and amount of students.
	*/
	public String getDescription()
	{
		return super.getDescription() + "\n" + semester + "\n" + students.size();
	}


	/**
	* This method sorts the list of students alphabetically by their name.
	*/
	public void sortByName()
	{
		Collections.sort(students, new NameComparator());
	}

	/**
	* This class is used to define the Comparator for the student name.
	*/
	private class NameComparator implements Comparator<Student>
	{
		public int compare(Student s1, Student s2)
		{
			return s1.getName().toUpperCase().compareTo(s2.getName().toUpperCase());	
		}
	}

	/**
	* This method sorts the list of students by their IDs
	*/
	public void sortById()
	{
		Collections.sort(students, new IdComparator());
	}

	/**
	* This class is used to define the Comparator for the student IDs.
	*/
	private class IdComparator implements Comparator<Student>
	{
		public int compare(Student s1, Student s2)
		{
			return s1.getId().compareTo(s2.getId());	
		}
	}

	/**
	* This method accepts a student object and adds it to the list of students.
	* @param The student object.
	*/
	public void addStudent(Student student)
	{
		students.add(student);
	}
	
	/**
	* This method accepts a student object and removes it from the list of students.
	* @param The student object.
	*/
	public void removeStudent(Student student)
	{
		students.remove(student);
	}
	
	/**
	* This method accepts a student object and checks to see if that student is currently enrolled in this course. If they are not, false is returned, else, true.
	* @param student The student object.
	* @return If the student exists in the list, return true, else, return false.
	*/
	public boolean checkStudentEnrollment(Student student)
	{
		for(Student s : students)
		{
			if(s.equals(student))
			{
				return true;
			}
		}
		
		return false;
	}
}
