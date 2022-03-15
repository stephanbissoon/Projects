import java.util.*;
import java.io.*;

/**
* This class is the registry that holds all students and courses that are enrolled in the university.
* @author Rajiv Bissoon 500954799
*/

public class Registry
{
   private TreeMap<String, Student>      students = new TreeMap<String, Student>();
   private TreeMap<String, ActiveCourse> courses  = new TreeMap<String, ActiveCourse>();
   private Scheduler scheduler = null;
   
   /**
   * Default constructor to initialize variables and create scheduler.
   */
   public Registry()
   {
		try
		{
			String data = "", name = "", id = "", courseName = "", courseCode = "", descr = "", format = "";
			Scanner scanner = null;
			ArrayList<Student> list = new ArrayList<Student>();
			Random r = new Random();
			String[] stu_data = null;
			Student stu = null;
			
			scanner = new Scanner(new File("students.txt"));
			
			while(scanner.hasNextLine())
			{
				data = scanner.nextLine().trim();
				
				if(!data.isEmpty())
				{
					stu_data = data.split("\\s+");
				
					if(stu_data.length == 2)
					{
						name = stu_data[0];
						id = stu_data[1];
					}
					
					else
					{
						throw new Exception("Missing or excess information. A student's name and ID is required in the students.txt file.");
					}
					
					students.put(id, new Student(name, id));
				}
			}
			
			stu_data = Arrays.copyOf(students.keySet().toArray(), students.size(), String[].class);
			//Collections.sort(students); TreeMap already sorts using the keys
			
			scanner = new Scanner(new File("courses.txt"));
			
			while(scanner.hasNextLine())
			{
				if(scanner.hasNextLine())
				{
					courseName = scanner.nextLine().trim();
					
					if(courseName.isEmpty())
					{
						throw new Exception("A course name is missing from the courses.txt file.");
					}
				}
				
				else
				{
					throw new Exception("A course name is missing from the courses.txt file.");
				}
				
				if(scanner.hasNextLine())
				{
					courseCode = scanner.nextLine().trim();
					
					if(courseCode.isEmpty())
					{
						throw new Exception("A code name is missing from the courses.txt file.");
					}
				}
				
				else
				{
					throw new Exception("A code name is missing from the courses.txt file.");
				}
				
				if(scanner.hasNextLine())
				{
					descr = scanner.nextLine().trim();
					
					if(descr.isEmpty())
					{
						throw new Exception("A course description is missing from the courses.txt file.");
					}
				}
				
				else
				{
					throw new Exception("A course description is missing from the courses.txt file.");
				}
				
				if(scanner.hasNextLine())
				{
					format = scanner.nextLine().trim();
					
					if(format.isEmpty())
					{
						throw new Exception("A course format is missing from the courses.txt file.");
					}
				}
				
				else
				{
					throw new Exception("A course format is missing from the courses.txt file.");
				}
				
				while(list.size() < 5) // Randomize students into courses
				{
					stu = students.get(stu_data[r.nextInt(students.size())]);
					
					if(!list.contains(stu))
					{
						list.add(stu);
						stu.addCourse(courseName,courseCode,descr,format,"W2020",0);
					}
				}
				
				courses.put(courseCode, new ActiveCourse(courseName, courseCode, descr, format, "W2020", list));
				
				list.clear();
			}
			
			scheduler = new Scheduler(courses);
		}
		
		catch(Exception ex)
		{
			System.out.println(ex.getMessage());
			System.exit(0); //Kill program since file has to be amended.
		}
   }
   
	/**
	* This method adds a new student to the current list of students in the registry.
	* @param name The name of the student.
	* @param id The ID of the student.
	* @return True is return if the student has been added. False is returned if the student is already in the list.
	*/
   public boolean addNewStudent(String name, String id)
   {
	   // Create a new student object
	   // check to ensure student is not already in registry
	   // if not, add them and return true, otherwise return false
	   // make use of equals method in class Student
	   
	   Student studentObj = new Student(name, id);
	   
	   for(Student student : students.values())
	   {
	   		if(student.equals(studentObj))
	   		{
	   			return false;
	   		}
	   }
	   
	   students.put(id, studentObj);
	   return true;
   }
   
	/**
	* This method removes a student from the registry using the student's ID
	* @param studentId The student's ID.
	* @return True if student was successfully removed, false if not.
	*/ 
   public boolean removeStudent(String studentId)
   {
	   // Find student in students arraylist
	   // If found, remove this student and return true
	   
	   for(Student student : students.values())
	   {
	   		if(student.getId().equalsIgnoreCase(studentId))
	   		{
	   			students.remove(student.getId());
	   			
	   			return true;
	   		}
	   }
	   
	   return false;
   }
   
	/**
	* This method prints all students in the registry.
	*/
   public void printAllStudents()
   {
	   for(Student student : students.values())
	   {
		   System.out.println("ID: " + student.getId() + "\tName: " + student.getName());   
	   }
	   
   }
   
   	/**
	* This method adds a student to a course using the student's ID and course code.
	* @param studentId The student's ID
	* @param courseCode The course code
	*/
   public void addCourse(String studentId, String courseCode) throws Exception
   {
	   // Find student object in registry (i.e. students arraylist)
	   // Check if student has already taken this course in the past Hint: look at their credit course list
	   // If not, then find the active course in courses array list using course code
	   // If active course found then check to see if student already enrolled in this course
	   // If not already enrolled
	   //   add student to the active course
	   //   add course to student list of credit courses with initial grade of 0
	   
	   	if(courseCode.length() != 6)
		{
			throw new Exception("The course code must be three letters proceeded by three numbers.");
		}
	
		for(int i = 0; i < courseCode.length(); i++)
		{
			if(i < 3 && !Character.isLetter(courseCode.charAt(i)))
			{
				throw new Exception("The course code must be three letters proceeded by three numbers.");
			}
			
			else
			if(i >= 3 && !Character.isDigit(courseCode.charAt(i)))
			{
				throw new Exception("The course code must be three letters proceeded by three numbers.");
			}
		}
	   
	   boolean taken_course = false;
	   
	   for(Student student : students.values())
	   {
	   		if(student.getId().equalsIgnoreCase(studentId)) //check if student is in registry
	   		{
	   			for(CreditCourse course : student.courses)
	   			{
	   				if(course.getCode().equalsIgnoreCase(courseCode)) //check if found student has already taken the course
	   				{
	   					System.out.println("The student is enrolled in or has already taken this course.");
	   					taken_course = true;
	   					break;
	   				}
	   			}
	   			
	   			if(!taken_course)
	   			{
		   			for(ActiveCourse course : courses.values())
		   			{
		   				if(course.getCode().equalsIgnoreCase(courseCode) && !course.checkStudentEnrollment(student))
		   				{
		   					course.addStudent(student);
							student.addCourse(course.getName(), course.getCode(), course.getDescr(), course.getFormat(), course.getSemester(), 0);
		   					System.out.println("The student has been successfully added to the course.");
		   					break;
		   				}
		   			}
	   			}
	   			
	   			break;
	   		}
	   }
   }
   
   	/**
	* This method removes a student from a course using the student's ID and course code.
	* @param studentId The student's ID
	* @param courseCode The course code
	*/
   public void dropCourse(String studentId, String courseCode) throws Exception
   {
	   // Find the active course
	   // Find the student in the list of students for this course
	   // If student found:
	   //   remove the student from the active course
	   //   remove the credit course from the student's list of credit courses
	   
	   if(courseCode.length() != 6)
		{
			throw new Exception("The course code must be three letters proceeded by three numbers.");
		}
	
		for(int i = 0; i < courseCode.length(); i++)
		{
			if(i < 3 && !Character.isLetter(courseCode.charAt(i)))
			{
				throw new Exception("The course code must be three letters proceeded by three numbers.");
			}
			
			else
			if(i >= 3 && !Character.isDigit(courseCode.charAt(i)))
			{
				throw new Exception("The course code must be three letters proceeded by three numbers.");
			}
		}
	   
	   for(ActiveCourse course : courses.values())
	   {
	   		if(course.getCode().equalsIgnoreCase(courseCode))
	   		{
	   			for(Student student : students.values())
	   			{
	   				if(student.getId().equalsIgnoreCase(studentId) && course.checkStudentEnrollment(student) && student.checkActiveCreditCourse(courseCode))
	   				{
	   					course.removeStudent(student);
	   					System.out.println("The student has been removed from this course.");
	   					student.removeActiveCourse(courseCode);
	   					
	   					break;
	   				}
	   			}
	   			
	   			break;
	   		}
	   }
   }
   
	/**
	* This method prints all courses currently in the registry.
	*/
   public void printActiveCourses()
   {
	   for(ActiveCourse course : courses.values())
	   {
			System.out.println(course.getDescription() + "\n");
	   }
   }
   
	/**
	* This method prints all the student in a given course.
	* @param courseCode The course code.
	*/
   public void printClassList(String courseCode) throws Exception
   {
   		if(courseCode.length() != 6)
		{
			throw new Exception("The course code must be three letters proceeded by three numbers.");
		}
	
		for(int i = 0; i < courseCode.length(); i++)
		{
			if(i < 3 && !Character.isLetter(courseCode.charAt(i)))
			{
				throw new Exception("The course code must be three letters proceeded by three numbers.");
			}
			
			else
			if(i >= 3 && !Character.isDigit(courseCode.charAt(i)))
			{
				throw new Exception("The course code must be three letters proceeded by three numbers.");
			}
		}
   
		for(ActiveCourse course : courses.values())
		{
			if(course.getCode().equalsIgnoreCase(courseCode))
			{
				course.printClassList();
				
				break;
			}
		}
   }
   
   	/**
	* This method sorts all students in a given course lexographically by their name.
	* @param courseCode The course code.
	*/
   public void sortCourseByName(String courseCode) throws Exception
   {
   		if(courseCode.length() != 6)
		{
			throw new Exception("The course code must be three letters proceeded by three numbers.");
		}
	
		for(int i = 0; i < courseCode.length(); i++)
		{
			if(i < 3 && !Character.isLetter(courseCode.charAt(i)))
			{
				throw new Exception("The course code must be three letters proceeded by three numbers.");
			}
			
			else
			if(i >= 3 && !Character.isDigit(courseCode.charAt(i)))
			{
				throw new Exception("The course code must be three letters proceeded by three numbers.");
			}
		}
   
	   for(ActiveCourse course : courses.values())
		{
			if(course.getCode().equalsIgnoreCase(courseCode))
			{
				course.sortByName();
				break;
			}
		}
   }
   
   	/**
	* This method sorts all students in a given course lexographically by their ID.
	* @param courseCode The course code.
	*/
   public void sortCourseById(String courseCode) throws Exception
   {
		if(courseCode.length() != 6)
		{
			throw new Exception("The course code must be three letters proceeded by three numbers.");
		}

		for(int i = 0; i < courseCode.length(); i++)
		{
			if(i < 3 && !Character.isLetter(courseCode.charAt(i)))
			{
				throw new Exception("The course code must be three letters proceeded by three numbers.");
			}
			
			else
			if(i >= 3 && !Character.isDigit(courseCode.charAt(i)))
			{
				throw new Exception("The course code must be three letters proceeded by three numbers.");
			}
		}
   
	   for(ActiveCourse course : courses.values())
		{
			if(course.getCode().equalsIgnoreCase(courseCode))
			{
				course.sortById();
				break;
			}
		}
   }
   
  	/**
	* This method prints all the students and their grades using a given course code.
	* @param courseCode The course code.
	*/
   public void printGrades(String courseCode) throws Exception
   {
   		if(courseCode.length() != 6)
		{
			throw new Exception("The course code must be three letters proceeded by three numbers.");
		}
	
		for(int i = 0; i < courseCode.length(); i++)
		{
			if(i < 3 && !Character.isLetter(courseCode.charAt(i)))
			{
				throw new Exception("The course code must be three letters proceeded by three numbers.");
			}
			
			else
			if(i >= 3 && !Character.isDigit(courseCode.charAt(i)))
			{
				throw new Exception("The course code must be three letters proceeded by three numbers.");
			}
		}
   
	   	for(ActiveCourse course : courses.values())
		{
			if(course.getCode().equalsIgnoreCase(courseCode))
			{
				course.printGrades();
				
				break;
			}
		}
   }
   
	/**
	* This method prints all the active courses a student is enrolled in using their student ID.
	* @param studentId The student's ID.
	*/
   public void printStudentCourses(String studentId)
   {
	   for(Student student : students.values())
		{
			if(student.getId().equalsIgnoreCase(studentId))
			{
				student.printActiveCourses();
				
				break;
			}
		}
   }
   
	/**
	* This method prints a student's transcript which includes all inactive courses and the grade assigned.
	* @param studentId The student's ID
	*/
   public void printStudentTranscript(String studentId)
   {
	   	for(Student student : students.values())
		{
			if(student.getId().equalsIgnoreCase(studentId))
			{
				student.printTranscript();
				
				break;
			}
		}
   }
   
   	/**
	* This method sets the final grade for a course a student is currently enrolled in. The course is set as inactive once the grade is set.
	* @param courseCode The course code.
	* @param studentId The student's ID.
	* @param grade The grade to set.
	*/
   public void setFinalGrade(String courseCode, String studentId, double grade) throws Exception
   {
	   // find the active course
	   // If found, find the student in class list
	   // then search student credit course list in student object and find course
	   // set the grade in credit course and set credit course inactive
	   
	   if(courseCode.length() != 6)
		{
			throw new Exception("The course code must be three letters proceeded by three numbers.");
		}
	
		for(int i = 0; i < courseCode.length(); i++)
		{
			if(i < 3 && !Character.isLetter(courseCode.charAt(i)))
			{
				throw new Exception("The course code must be three letters proceeded by three numbers.");
			}
			
			else
			if(i >= 3 && !Character.isDigit(courseCode.charAt(i)))
			{
				throw new Exception("The course code must be three letters proceeded by three numbers.");
			}
		}
	   
	   if(grade > 100.0 || grade < 0.0)
	   {
	   	throw new Exception("Grade must be between 0.0 and 100.0");
	   }
	   
		for(ActiveCourse course : courses.values())
		{
			if(course.getCode().equalsIgnoreCase(courseCode))
			{
				for(Student student : students.values())
				{
					if(student.getId().equalsIgnoreCase(studentId))
					{
						for(CreditCourse c_course : student.courses)
						{
							if(course.getCode().equalsIgnoreCase(c_course.getCode()) && c_course.getActive())
							{
								c_course.setGrade(grade);
								c_course.setInactive();
								
								System.out.println("The final grade for this course has been set for the selected student.");
							}
						}
					
						break;
					}
				}
				
				break;
			}
		}
   }
   
   	/**
   	* This method accepts the below parameters and automatically schedules the course. If no slot is available, an exception is thrown.
   	* @param courseCode The course code.
   	* @param duration The length of time in hours of the class.
   	*/
	public void setSchedule(String courseCode, int duration)
	{
		try
		{
			if(courseCode.length() != 6)
			{
				throw new Exception("The course code must be three letters proceeded by three numbers.");
			}
		
			for(int i = 0; i < courseCode.length(); i++)
			{
				if(i < 3 && !Character.isLetter(courseCode.charAt(i)))
				{
					throw new Exception("The course code must be three letters proceeded by three numbers.");
				}
				
				else
				if(i >= 3 && !Character.isDigit(courseCode.charAt(i)))
				{
					throw new Exception("The course code must be three letters proceeded by three numbers.");
				}
			}
			
			if(findCourse(courseCode) == null)
			{
				throw new UnknownCourseException("The entered course does not exist.");
			}
			
			if(duration < 1 || duration > 3)
			{
				throw new InvalidDurationException("The duration you have entered is not between 1 to 3 hours (inclusive).");
			}
			
			scheduler.setDayAndTime(courseCode, duration);
		}
		
		catch(Exception ex)
		{
			System.out.println(ex.getClass().getSimpleName() + ": " + ex.getMessage());
		}
	}
   
   /**
   * This method accepts the below parameters and sets the course on the schedule.
   * @param courseCode The course code.
   * @param day_of_week The day of the course.
   * @param startTime The start time of the course.
   * @param duration The length of time in hours of the class.
   */
   public void setSchedule(String courseCode, String day_of_week, int startTime, int duration)
   {
   		try
   		{
	   		if(courseCode.length() != 6)
			{
				throw new Exception("The course code must be three letters proceeded by three numbers.");
			}
		
			for(int i = 0; i < courseCode.length(); i++)
			{
				if(i < 3 && !Character.isLetter(courseCode.charAt(i)))
				{
					throw new Exception("The course code must be three letters proceeded by three numbers.");
				}
				
				else
				if(i >= 3 && !Character.isDigit(courseCode.charAt(i)))
				{
					throw new Exception("The course code must be three letters proceeded by three numbers.");
				}
			}
			
			if(findCourse(courseCode) == null)
			{
				throw new UnknownCourseException("The entered course does not exist.");
			}
			
			if(!checkDay(day_of_week))
			{
				throw new InvalidDayException("The day you have entered is incorrect. Please see this list of valid days: Mon, Tue, Wed, Thu, Fri");
			}
			
			if(duration < 1 || duration > 3)
			{
				throw new InvalidDurationException("The duration you have entered is not between 1 to 3 hours (inclusive).");
			}
			
			if(startTime < 800 || (startTime + duration * 100 > 1700))
			{
				throw new InvalidTimeException("The time you have specified is invalid. Please see this list of valid times: 800, 900, 1000, 1100, 1200, 1300, 1400, 1500, 1600. Classes cannot exceed 1700 (5 pm).");
			}
			
			scheduler.setDayAndTime(courseCode, day_of_week, startTime, duration);
		}
		
		catch(Exception ex)
		{
			System.out.println(ex.getClass().getSimpleName() + ": " + ex.getMessage());
		}
   }
   
   /**
   * This method accepts a course code and clears its corresponding course from the schedule.
   * @param courseCode The course to remove.
   */
   public void clearSchedule(String courseCode)
   {
   		try
   		{
   			if(courseCode.length() != 6)
			{
				throw new Exception("The course code must be three letters proceeded by three numbers.");
			}
		
			for(int i = 0; i < courseCode.length(); i++)
			{
				if(i < 3 && !Character.isLetter(courseCode.charAt(i)))
				{
					throw new Exception("The course code must be three letters proceeded by three numbers.");
				}
				
				else
				if(i >= 3 && !Character.isDigit(courseCode.charAt(i)))
				{
					throw new Exception("The course code must be three letters proceeded by three numbers.");
				}
			}
			
			if(findCourse(courseCode) == null)
			{
				throw new UnknownCourseException("The entered course does not exist.");
			}
			
			scheduler.clearSchedule(courseCode);
   		}
   		
   		catch(Exception ex)
		{
			System.out.println(ex.getClass().getSimpleName() + ": " + ex.getMessage());
		}
   }
   
   	/**
   	* This method prints the schedule.
   	*/
	public void printSchedule()
	{
		scheduler.printSchedule();
	}

	/**
   	* This method checks if an entered course code is valid or not.
   	* @param courseCode The course to find.
   	* @return The ActiveCourse if found, null if not.
   	*/
	private ActiveCourse findCourse(String courseCode)
	{
		for(String key : courses.keySet())
		{
			if(key.equalsIgnoreCase(courseCode))
			{
				return courses.get(key);
			}
		}
		
		return null;
	}
   
   	/**
   	* This method checks if an entered day is valid or not.
   	* @param day The day to check.
   	* @return true if valid, false if not.
   	*/
	private boolean checkDay(String day)
	{
		String[] days = {"Mon", "Tue", "Wed", "Thu", "Fri"};

		for(String ele : days)
		{
			if(ele.equalsIgnoreCase(day))
			{
				return true;
			}
		}
		
		return false;
	}
}
