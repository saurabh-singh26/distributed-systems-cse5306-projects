//Submitted by 1001568347 - Saurabh Singh for CS5306 - Fall 2017 - Lab 2 - Online Advising using RPC

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.LinkedList;

// The interface declares the method which can be called by remote processes using RMI
public interface MQSInterface extends Remote{
	public void submitRequest(Student student) throws RemoteException;

	public LinkedList<Student> retrieveStudentRequests() throws RemoteException;

	public void postForNotification(Student student) throws RemoteException;

	public LinkedList<Student> retrieveAdvisorResponse() throws RemoteException;
}
