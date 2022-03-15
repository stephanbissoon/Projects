import java.util.ArrayList;
import java.util.Scanner;

/**
* This main class serves as the simulator for the registry.
* @author Tim McInerney & Rajiv Bissoon (500954799)
*/

public class StudentRegistrySimulator 
{
  public static void main(String[] args)
  {
  	try
  	{
		Scanner scanner = null, commandLine = null;
		String inputLine = "", command = "";

		Registry registry = new Registry();
		scanner = new Scanner(System.in);

		System.out.print(">");

		while (scanner.hasNextLine())
		{
		  inputLine = scanner.nextLine();
		  
		  if (inputLine == null || inputLine.equals(""))
		  {
		  	System.out.println("No command detected.");
		  	System.out.print(">");
		  	continue;
		  };
		  
		  commandLine = new Scanner(inputLine);
		  
		  if(commandLine.hasNext())
		  {
			command = commandLine.next();
		  }
		  
		  else
		  {
		  	command = "";
		  }
		  
		  if (command == null || command.equals(""))
		  {
		  	System.out.println("No command detected.");
		  	System.out.print(">");
		  	continue;
		  }
		  
		  else if (command.equalsIgnoreCase("L") || command.equalsIgnoreCase("LIST"))
		  {
			  registry.printAllStudents();
		  }
		  else if (command.equalsIgnoreCase("Q") || command.equalsIgnoreCase("QUIT"))
			  return;
		  
		  else if (command.equalsIgnoreCase("REG"))
		  {
				// register a new student in registry
				// get name and student id string 
				// e.g. reg JohnBoy 74345
				// Checks:
				//  ensure name is all alphabetic characters
				//  ensure id string is all numeric characters

				String name = "", id = "";

				if(commandLine.hasNext())
				{
					name = commandLine.next();
				}
				
				if(commandLine.hasNext())
				{
					id = commandLine.next();
				}

				if(!name.isEmpty() && !id.isEmpty())
				{
					if(!isStringOnlyAlphabet(name))
					{
						System.out.println("Name has an illegal character. Please try again.");
						continue;
					}
					
					if(!isNumeric(id))
					{
						System.out.println("The ID must only be 5 digit numeric. Please try again.");
						continue;
					}
					
					if(registry.addNewStudent(name, id))
					{
						System.out.println("Student " + name + " has been registered.");
					}
					
					else
					{
						System.out.println("Student " + name + " is already registered");
					}
				}
				
				else
				{
					System.out.println("Missing information. Please ensure that a name and ID is entered.");
				}
		  }
		  else if (command.equalsIgnoreCase("DEL"))
		  {
				// delete a student from registry
				// get student id
				// ensure numeric
				// remove student from registry
				
				String id = "";
				
				if(commandLine.hasNext())
				{
					id = commandLine.next();
				}
				
				if(!id.isEmpty())
				{
					if(!isNumeric(id))
					{
						System.out.println("The ID must only be a 5 digit numeric. Please try again.");
						continue;
					}
					
					if(registry.removeStudent(id))
					{
						System.out.println("Student has been successfully removed.");
					}
					
					else
					{
						System.out.println("Student was not removed because ID does not exist. Please try again.");
					}
				}
				
				else
				{
					System.out.println("Missing information. Please ensure that an ID is entered.");
				}
		  }
		  
		  else if (command.equalsIgnoreCase("ADDC"))
		  {
			 // add a student to an active course
			 // get student id and course code strings
			 // add student to course (see class Registry)
			 
			String id = "";
			String courseCode = "";
			
			if(commandLine.hasNext())
			{
				id = commandLine.next();
			}
			
			if(commandLine.hasNext())
			{
				courseCode = commandLine.next();
			}
			
			if(!id.isEmpty() && !courseCode.isEmpty())
			{
				if(!isNumeric(id))
				{
					System.out.println("The ID must only be a 5 digit numeric. Please try again.");
					continue;
				}
			 
				registry.addCourse(id, courseCode);
			}
			
			else
			{
				System.out.println("Missing information. Please ensure an ID and course code has been entered.");
			}
		  }
		  else if (command.equalsIgnoreCase("DROPC"))
		  {
			  // get student id and course code strings
			  // drop student from course (see class Registry)
			  
			String id = "";
			String courseCode = "";
			
			if(commandLine.hasNext())
			{
				id = commandLine.next();
			}
			
			if(commandLine.hasNext())
			{
				courseCode = commandLine.next();
			}

			if(!id.isEmpty() && !courseCode.isEmpty())
			{
				if(!isNumeric(id))
				{
					System.out.println("The ID must only be a 5 digit numeric. Please try again.");
					continue;
				}
				 
				registry.dropCourse(id, courseCode);
			}
			
			else
			{
				System.out.println("Missing information. Please ensure an ID and course code is entered.");
			}
		  }
		  else if (command.equalsIgnoreCase("PAC"))
		  {
			  // print all active courses
			  
			  registry.printActiveCourses();
		  }		  
		  else if (command.equalsIgnoreCase("PCL"))
		  {
			  // get course code string
			  // print class list (i.e. students) for this course
			  	
			  	if(commandLine.hasNext())
			  	{ 
					registry.printClassList(commandLine.next());
			  	}

				else
				{
					System.out.println("Missing information. Please ensure a course code is entered.");
				}
		  }
		  else if (command.equalsIgnoreCase("PGR"))
		  {
			  // get course code string
			  // print name, id and grade of all students in active course
			  	
			  	if(commandLine.hasNext())
			  	{
					registry.printGrades(commandLine.next());
			  	}

				else
				{
					System.out.println("Missing information. Please ensure a course code is entered.");
				}
		  }
		  else if (command.equalsIgnoreCase("PSC"))
		  {
			  // get student id string
			  // print all credit courses of student
			  
			String id = "";

			if(commandLine.hasNext())
			{
				id = commandLine.next();

				if(!isNumeric(id))
				{
					System.out.println("The ID must only be a 5 digit numeric. Please try again.");
					continue;
				}

				registry.printStudentCourses(id);
			}

			else
			{
				System.out.println("Missing information. Please ensure an ID is entered.");
			}
		  }
		  else if (command.equalsIgnoreCase("PST"))
		  {
			  // get student id string
			  // print student transcript
			  
			String id = "";

			if(commandLine.hasNext())
			{
				id = commandLine.next();

				if(!isNumeric(id))
				{
					System.out.println("The ID must only be a 5 digit numeric. Please try again.");
					continue;
				}

				registry.printStudentTranscript(id);
			}

			else
			{
				System.out.println("Missing information. Please ensure an ID is entered.");
			}
		  }
		  else if (command.equalsIgnoreCase("SFG"))
		  {
			  // set final grade of student
			  // get course code, student id, numeric grade
			  // use registry to set final grade of this student (see class Registry)
			  
				String courseCode = "";
				String id = "";
				String grade = "";
				
				if(commandLine.hasNext())
				{
					courseCode = commandLine.next();
				}
				
				if(commandLine.hasNext())
				{
					id = commandLine.next();
				}
				
				if(commandLine.hasNext())
				{
					grade = commandLine.next();
				}

				if(!courseCode.isEmpty() && !id.isEmpty() && !grade.isEmpty())
				{
					if(!isNumeric(id))
					{
						System.out.println("The ID must only be a 5 digit numeric. Please try again.");
						continue;
					}
					
					if(!isDouble(grade) && !isNumeric(grade))
					{
						System.out.println("The grade must only be numerical or decimal. Please try again.");
						continue;
					}
				  
					registry.setFinalGrade(courseCode, id, Double.parseDouble(grade));
				}
				
				else
				{
					System.out.println("Missing information. Please ensure that a course code, ID and grade is entered.");
				}
		  }
		  else if (command.equalsIgnoreCase("SCN"))
		  {
			  // get course code
			  // sort list of students in course by name (i.e. alphabetically)
			  // see class Registry
			  	
		  	if(commandLine.hasNext())
		  	{
				registry.sortCourseByName(commandLine.next());
		  	}

			else
			{
				System.out.println("Missing information. Please ensure a course code is entered.");
			}
		  }
		  else if (command.equalsIgnoreCase("SCI"))
		  {
			// get course code
			// sort list of students in course by student id
			// see class Registry
			  	
		  	if(commandLine.hasNext())
		  	{
				registry.sortCourseById(commandLine.next());
		  	}

			else
			{
				System.out.println("Missing information. Please ensure a course code is entered.");
			}
		  }
		  
		  System.out.print("\n>");
		}
	}
  
  	catch(Exception ex)
	{
		System.out.println(ex.getMessage());
	}
  }
  
	/**
	* This method checks if the passed string is only made up of alphabetical characters.
	* @param str The string to check.
	* @return True if the string is legal, false if not.
	*/
	private static boolean isStringOnlyAlphabet(String str) 
	{ 
	  // write method to check if string str contains only alphabetic characters 
	  
		for(char letter : str.toCharArray())
		{
			if(!Character.isLetter(letter))
			{
				return false;
			}
		}
	  
	  return true;
	} 

	/**
	* This method checks if the passed string is only made up of numerical characters.
	* @param str The string to check.
	* @return True if the string is legal, false if not.
	*/
	public static boolean isNumeric(String str)
	{
	  // write method to check if string str contains only numeric characters
	  
	  	if(str.length() != 5)
		{
			return false;
		}
	  
		for(char letter : str.toCharArray())
		{
			if(!Character.isDigit(letter))
			{
				return false;
			}
		}
	  
		return true;
	}
	
	/**
	* This method checks if the passed string is a double number.
	* @param str The string to check.
	* @return True if the string is legal, false if not.
	*/
	public static boolean isDouble(String str)
	{
		int dot_counter = 0;
	
		for(char character : str.toCharArray())
		{
			if(character == '.')
			{
				dot_counter++;
			}

			if((!Character.isDigit(character) && character != '.') || dot_counter > 1)
			{
				return false;
			}
		}
		
		return true;
	}
}
