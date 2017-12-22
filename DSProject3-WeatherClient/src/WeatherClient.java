import java.util.Scanner;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class WeatherClient {
	static double latitude;
	static double longitude;
//	https://api.weather.gov/points/32.7357,-97.1081/forecast
	final static String host = "w1.weather.gov";
//	final static String host = "maps.googleapis.com";
	final static int port = 80;
	static Socket clientSocket;
	static PrintStream out; //Output Stream to server from client
	static BufferedReader in; //Input stream from server to client
	
	
	public static void main(String[] args) {
//		Scanner sc = new Scanner(System.in);
//		System.out.println("Enter latitude: ");
//		latitude = sc.nextDouble();
//		System.out.println("Enter longitude: ");
//		longitude = sc.nextDouble();
		
		try {
			clientSocket = new Socket(host, port);
			out = new PrintStream(clientSocket.getOutputStream()); //Creates on output stream for the clientSocket to send data to server
			in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())); //Creates an input stream to read data from the server
			out.println("GET /xml/current_obs/CWAV.xml HTTP/1.1");
//			out.println("GET /maps/api/geocode/json?address=1600+Amphitheatre+Parkway,+Mountain+View,+CA HTTP/1.1");
//			out.println("GET /points/32.7357,-97.1081/forecast HTTP/1.1");
//			out.println(":scheme: https");
//			out.println("Host: w1.weather.gov");
			out.println("");
			String msg;
			while((msg=in.readLine())!=null)System.out.println(msg);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
