//Submitted by 1001568347 - Saurabh Singh for CS5306 - Fall 2017 - Lab 2 - Online Advising using RPC

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;

public class Advisor {
	
	public static void main(String[] args) {
		// Initialize the security manager if not set
		if (System.getSecurityManager() == null) {
			System.setProperty("java.security.policy",System.getProperty("user.dir")+"\\security.policy");
            System.setSecurityManager(new SecurityManager());
        }
		try {
			// The MQS server is referenced on the stub using the name "Advising"
			String referenceName = "Advising";
            Registry registry = LocateRegistry.getRegistry();
            MQSInterface stub = (MQSInterface) registry.lookup(referenceName);
            while(true) {
            	System.out.print("Checking requests from student at " + new SimpleDateFormat("yyyy.MM.dd HH:mm:ss").format(new Date()) + "....");
            	// Call the method defined in MQSInterface to retrieve student requests
            	LinkedList<Student> studentRequests = stub.retrieveStudentRequests();
            	if(studentRequests.size() > 0) {
            		Iterator<Student> itr = studentRequests.iterator();
            		while(itr.hasNext()) {
            			Student student = itr.next();
            			// For all request, approve or deny the request with a random probability
            			student.approvalStatus = (Math.random() >= 0.5) ? "Approved" : "Denied";
            			System.out.print("\n" + student.approvalStatus + " request for [" + student.name + " : " + student.course +"]");
            			// Call the method defined in MQSInterface to post the response
            			stub.postForNotification(student);
            		}
            	}
            	else System.out.println("no new requests.");
            	Thread.sleep(3000); // Sleep for 3 seconds before checking again with MQS server
            }
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
