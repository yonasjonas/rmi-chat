<<<<<<< HEAD
package Server;

import java.awt.Color;
import java.net.MalformedURLException;
import java.rmi.ConnectIOException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
//import java.rmi.Remote;
import java.rmi.RemoteException;
//import java.rmi.server.RemoteRef;
import java.rmi.server.UnicastRemoteObject;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import Client.IChatClient;
import Server.ClassExecutingTask.LoopTask;

public class ChatServer extends UnicastRemoteObject implements IChatServer {

	private static final long serialVersionUID = 1L;
	private Vector<User> chatClients;

	protected ChatServer() throws RemoteException {
		chatClients = new Vector<User>();
	}

//	public synchronized void broadcastMessage(String message) throws RemoteException {
//		int i = 0;
//		while (i < chatClients.size()) {
//			chatClients.get(i++).retrieveMessage(message);
//		}
//		
//	}

	public static void main(String args[]) {
		
		// setting private ip for the server as localhost is not recommended
		String hostName = "192.168.0.87";
		// setting serviceName to form an uri for later communication with a client
		String serviceName = "PrivateChat";

		
		//  if running from command line arguments can be added: 
		//arg 1: hostName 
		//arg2: serviceName
		
		if (args.length == 2) {
			hostName = args[0];
			serviceName = args[1];
		}
		// creating a connection and if successful print message
		try {
			java.rmi.registry.LocateRegistry.createRegistry(1099);
			IChatServer server = new ChatServer();
			Naming.rebind("rmi://" + hostName + "/" + serviceName, server);
			//
			// Naming.rebind(serviceName, new ChatServer());
			System.out.println("Group Chat RMI Server is running...");
		} catch (Exception e) {
			System.out.println(e);
			System.out.println("Server had problems starting");
		}
		
		// Following code below triggers  ClassExecutingTask which 
		//runs every 5 minutes to check if clients are alive and
		//if not if removes it from chatClient list
		//ClassExecutingTask executingTask = new ClassExecutingTask();
        //executingTask.start();
		
		
		
		
		
	}
	// when a user message received from  server itself or chat clients 
	// following method is invoked to send message from: name with a: message 

	
	// Assigning colour for the users based on their first letter of the name
	// still need to make this better by checking indxeOf the name in the chatClient data structure
	public SimpleAttributeSet assignColour(String name)  {
		
		SimpleAttributeSet keyWord = new SimpleAttributeSet();
		
		//int size = chatClients.size();
		
		if (name.toUpperCase().startsWith("A")) {
			StyleConstants.setForeground(keyWord, Color.ORANGE);
			StyleConstants.setBold(keyWord, true);
		}
		else if (name.toUpperCase().startsWith("B")) {
			StyleConstants.setForeground(keyWord, Color.BLUE);
			StyleConstants.setBold(keyWord, true);
		}
		else if (name.toUpperCase().startsWith("C")) {
			StyleConstants.setForeground(keyWord, Color.GREEN);
			StyleConstants.setBold(keyWord, true);
		}
		else if (name.toUpperCase().startsWith("D")) {
			StyleConstants.setForeground(keyWord, Color.MAGENTA);
			StyleConstants.setBold(keyWord, true);
		}
		else if (name.toUpperCase().startsWith("E")) {
			StyleConstants.setForeground(keyWord, Color.GRAY);
			StyleConstants.setBold(keyWord, true);
		}
		else if (name.toUpperCase().startsWith("J")) {
			StyleConstants.setForeground(keyWord, Color.RED);
			StyleConstants.setBold(keyWord, true);
		}
		else if (name.toUpperCase().startsWith("L")) {
			StyleConstants.setForeground(keyWord, Color.ORANGE);
			StyleConstants.setBold(keyWord, true);
		}
		
		return keyWord;
	}
	
	
	// this is unfinished method to check if clients are responding
	// and if not it invokes leaveChat() for the names that don't return
	public void checkClients() {
		
		for (User u : chatClients) {
			
						
		}
	}

	// remote method where client has selected users from defaulListmodel in ChatUI class and they were added to the list[] 
	// loop through the list and send message only to those in the list.
	public void sendPrivate(int[] list, String message) throws RemoteException {		
		
		User u;
		for (int i : list) {
			u = chatClients.elementAt(i);
			u.getClientInfo().messageFromServer(message, null);
		}
	}

	// Could directly reference register user method so created this to invoke registerUser(details[])
	//@Override
	public void initiateRegister(String[] details) throws RemoteException {
		registerUser(details);
		
		Runtime.getRuntime().gc();
	}
	
	// takes in the argument with details object that was constructed on the client side and passed.
	// includes name, dob, country, userStub, servicename, hostName, 
	public void registerUser(String[] details) {

		try {
			// setup a userStub address on the client that is being created for 
			IChatClient userStub = (IChatClient) Naming.lookup("rmi://" + details[4] + "/" + details[3]);			
			
			for (User u : chatClients) {
				
				//Here I am checking if name of the new user is already taken and if it is it prints an error  
				// problem i have is that it is not re-initialises popup again correctly
				if (u.getName().equals(details[0])) {
					String newMessage = " username is taken, please choose again";
					System.out.println(details[0] + ": " + newMessage);
					// first argument to be used to set new JLabel on the RegisterLoginPopup,
					// second argument is used to set the sameName boolean of the RegisterLoginPopup so popup could be reinitialised 
					// but  in this case it is null since it is message from server
					userStub.openLogin(newMessage, false);
	
					break;
					
				}
				
			} 
			// add an new user object to the chatClients vector 
			chatClients.addElement(new User(details[0], details[1], details[2], userStub));
			
			// invoke messageFromServer to the send message to the client that has been successfully created. Second argument null is a colour
			userStub.messageFromServer(
					"Welcome " + details[0] + " you are fully connected :) \n",  null);
			
			// invoke massSend to the send message to all connected clients to notify about new user 
			// didn't implement a compare function to return age by comparing input date with todays date. 
			// It getting late and there is still plenty to finish. Sorry...
			massSend("The new user called " + details[0] + " From: " + details[1] + " who is " + details[2] 
					+ " years old, has joined the system Please, say hello to " + details[0], details[0] + "\n");

			//add user name to the list of connected users and update usersPanel in the ChatGUI
			updateUserList();
			
			

		} catch (Exception e) {

			e.printStackTrace();
		}

	}
	// method takes user name as the argument and removes it from chatCleints Vector data structure
	public void leaveChat(String name) throws RemoteException {

		for (User u : chatClients) {
			if (u.getName().equals(name)) {
				chatClients.remove(u);
				break;
			}
		}
		// if this was the last user connect no point to update usersPanel in the ChatGUI 
		if (!chatClients.isEmpty()) {
			updateUserList();
		}
	}
	
	// generates a list of connected users and returns it
	public String[] getUserList() {
		String[] allUsers = new String[chatClients.size()];
		for (int i = 0; i < allUsers.length; i++) {
			allUsers[i] = chatClients.elementAt(i).getName();
		}

		return allUsers;

	}
	
	// takes a newly generated list from getUserList() and updates usersPanel in the UI for each in the chatClients Vector
	private void updateUserList() {

		String[] currentUsers = getUserList();
		for (User u : chatClients) {
			// System.out.print("chatClients size is: " + chatClients.size());
			try {
				u.getClientInfo().showUserList(currentUsers);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}
	
	// this method is triggered when a user sends a message 
	// with sendMessage method on the client side
	public void massSend(String text, String name) {

		// running assignColour function to find out which colour 
		// to be used for messages for client with this name.
		SimpleAttributeSet keyWord = assignColour(name);
		
		// looping through chatClient instances to send messages			
		for (User u : chatClients) {			
			try {
				// where u is User invokes User method getClientInfo 
				// which returns userStub from the chatClients Vector
				// and invokes messageFromServer to each userStub
				u.getClientInfo().messageFromServer(text, keyWord);

			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}


=======
package Server;

import java.awt.Color;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
//import java.rmi.Remote;
import java.rmi.RemoteException;
//import java.rmi.server.RemoteRef;
import java.rmi.server.UnicastRemoteObject;
import java.util.Date;
import java.util.Vector;

import Client.IChatClient;

public class ChatServer extends UnicastRemoteObject implements IChatServer {

	private static final long serialVersionUID = 1L;
	private Vector<User> chatClients;

	protected ChatServer() throws RemoteException {
		chatClients = new Vector<User>();
	}

//	public synchronized void broadcastMessage(String message) throws RemoteException {
//		int i = 0;
//		while (i < chatClients.size()) {
//			chatClients.get(i++).retrieveMessage(message);
//		}
//		
//	}
	

	public static void main(String args[]) {
		// System.setProperty("java.rmi.server.hostname","192.168.0.87");
		// startRMIRegistry();
		String hostName = "192.168.0.87";
		String serviceName = "PrivateChat";

		if (args.length == 2) {
			hostName = args[0];
			serviceName = args[1];
		}

		try {
			java.rmi.registry.LocateRegistry.createRegistry(1099);
			IChatServer server = new ChatServer();
			Naming.rebind("rmi://" + hostName + "/" + serviceName, server);
			//
			// Naming.rebind(serviceName, new ChatServer());
			System.out.println("Group Chat RMI Server is running...");
		} catch (Exception e) {
			System.out.println(e);
			System.out.println("Server had problems starting");
		}
	}

	public void udateChatWindow(String name, String message) {
		String appendText = name + ": " + message + "\n";
		massSend(appendText);
	}
	
	public void checkName() {
		
	}

	public void massSend(String text) {
		// int i = 0;
		// int arrayLength = chatClients.size();

		for (User u : chatClients) {
			//System.out.print(u);
			try {
				u.getClientInfo().messageFromServer(text);

			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void sendPrivate(int[] list, String message) throws RemoteException {
		User u;
		for (int i : list) {
			u = chatClients.elementAt(i);
			u.getClientInfo().messageFromServer(message);
		}
	}

@Override
	public void initiateRegister(String[] details) throws RemoteException {
		registerUser(details);
		
//		if (chatClients.isEmpty()) {
//			registerUser(details);
//		} else {
//			for (User u : chatClients) {
//				if (u.getName().equals(details[0])) {
//
//					System.out.println(details[0] + " username is taken, please choose again");
//					try {
//						IChatClient userStub = (IChatClient) Naming.lookup("rmi://" + details[4] + "/" + details[3]);
//						userStub.messageFromServer(details[0] + " username is taken, please choose again");
//						//String newMessage;
//						//userStub.openLogin(newMessage);
//						
//					} catch (MalformedURLException | RemoteException | NotBoundException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//					
//
//					// System.out.println(new Date(System.currentTimeMillis()));
//					// chatClients.remove(c);
//
//					break;
//				} else {
//					registerUser(details);
//				}
//			}
//		}
	}
	

	public void registerUser(String[] details) {
		
		//User u = new User(details[0], details[1], details[2], details[3], userStub);
		String colour = "";

		try {
			IChatClient userStub = (IChatClient) Naming.lookup("rmi://" + details[5] + "/" + details[4]);
			User u = new User(details[0], details[1], details[2], details[3], userStub);
			
			chatClients.addElement(u);

			userStub.messageFromServer(
					"Welcome " + details[0] + details[1] + details[2] + " you are fully connected :) \n");

			massSend("Server: New user: " + details[0] + "\nBorn in: " + details[1] + "\nFrom: " + details[2]
					+ " just joined");

			updateUserList();
			// printUsers();

		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	public void leaveChat(String name) throws RemoteException {

		for (User u : chatClients) {
			if (u.getName().equals(name)) {
				System.out.println(name + " left the chat....");
				chatClients.remove(u);
				break;
			}
		}
		if (!chatClients.isEmpty()) {
			updateUserList();
		}
	}

	private void updateUserList() {

		String[] currentUsers = getUserList();
		for (User u : chatClients) {
			// System.out.print("chatClients size is: " + chatClients.size());
			try {
				u.getClientInfo().showUserList(currentUsers);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}
	
//	public String[] getColours() {
//		String[] colourIds = new String[chatClients.size()];
//		for (int i = 0; i < colourIds.length; i++) {
//			allUsers[i] = i;
//		}
//		
//		return allUsers;
//	}
	


	public String[] getUserList() {
		String[] allUsers = new String[chatClients.size()];
		for (int i = 0; i < allUsers.length; i++) {
			allUsers[i] = chatClients.elementAt(i).getName();
		}

		return allUsers;

	}

	

	@Override
	public void updateChat(String name, String chatMessage) {
		String string = name + ":" + chatMessage;
		massSend(string);
	}

>>>>>>> 1c2013c7db3989daa785cc368e5129f975bdcc03
}