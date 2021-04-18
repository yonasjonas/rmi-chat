package Client;

import java.net.MalformedURLException;
import java.rmi.ConnectException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Vector;

import javax.swing.JOptionPane;

import Server.IChatServer;
import Server.User;
import Client.ChatUI;

public class ChatClient extends UnicastRemoteObject implements IChatClient {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L; 
	ChatUI chatGUI;
	private String hostName = "localhost";
	private String serviceName = "PrivateChat";
	private String clientServiceName;
	private String name, dob;
	String country;
	
	protected IChatServer IServer;
	protected boolean connectionProblem = false;
    
	

    public ChatClient(ChatUI ChatGUI, String userName, String dateOfBirth, String selectedCountry) throws RemoteException {
    	super();
    	this.chatGUI = ChatGUI;
		this.name = userName;
		this.dob = dateOfBirth;
		this.country = selectedCountry; 
		this.clientServiceName = "ClientListenService_" + userName;
    	
    }
    
    @SuppressWarnings("null")
	public void startClient() throws RemoteException {
		String[] details = {name, dob, country, clientServiceName, hostName};	
		//User username = new User(name, clientServiceName, clientServiceName, null);
		Vector<User> chatClients = null;
		
		
		try {
			Naming.rebind("rmi://" + hostName + "/" + clientServiceName, this);
			IServer = ( IChatServer )Naming.lookup("rmi://" + hostName + "/" + serviceName);	
		} 
		catch (ConnectException  e) {
			JOptionPane.showMessageDialog(
					chatGUI.frame, "The server seems to be unavailable\nPlease try later",
					"Connection problem", JOptionPane.ERROR_MESSAGE);
			connectionProblem = true;
			e.printStackTrace();
		}
		catch(NotBoundException | MalformedURLException me){
			connectionProblem = true;
			me.printStackTrace();
		}
		for (User c : IServer.chatClients) {
			if (c.getName().equals(details[0])) {
			
				System.out.println(details[0] + " has same name as you, please pick another username");
				//System.out.println(new Date(System.currentTimeMillis()));
				//chatClients.remove(c);
				break;
			}
			else {
				if(!connectionProblem){
					System.out.print("details: " + details);
					IServer.initiateRegister(details);
					
				}	
				
			}
		}
		
		System.out.println("Client Listen RMI Server is running...\n");
	}


	
	
	

	@Override
	public void messageFromServer(String message) throws RemoteException {		
		
		System.out.println( message );
		chatGUI.outputArea.append( "\n" );	
		chatGUI.outputArea.append( message );
		
		
	}
	
	public void showUserList(String[] users) throws RemoteException {
		
		
		System.out.print("list length: "+ users.length);
		for(String s : users){
        	System.out.print("user puser: "+ s);
        }	
		
		chatGUI.usersPanel.remove(chatGUI.usersPanel);
		chatGUI.updateUsersPanel(users);
		chatGUI.usersPanel.repaint();
		chatGUI.usersPanel.revalidate();
		
		System.out.print("list length: "+ users.length);

		
		
		
	}
	
}