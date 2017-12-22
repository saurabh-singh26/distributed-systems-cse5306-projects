//Submitted by 1001568347 - Saurabh Singh for CS5306 - Fall 2017 - Lab 2 - Online Advising using RPC

import java.io.Serializable;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

// Student class is used to instantiate a student throughout the system using its properties, that is 
// Student Name, Course Name and approval status by the advisor.
public class Student implements Serializable{	
	String name;
	String course;
	String approvalStatus;
	
	// Constructor
	public Student(String name, String course, String status) {
		this.name = name;
		this.course = course;
		this.approvalStatus = status;
	}
	
	public static void main(String[] args) {
		Scanner userInput = new Scanner(System.in);
		// Initialize the security manager if not present
		if (System.getSecurityManager() == null) {
			System.setProperty("java.security.policy",System.getProperty("user.dir")+"\\security.policy");
            System.setSecurityManager(new SecurityManager());
        }
		try {
			// The MQS server is referenced on the stub using the name "Advising"
			String referenceName = "Advising";
			// Locate the registry and then look for the reference name
            Registry registry = LocateRegistry.getRegistry();
            MQSInterface stub = (MQSInterface) registry.lookup(referenceName);
            // Take user input for Student name and course name until killed
			while(true) {
				System.out.print("Student Name: ");
				String name = userInput.nextLine();
				System.out.print("Course Name: ");
				String course = userInput.nextLine();
				// Creating a student object with approval status as blank, which would be picked up by the
				// Advisor for processing
				Student student = new Student(name, course, "");
				System.out.println("You entered: " + student.name + " & " + student.course);
				// Call the submitRequest method defined on the server using the stub obtained above
				stub.submitRequest(student);
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			if(userInput!=null)userInput.close();
		}
	}
}

