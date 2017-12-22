//Submitted by 1001568347 - Saurabh Singh for CS5306 - Fall 2017 - Lab 2 - Online Advising using RPC

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class MessageQueuingServer implements MQSInterface{
	static List<Student> centralQueue = new CopyOnWriteArrayList<Student>();
	static String FILENAME = "src/records.txt";
	static File recordsFile = new File(FILENAME);
	static FileOutputStream fos = null;
	static BufferedReader br = null;
	
	public MessageQueuingServer(){
		super();
	}
	
	// Populates the queue with unprocessed data(Student object), if any, stored in the records file
	static {
		try {
			br = new BufferedReader(new FileReader(recordsFile.getAbsolutePath()));
			String entries = null;
			while((entries=br.readLine())!=null) {
				String[] studentInfo = entries.split(",");
//				System.out.println("Adding from file: " + studentInfo[0]+","+studentInfo[1]);  //---- For debugging
				centralQueue.add(new Student(studentInfo[0], studentInfo[1], 
						(studentInfo.length==3) ? studentInfo[2] : ""));
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			} 
		}
	}
	
	// Called by the Student process to submit the request for processing by advisor. The student object is
	// added to the queue and added to the file using appendRecord method.
	public void submitRequest(Student student) {
		centralQueue.add(student);
		appendRecord(student);
	}
	
	// Takes student object as input, converts it into a string and writes the string to records file 
	private static void appendRecord(Student s) {
		String entry = s.name + "," + s.course + "," + s.approvalStatus;
//		System.out.println("Entering: " + entry); ---- For debugging
		try {
			fos = new FileOutputStream(recordsFile, true);
			fos.write(entry.getBytes());
			fos.write(System.lineSeparator().getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			} 
		}
	}

	// This method is executed by Advisor to retrieve the student requests which has not been processed yet
	// We implement over the data structure and return all those student objects whose approval status is
	// empty, as initialized during object creation
	@Override
	public LinkedList<Student> retrieveStudentRequests() {
		LinkedList<Student> requests = new LinkedList<>();
		Iterator<Student> itr = centralQueue.iterator();
		while(itr.hasNext()) {
			Student student = itr.next();
			if(student.approvalStatus.isEmpty()) {
				requests.add(student);
				centralQueue.remove(student);
			}
		}
		updateRecord(); // After all requests have been retrieved to send to the advisor, update the records file too
		return requests;
	}
	
	// This method overwrites the records file with current values in the queue. It is called after Advisor
	// process retrieves its request and after Notification process retrieves the response from Advisor
	private static void updateRecord() {
		try {
			fos = new FileOutputStream(recordsFile, false);
			Iterator<Student> itr = centralQueue.iterator();
			while(itr.hasNext()) {
				Student s = itr.next();
				String entry = s.name + "," + s.course + "," + s.approvalStatus;
				fos.write(entry.getBytes());
				fos.write(System.lineSeparator().getBytes());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// This method is executed by Advisor after the requests have been processed by the Advisor. The student
	// object with an approvalStatus is added back to the central data structure to be picked up by Notification
	@Override
	public void postForNotification(Student student) {
		if(student!=null) {
			centralQueue.add(student);
			appendRecord(student); // Append the advisor response to the file
		}
	}
	
	// This method is executed by Notification to retrieve advisor response. We search for all those student
	// objects whose approval status is not empty and pass it to the Notification process
	@Override
	public LinkedList<Student> retrieveAdvisorResponse() {
		LinkedList<Student> requests = new LinkedList<>();
		Iterator<Student> itr = centralQueue.iterator();
		while(itr.hasNext()) {
			Student student = itr.next();
			if(!student.approvalStatus.isEmpty()) {
				requests.add(student);
				centralQueue.remove(student);
			}
		}
		updateRecord(); // Update the records file
		return requests;
	}
	
	public static void main(String[] args) {
		// Initialize the security manager if not set
		if (System.getSecurityManager() == null) {
			System.setProperty("java.security.policy",System.getProperty("user.dir")+"\\security.policy");
            System.setSecurityManager(new SecurityManager());
        }
		try {
			// Binding the MQSInterface class to the registry with reference name as "Advising", which can
			// be used by other processes to call the methods defined in the interface
			String name = "Advising";
			MQSInterface engine = new MessageQueuingServer();
			MQSInterface stub = (MQSInterface) UnicastRemoteObject.exportObject(engine, 0);
            Registry registry = LocateRegistry.getRegistry();
            registry.rebind(name, stub);
            System.out.println("Online Advising System is running!!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
