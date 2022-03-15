import java.util.*;
import java.io.*;

/**
* This class is the registry that holds all students and courses that are currently in the university.
* @author Rajiv Bissoon 500954799
*/

public class Registry
{
   private ArrayList<Student>      students = new ArrayList<Student>();
   private ArrayList<ActiveCourse> courses  = new ArrayList<ActiveCourse>();
   
   public Registry()
   {
		try
		{
			String name = "", id = "", courseName = "", courseCode = "", descr = "", format = "";
			Scanner scanner;
			Random r = new Random();
			int random_index = 0;
			ArrayList<Student> list = new ArrayList<Student>();
			
			scanner = new Scanner(new File("students.txt"));
			
			while(scanner.hasNextLine() && scanner.hasNext())
			{
			
				if(scanner.hasNext())
				{
					name = scanner.next();
				}
				
				else
				{
					throw new Exception("A name is missing from the students.txt file.");
				}
				
				if(scanner.hasNext())
				{
					id = scanner.next();
				}
				
				else
				{
					throw new Exception("An ID is missing from the students.txt file.");
				}
				
				students.add(new Student(name, id));
			}
			
			Collections.sort(students);
			
			scanner = new Scanner(new File("courses.txt"));
			
			while(scanner.hasNextLine())
			{
				if(scanner.hasNextLine())
				{
					courseName = scanner.nextLine();
					
					if(courseName.trim().isEmpty())
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
					courseCode = scanner.nextLine();
					
					if(courseCode.trim().isEmpty())
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
					descr = scanner.nextLine();
					
					if(descr.trim().isEmpty())
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
					format = scanner.nextLine();
					
					if(format.trim().isEmpty())
					{
						throw new Exception("A course format is missing from the courses.txt file.");
					}
				}
				
				else
				{
					throw new Exception("A course format is missing from the courses.txt file.");
				}
				
				while(list.size() < 5)
				{
					random_index = r.nextInt(students.size());
					
					if(!list.contains(students.get(random_index)))
					{
						list.add(students.get(random_index));
						students.get(random_index).addCourse(courseName,courseCode,descr,format,"W2020",0);
					}
				}
				
				courses.add(new ActiveCourse(courseName,courseCode,descr,format,"W2020",list));
				
				list.clear();
			}
		}
		
		catch(Exception ex)
		{
			System.out.println(ex.getMessage());
			System.exit(0); //Kill program since file has to be amended.
		}
		
		/*// Add some students
		// in A2 we will read from a file
		Student s1 = new Student("JohnOliver", "34562");
		Student s2 = new Student("HarryWindsor", "38467");
		Student s3 = new Student("SophieBrown", "98345");
		Student s4 = new Student("FaisalQuereshi", "57643");
		Student s5 = new Student("GenghisKhan", "25347");
		Student s6 = new Student("SherryTu", "46532");
		students.add(s1);
		students.add(s2);
		students.add(s3);
		students.add(s4);
		students.add(s5);
		students.add(s6);
		// sort the students alphabetically - see class Student

		Collections.sort(students);
		ArrayList<Student> list = new ArrayList<Student>();

		// Add some active courses with students
		String courseName = "Computer Science II";
		String courseCode = "CPS209";
		String descr = "Learn how to write complex programs!";
		String format = "3Lec 2Lab";
		list.add(s2); list.add(s3); list.add(s4);
		courses.add(new ActiveCourse(courseName,courseCode,descr,format,"W2020",list));
		// Add course to student list of courses
		s2.addCourse(courseName,courseCode,descr,format,"W2020", 0); 
		s3.addCourse(courseName,courseCode,descr,format,"W2020", 0); 
		s4.addCourse(courseName,courseCode,descr,format,"W2020", 0); 

		// CPS511
		list.clear();
		courseName = "Computer Graphics";
		courseCode = "CPS511";
		descr = "Learn how to write cool graphics programs";
		format = "3Lec";
		list.add(s1); list.add(s5); list.add(s6);
		courses.add(new ActiveCourse(courseName,courseCode,descr,format,"F2020",list));
		s1.addCourse(courseName,courseCode,descr,format,"W2020", 0); 
		s5.addCourse(courseName,courseCode,descr,format,"W2020", 0); 
		s6.addCourse(courseName,courseCode,descr,format,"W2020", 0);

		// CPS643
		list.clear();
		courseName = "Virtual Reality";
		courseCode = "CPS643";
		descr = "Learn how to write extremely cool virtual reality programs";
		format = "3Lec 2Lab";
		list.add(s1); list.add(s2); list.add(s4); list.add(s6);
		courses.add(new ActiveCourse(courseName,courseCode,descr,format,"W2020",list));
		s1.addCourse(courseName,courseCode,descr,format,"W2020", 0); 
		s2.addCourse(courseName,courseCode,descr,format,"W2020", 0); 
		s4.addCourse(courseName,courseCode,descr,format,"W2020", 0); 
		s6.addCourse(courseName,courseCode,descr,format,"W2020", 0); */
	   
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
	   
	   for(Student student : students)
	   {
	   		if(student.equals(studentObj))
	   		{
	   			return false;
	   		}
	   }
	   
	   students.add(studentObj);
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
	   
	   for(Student student : students)
	   {
	   		if(student.getId().equalsIgnoreCase(studentId))
	   		{
	   			students.remove(student);
	   			
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
	   for (int i = 0; i < students.size(); i++)
	   {
		   System.out.println("ID: " + students.get(i).getId() + " Name: " + students.get(i).getName() );   
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
	   
	   for(Student student : students)
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
		   			for(ActiveCourse course : courses)
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
	   
	   for(ActiveCourse course : courses)
	   {
	   		if(course.getCode().equalsIgnoreCase(courseCode))
	   		{
	   			for(Student student : students)
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
	   for (int i = 0; i < courses.size(); i++)
	   {
		   ActiveCourse ac = courses.get(i);
		   System.out.println(ac.getDescription() + "\n");
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
   
		for(ActiveCourse course : courses)
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
   
	   for(ActiveCourse course : courses)
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
   
	   for(ActiveCourse course : courses)
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
   
	   	for(ActiveCourse course : courses)
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
	   for(Student student : students)
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
	   	for(Student student : students)
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
	   
	   	for(ActiveCourse course : courses)
		{
			if(course.getCode().equalsIgnoreCase(courseCode))
			{
				for(Student student : students)
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
}
