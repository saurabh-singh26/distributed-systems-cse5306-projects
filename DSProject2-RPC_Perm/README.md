In this project, we simulate an online advising system which is similar to what is used while registering in classes. Students request clearance for a course from the advisor. At some other time the advisor approves or disapproves the request and then the student is notified of the advisor’s decision. 

The project implement 4 processes to simulate this: a student process, an advisor process, a notification process and a message queuing server (MQS). The student process, advisor process and notification process communicate through the message queuing server (message oriented middleware). Communication from these processes to the message queuing server is through remote method invocation. Note that Student, Advisor and Notification Process do not communicate directly. 

Student Process:
The Student process works on behalf of the student. It takes as input (from the command prompt) the name of the student and the course for which the student wants clearance. The Student process then sends a message through the MQS to the Advisor seeking clearance. It keeps doing this (under control of the user) until it is killed.

Advisor Process:
When it is started, the Advisor Process contacts the MQS for any message from the Student process. If there are any messages then it retrieves them and approves or disapproves the requests based on a random probability. It then send a message for each request (message) through the MQS to the Notification process so that it (the Notification process) can notify the Student process about the decision. If there are no messages for the Advisor process in the MQS then it sleeps for 3 seconds and again contact back to check for messages. It keeps doing this until is killed. The system also print out a message for each request (message) that the Advisor process processes (whether it is approved or not).

Notification Process:
The Notification process notifies the Student process about the Advisor’s decision. To simulate this the system just prints on the command prompt the student name, course and the advisor decision. The Notification process checks MQS for messages from the Advisor process. If there are any messages then it retrieves them and notify the student by printing as stated above. If there are no messages then it sleeps for 7 seconds and then contact the MQS again. It keeps doing this until is killed.

Message Queuing Server (MQS):
This process simulates the Message Queuing server. MQS have the following:
1. It stores or returns a message from or to any requesting process.
2. The system has only one physical queue (single array, linked list or queue, etc.) to store the messages from both Student and Advisor process.
3. Messages once retrieved is physically deleted from the queue.
4. Messages are persistent, i.e. even if the server is shut down, the messages are not lost. For that the system stores the messages in a file. When the MQS is restarted it reloads all the messages from the file in main memory data structure (array or linked-list etc). Its assumed that the MQS will be killed only after all the 3 other processes are killed so that there's no worry about problems that would arise if it were killed while in communication.

Other Specifications:
1. The MQS will be started first. The other 3 processes can be started or stopped in any order. 
2. For the Advisor and Notification process, a message (like “no message found”) is printed on the console when it probes the MQS and there is no message.
