package Client;

import java.net.MalformedURLException;
import java.rmi.ConnectException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import javax.swing.JOptionPane;

import Server.IChatServer;

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

	public ChatClient(ChatUI ChatGUI, String userName, String dateOfBirth, String selectedCountry)
			throws RemoteException {
		super();
		this.chatGUI = ChatGUI;
		this.name = userName;
		this.dob = dateOfBirth;
		this.country = selectedCountry;
		this.clientServiceName = "ClientListenService_" + userName;

	}

	public void startClient() throws RemoteException {
		String[] details = { name, dob, country, clientServiceName, hostName };

		try {
			Naming.rebind("rmi://" + hostName + "/" + clientServiceName, this);
			IServer = (IChatServer) Naming.lookup("rmi://" + hostName + "/" + serviceName);
		} catch (ConnectException e) {
			JOptionPane.showMessageDialog(chatGUI.frame, "The server seems to be unavailable\nPlease try later",
					"Connection problem", JOptionPane.ERROR_MESSAGE);
			connectionProblem = true;
			e.printStackTrace();
		} catch (NotBoundException | MalformedURLException me) {
			connectionProblem = true;
			me.printStackTrace();
		}
		if (!connectionProblem) {
			System.out.print("details: " + details);
			IServer.initiateRegister(details);

		}
		System.out.println("Client Listen RMI Server is running...\n");
	}

	public void openLogin(String message) {	
		//RegisterLoginPopup.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
		RegisterLoginPopup.main(null);
		
	}

	@Override
	public void messageFromServer(String message) throws RemoteException {

		System.out.println(message);
		chatGUI.outputArea.append("\n");
		chatGUI.outputArea.append(message);

	}

	public void showUserList(String[] users) throws RemoteException {

		chatGUI.defaulUsersPanel.remove(chatGUI.scrollPane);
		//chatGUI.usersPanel.remove(chatGUI.usersPanel);		
		chatGUI.updateUsersPanel(users);
		chatGUI.usersPanel.repaint();
		chatGUI.usersPanel.revalidate();

		//System.out.print("list length: " + users.length);

	}
}