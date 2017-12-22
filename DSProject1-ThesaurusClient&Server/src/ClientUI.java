/*
 * Submitted by 1001568347 - Saurabh Singh for CS5306 - Fall 2017 Lab 1 - Thesaurus Client and Server
 * References:
 * 1) https://www.javatpoint.com/java-swing: For client GUI creation
*/

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

//The class takes care of client UI and connects to the server
public class ClientUI extends JFrame{
	//Initializing a panel and objects inside the panel which define client GUI
	JPanel panel = new JPanel();
	
	//Initialized two text fields: one for word, second for its synonyms
	JTextField inputField, outputField;
	
	//Initialized a combo box which contains the words whose synonyms are already present in data file
	JComboBox inputComboBox;
	
	//An instance of the ThesaurusClient class which contains connection related methods
	public static ThesaurusClient client;
	
	//Constructor for ClientUI class which calls an initUI method
	ClientUI(){
		initUI();
	}
	
	//This method designs the complete client GUI 
	private void initUI() {
		setTitle("Thesaurus Client"); //Defines the title of client window
		setSize(300, 250); //Defines the size of client window
		setLocationRelativeTo(null); //Places the client window in center of the screen
		setDefaultCloseOperation(EXIT_ON_CLOSE); //Defines the behavior when user closes the client window. "EXIT_ON_CLOSE" would close the application using System.exit method
	    getContentPane().add(panel); //Returns the content pane and adds panel to it
	    panel.setLayout(null); //Not using any of the pre-defined swing layouts 
	    
	    //Adding all the objects on the panel in separate methods
	    addComboBox();
	    addInputField();
	    addOutputField();
	    addQuitButton();
	    addSendButton();
	}
	
	//Adding combo box which contains the words already present in data file
	private void addComboBox() {
		String[] inputData = {"", "abandon", "success"}; //Synonyms of abandon and success are already present in the data file and thus would display in the combo box
		
		//Creating the object of combo box and setting its position within the panel using setBounds method
		inputComboBox = new JComboBox(inputData);
		inputComboBox.setBounds(30, 20, 160, 30);
		
		//Adding ActionListener on the combo box.
		inputComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	//The selected word from the combo box would get populated in the inputField text box which is the woud to be sent to the server
            	inputField.setText(inputComboBox.getItemAt(inputComboBox.getSelectedIndex()).toString());
            }
        });
		panel.add(inputComboBox); //Finally adding the combo box to the panel
	}
	
	/*Creating the object for inputField which contains the word whose synonym we wish to seek. 
	Setting its relative position within the panel and adding it to the panel.*/
	private void addInputField() {
		inputField  = new JTextField();
		inputField.setBounds(30, 60, 160, 30);
		panel.add(inputField);
	}

	/*Creating the object for outputField which contains the synonyms of the word entered in inputField text box as returned from the server
	Setting its relative position within the panel and adding it to the panel.*/
	private void addOutputField() {
		outputField  = new JTextField();
		outputField.setBounds(30, 100, 160, 30);
	    panel.add(outputField);
	}
	
	//Creating a quit button for the client, setting its position and adding it to the panel
	private void addQuitButton() {
		JButton quitButton = new JButton("Exit");
		quitButton.setBounds(30, 140, 70, 30);
		quitButton.addActionListener(new ActionListener() {	    	
			public void actionPerformed(ActionEvent event) {
				//On clicking the Quit button, first we close the resources like client socket, input and output streams and then kill the application using System.exit method
				client.closeResources();
				System.exit(0);
			}
		});
		panel.add(quitButton);
	}
	
	//Creating a send button for the client, setting its position and adding it to the panel
	private void addSendButton() {
	    JButton sendButton = new JButton("Send");
	    sendButton.setBounds(120, 140, 70, 30);
	    sendButton.addActionListener(new ActionListener() {	    	
	    	public void actionPerformed(ActionEvent event) {
	    		/*On clicking the Send button, we do the following:
	    		 * 1. Get the entry in inputField text box using getInputWord() method
	    		 * 2. Call the getMeaning() method defined in ThesaurusClient class and pass the word read from step 1 as an argument
	    		 * 3. Set the meaning retrieved from the server in outputField text box using setMeaning() method*/
	    		setMeaning(client.getMeaning(getInputWord()));
	    	}
	    });
	    panel.add(sendButton);
	}

	//To read the contents of the inputField text box to be sent to the server for query
	public String getInputWord() {
		return inputField.getText().trim(); //Trimmed the input to remove any leading or tailing spaces
	}
	
	//To set the value of outputField text box once we have the synonym from the server
	public void setMeaning(String fromServer) {
		outputField.setText(fromServer);
	}
	
	public static void main(String[] args) {
		ClientUI clientUIObj = new ClientUI();
		clientUIObj.setVisible(true);
		
		//Create a client and connects it to the default host and port IF no arguments are passed
		client = (args.length!=0) ? 
					new ThesaurusClient(args[0], Integer.parseInt(args[1])) : 
					new ThesaurusClient("127.0.0.1", 9999);
	}
}
