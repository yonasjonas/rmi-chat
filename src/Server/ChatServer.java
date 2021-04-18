package Server;

import java.rmi.Naming;
//import java.rmi.Remote;
import java.rmi.RemoteException;
//import java.rmi.server.RemoteRef;
import java.rmi.server.UnicastRemoteObject;
import java.util.Date;
import java.util.Vector;

import Client.IChatClient;
//import Server.User;
import server.Chatter;

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

	public void massSend(String text) {
		// int i = 0;
		// int arrayLength = chatClients.size();

		for (User u : chatClients) {
			System.out.print(u);
			try {
				u.getClientInfo().messageFromServer(text);

			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}

	}

	@Override
	public void initiateRegister(String[] details) throws RemoteException {
		if (chatClients.isEmpty()) {
			registerUser(details);
		}
		else {
			for (User c : chatClients) {
				if (c.getName().equals(details[0])) {
				
					System.out.println(details[0] + " has same name as you, please pick another username");
					//System.out.println(new Date(System.currentTimeMillis()));
					//chatClients.remove(c);
					break;
				}
				else {
					registerUser(details);
				}
			}
			
		}
		
		
		
		//registerUser(details);
		//checkName(details);
	}
	
//	public void checkName(String[] details) {
//		
//		
//		for (User c : chatClients) {
//			if (c.getName().equals(details[0])) {
//				
//				System.out.println(details[0] + " has same name as you, please pick another username");
//				//System.out.println(new Date(System.currentTimeMillis()));
//				//chatClients.remove(c);
//				//break;
//			}
//			else {
//				//registerUser(details);
//			}
//		}
//	}

	public void registerUser(String[] details) {
		
		

		try {

			// IChatClient userDetails = (IChatClient).Naming.lookup("rmi://" + details[1] +
			// "/" + details[2]) ;

			IChatClient userStub = (IChatClient) Naming.lookup("rmi://" + details[4] + "/" + details[3]);
			chatClients.addElement(new User(details[0], details[1], details[2], userStub));

			userStub.messageFromServer("Welcome " + details[0] + details[1] + details[2] + " you are fully connected :) \n");

			massSend("Server: New user: " + details[0] + "\nBorn in: " + details[1] + "\nFrom: " + details[2] + " just joined");

			updateUserList();
			// printUsers();

		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	public void leaveChat(String name) throws RemoteException {

		for (User c : chatClients) {
			if (c.getName().equals(name)) {
				System.out.println(name + " left the chat...");
				System.out.println(new Date(System.currentTimeMillis()));
				chatClients.remove(c);
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

	public String[] getUserList() {
		String[] allUsers = new String[chatClients.size()];
		for (int i = 0; i < allUsers.length; i++) {
			allUsers[i] = chatClients.elementAt(i).getName();
		}

		return allUsers;

	}

	@Override
	public String sayHello() throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateChat(String name, String chatMessage) {
		String string = name + ":" + chatMessage;
		massSend(string);
	}

}