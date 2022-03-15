import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Client
{
	private Socket clientSocket = null;
	private PrintWriter pw = null;
	private BufferedReader br = null;
	
	/**
	*	Builds the GUI and opens a connection to the server once a Client object is created.
	*/
	public Client()
	{
		buildGui();
		openConnection();
	}
	
	/**
	* Creates the GUI with a date button, time button and a text field for the response.
	*/
	private void buildGui()
	{
			JTextArea txt = new JTextArea();
			txt.setBorder(BorderFactory.createLineBorder(Color.black));
			txt.setLineWrap(true);
			txt.setBounds(50, 150, 250, 40);
			txt.setEditable(false);
			txt.setFont(new Font("Times New Roman", Font.PLAIN, 28));
			
			/*****************************************************/
			
			JButton b1 = new JButton("Date");  
			b1.setBounds(50, 50, 100, 30);  
			
			b1.addActionListener(new ActionListener()
			{
				/**
				* When the Date button is clicked, a request containing "1" is sent to the server and the response is placed into the JTextArea.
				*/
				@Override
				public void actionPerformed(ActionEvent e)
				{
					try
					{
						pw.println("1");
						txt.setText("Date: " + br.readLine());
					}
					
					catch(Exception ex)
					{
						ex.printStackTrace();
					}
				}
			});
			
			/*****************************************************/
			
			JButton b2 = new JButton("Time");  
			b2.setBounds(200, 50, 100, 30);  
			
			b2.addActionListener(new ActionListener()
			{
				/**
				* When the Time button is clicked, a request containing "2" is sent to the server and the response is placed into the JTextArea.
				*/
				@Override
				public void actionPerformed(ActionEvent e)
				{
					try
					{
						pw.println("2");
						txt.setText("Time: " + br.readLine());
					}
					
					catch(Exception ex)
					{
						ex.printStackTrace();
					}
				}
			});
			
			/*****************************************************/
			
			JFrame f = new JFrame("Client");
			
			f.addWindowListener(new WindowAdapter()
			{
				/**
				* When the window is closed, the server is shut down.
				*/
				@Override
				public void windowClosing(WindowEvent e)
				{	
					try
					{
						pw.println("0");
						f.dispose(); // Releases the resources that was used to create the window.
						closeConnection();
						System.out.println("Window closed. Program ended.");
					}
					
					catch(Exception ex)
					{
						ex.printStackTrace();
					}
				}
			});
			
			f.add(b1);
			f.add(b2);
			f.add(txt);
			f.setSize(350, 350);
			f.setLocation(300,200);
			f.setResizable(false);
			f.setLayout(null);
			f.setVisible(true);
	}
	
	/**
	* Opens a connection to the server.
	*/
	private void openConnection()
	{
		try
		{	
			clientSocket = new Socket("localhost", 5000);
			System.out.println("Connection opened on " + clientSocket);
			pw = new PrintWriter(clientSocket.getOutputStream(), true); // Used for sending data to the server.
			br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())); // Used for receiving data from the server.
		}
		
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	/**
	* Closes the connection between the client and server.
	*/
	private void closeConnection()
	{
		try
		{
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
		Client client = new Client();
	}
}
