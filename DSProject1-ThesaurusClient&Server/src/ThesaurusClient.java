/*Submitted by 1001568347 - Saurabh Singh for CS5306 - Fall 2017 Lab 1 - Thesaurus Client and Server
 * References: http://cs.lmu.edu/~ray/notes/javanetexamples/
 * */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;

//Handles the communication related services for client
public class ThesaurusClient {
	Socket clientSocket; //Initialize a socket for client which will be connected to server
	PrintStream out; //Output Stream to server from client
	BufferedReader in; //Input stream from server to client
	
	//Constructor for ThesaurusClient class. It calls the connectToServer method which connects to the server and creates input and output streams to communicate with the server
	public ThesaurusClient(String host, int port) {
		connectToServer(host, port);
	}
	
	//Connects to the server hosted on host "host" and port "port", passed as an argument to this method
	private void connectToServer(String host, int port) {
		try {
			clientSocket = new Socket(host, port); //Creates a socket and connect to the server
			out = new PrintStream(clientSocket.getOutputStream()); //Creates on output stream for the clientSocket to send data to server
			in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())); //Creates an input stream to read data from the server
		} catch (UnknownHostException e) {
			e.printStackTrace(); //To print stack trace if the server does not exists of the entered host and port
		} catch (IOException e) {
			e.printStackTrace(); //To print stack trace for any input output exception while creating the input and output streams for the client socket
		}
	}
	
	//The methods takes no arguments and returns nothing. Its called when "Quit" button is clicked on the ClientUI
	void closeResources() {
		try {
			out.println("_QUIT_"); //Send an input string to the server which informs the server to close the input and output streams for this client
			
			//Check if the resources(Client Socket, input stream, and output stream) are not null and then close it
			if(clientSocket!=null)clientSocket.close();
			if(out!=null)out.close();
			if(in!=null)in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//The method takes the input word as argument and returns the synonyms of the word
	public String getMeaning(String inputWord) {
		String meaning = null;
		try {
			out.println(inputWord); //send it to the server using output stream "out"
			meaning = in.readLine(); //reads the input using input stream "in"
		} catch (IOException e) {
			e.printStackTrace();
		}
		return meaning; //Return the synonym obtained from the server
	}
	
}
