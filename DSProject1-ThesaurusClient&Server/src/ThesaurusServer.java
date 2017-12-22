/*Submitted by 1001568347 - Saurabh Singh for CS5306 - Fall 2017 Lab 1 - Thesaurus Client and Server
 * References: 
 * 1) http://cs.lmu.edu/~ray/notes/javanetexamples/
 * 2) https://www.mkyong.com/java/how-to-read-file-from-java-bufferedreader-example/s
 * */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;

//Handles the connection and communication related services with the client
public class ThesaurusServer extends Thread{
	public static HashMap<String, String> wordList = new HashMap<>(); //to store the contents in data file
	Socket clientSocket; //declare the clientSocket to which the thread belongs
	//input and output streams from and to the client
	PrintWriter out; 
	BufferedReader in;
	
	//Constructor for ThesaurusServer which initialized the wordList hash map with the data file
	public ThesaurusServer(Socket clientSocket) {
		populateWordList(); //populate the hash map with the contents of the dataFile 
		this.clientSocket = clientSocket; // Initialize the client socket with the received connection request
	}
	
	//This method is called when start() is called on ThesaurusServer object in ServerUI main method
	public void run() {
		try {
			//Creates input and output streams for every server thread for each client
			in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			out = new PrintWriter(clientSocket.getOutputStream(), true);
			//To store the incoming word in 'inputWord' variable and the meaning to be sent in 'output' variable
			String inputWord, output;
			
			//Server enters into a listening mode from client 
			while(true) {
				inputWord = in.readLine(); //Read input from client
				//When QUIT button is clicked from client GUI, client sends "_QUIT_" which breaks the loop and execute the finally block
				if(inputWord.equals("_QUIT_"))break; 
				//Set the text field as the string sent from the client
				ServerUI.setTextField(inputWord);
				//Search the string's synonym in the hash map and return to the client using output stream
				output = wordList.get(inputWord);
				out.println(output);
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			//Close the resources like socket, input stream and ouput stream
			try {
				if(out!=null)out.close();
				if(in!=null)in.close();
				if(clientSocket!=null)clientSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	//For every ThesaurusServer object, the data file is read and stores the contents in hashmap with word as key and its synonym as value
	private static void populateWordList() {
		BufferedReader br = null;
		try {
			//Read data.txt file and stores the contents in wordList hashmap
			File dataFile = new File("./src/data.txt");
			br = new BufferedReader(new FileReader(dataFile.getPath()));
			String line;
			while((line = br.readLine())!=null) { //Read line by line
				//Split the data using ":" as input is stored in data file as word:meaning
				wordList.put(line.split(":")[0].trim(), line.split(":")[1]);	
			}
		} catch (Exception e) {
			e.printStackTrace(); //Print stack trace for any exception in try block
		} finally {
			try {
				br.close(); //close the bufferedReader object
			} catch (IOException e) {
				e.printStackTrace(); //Print stack trace if the resource closing returns an exception
			}
		}
	}

}
