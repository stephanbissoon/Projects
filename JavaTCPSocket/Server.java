import java.io.*;
import java.net.*;
import java.util.*; 
import java.text.*;

public class Server extends Thread
{
	private ServerSocket serverSocket = null;
	private Socket clientSocket = null;
	private PrintWriter pw = null;
	private BufferedReader br = null;
	private Calendar c = null;
	private SimpleDateFormat sdf = null;
	private String clientData = "";

	/**
	* Set the clientSocket the incoming parameter.
	*/
	public Server(Socket cs)
	{
		clientSocket = cs;
	}

	/**
	* Creates a Calendar instance then converts it to the h:m:s format.
	*
	* @return The formatted time in the h:m:s format.
	*/
	public String getTime()
	{
		c = Calendar.getInstance();
		sdf = new SimpleDateFormat("HH:mm:ss");
		
		return sdf.format(c.getTime());
	}

	/**
	* Creates a Calendar instance then converts it to the m/d/y format.
	*
	* @return The formatted date in the m/d/y format.
	*/
	public String getDate()
	{
		c = Calendar.getInstance();
		sdf = new SimpleDateFormat("MM/dd/y");
		
		return sdf.format(c.getTime());
	}
	
	/**
	* Opens a socket for the server and client. Takes input from the client then sends a response to the client.
	*/
	@Override
	public void run()
	{
		try
		{
			System.out.println("Connection opened on " + clientSocket);
			pw = new PrintWriter(clientSocket.getOutputStream(), true); // Used for sending responses to the client
			br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())); // Used for receiving data from the client
			
			while(!(clientData = br.readLine()).equalsIgnoreCase("0")) // When 0 is sent to the server, loop stops, and sockets and I/O connections are closed.
			{
				if(clientData.equalsIgnoreCase("1")) // When 1 is sent to the server, the date is sent back to the client.
				{
					pw.println(getDate());
				}
				
				else if(clientData.equalsIgnoreCase("2")) // When 2 is sent to the server, the time is sent back to the client.
				{
					pw.println(getTime());
				}
				
				else
				{
					pw.println("Incorrect Input Received"); // In the event neither 1 or 2 are sent to the server, an error message is sent to the client.
				}
			}
			
			System.out.println("Connection closing on " + clientSocket);
			clientSocket.close();
			pw.close();
			br.close();
		}
		
		catch(Exception ex)
		{
			ex.printStackTrace();
		}		
	}
	
	public static void main(String[] args)
	{
		try
		{
			ServerSocket serverSocket = new ServerSocket(5000); // Create server socket on port 5000.
			System.out.println("Server running on port 5000");
			
			while(true)
			{
				new Server(serverSocket.accept()).start(); // Start a new thread when a client connection is made.
			}
		}
		
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
}
