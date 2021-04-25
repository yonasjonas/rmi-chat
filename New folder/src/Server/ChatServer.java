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

}