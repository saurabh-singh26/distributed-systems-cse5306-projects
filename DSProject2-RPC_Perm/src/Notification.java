//Submitted by 1001568347 - Saurabh Singh for CS5306 - Fall 2017 - Lab 2 - Online Advising using RPC

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;

public class Notification {
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
            	System.out.print("Checking updates from advisor at " + new SimpleDateFormat("yyyy.MM.dd HH:mm:ss").format(new Date()) + "....");
            	// Call the method defined in MQSInterface to retrieve advisor response
            	LinkedList<Student> advisorResponse = stub.retrieveAdvisorResponse();
            	if(advisorResponse.size() > 0) {
            		// If any new advisor response is present then print the response on the console
            		Iterator<Student> itr = advisorResponse.iterator();
            		while(itr.hasNext()) {
            			Student student = itr.next();
            			System.out.println("\n["+student.name + " : " + student.course + "] ----------> " + student.approvalStatus);
            		}
            	}
            	else System.out.println("no new updates.");
            	Thread.sleep(7000); // Sleep for 7 seconds before checking again with MQS server
            }
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
