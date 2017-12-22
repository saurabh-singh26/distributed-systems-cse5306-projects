/*Submitted by 1001568347 - Saurabh Singh for CS5306 - Fall 2017 Lab 1 - Thesaurus Client and Server
 * References:
 * 1) https://www.javatpoint.com/java-swing: For server GUI creation
 * 2) https://docs.oracle.com/javase/tutorial/displayCode.html?code=https://docs.oracle.com/javase/tutorial/networking/sockets/examples/KKMultiServer.java
 * */

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.ServerSocket;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

//The class takes care for the Server UI and listens for any new client connection request
public class ServerUI extends JFrame{
	static ThesaurusServer server;
	JPanel panel = new JPanel();
	static JTextField textField;
	
	//Constructor for ServerUI class. It calls initThesaurusServerUI() method which create GUI for server
	public ServerUI() {
		initThesaurusServerUI();
	}
	
	//The method receives no arguments and returns nothing
	private void initThesaurusServerUI() {
		setTitle("Thesaurus Server"); //Defines the title of server window
		setSize(300, 200); //Defines the size of server window
		setLocationRelativeTo(null); //Places the server window in center of the screen
		setDefaultCloseOperation(EXIT_ON_CLOSE); //Defines the behavior when user closes the server window. "EXIT_ON_CLOSE" would close the application using System.exit method
	    getContentPane().add(panel); //Returns the content pane and adds panel to it
	    panel.setLayout(null); //Not using any of the pre-defined swing layouts
	    
	    //Adding all the objects on the panel in separate methods
	    addTextField();
	    addQuitButton();
	}

	/*Creating a text field which displays the word received from from client
	Setting its relative position within the panel and adding it to the panel.*/
	private void addTextField() {
		textField  = new JTextField();
	    textField.setBounds(30, 20, 160, 30);
	    panel.add(textField);
	}
	
	//Creating a quit button for the server, setting its position and adding it to the panel
	private void addQuitButton() {
		JButton quitButton = new JButton("Quit");
	    quitButton.setBounds(30, 60, 70, 30);
	    quitButton.addActionListener(new ActionListener() {	    	
	    	public void actionPerformed(ActionEvent event) {
	    		//Closes the server when "Quit" button is clicked by using System.exit method
	    		System.exit(0);
	    	}
	    });
	    panel.add(quitButton);
	}
	
	//A method to write the word obtained from the client to the text field
	public static void setTextField(String inputWord) {
//		if(inputWord!=null) System.out.println("Entering: " + inputWord); For Debugging//ignore
		textField.setText(inputWord);
	}
	
	public static void main(String[] args) throws IOException {
		//Creates the ServerUI object which creates server GUI and call setVisible to true to display it
		ServerUI serverUIObj = new ServerUI();
		serverUIObj.setVisible(true);
		
		//Checks for any port entered entered by the user. If not entered then server will be created on port 9999 by default
		int port = (args.length!=-0) ? Integer.parseInt(args[0]) : 9999;
		ServerSocket listener = null; //Initialize a server socket object and assign it to null
		try {
			listener = new ServerSocket(port); //Create a new server socket object with the entered port number
            while (true) { //Server enters into listen mode for connection requests from client
            	//Creates a new ThesaurusServer thread for any new client connection request to make the server multithreaded 
                new ThesaurusServer(listener.accept()).start(); 
            }
        } finally {
            listener.close();
        }
	}
}
