package Client;

import java.awt.Window;
import java.net.MalformedURLException;
import java.rmi.ConnectException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import Server.IChatServer;

public class ChatClient extends UnicastRemoteObject implements IChatClient {

	private static final long serialVersionUID = 1L;
	ChatUI chatGUI;
	private String hostName = "localhost";
	private String serviceName = "PrivateChat";
	private String clientServiceName;
	private String name, dob;
	String country;

	protected IChatServer IServer;
	protected boolean connectionProblem = false;

	// constructor with information 
	public ChatClient(ChatUI ChatGUI, String userName, String dateOfBirth, String selectedCountry)
			throws RemoteException {
		super();
		this.chatGUI = ChatGUI;
		this.name = userName;
		this.dob = dateOfBirth;
		this.country = selectedCountry;
		this.clientServiceName = "ClientListenService_" + userName;

	}
	// startClient() is invoked in ChatUI to establish connection with the server 
	public void startClient() throws RemoteException {
		
		// object to be passed to server
		String[] details = { name, dob, country, clientServiceName, hostName };

		// creating server stub 
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
		// if no problem sends details object to the server to be processed
		if (!connectionProblem) {
			IServer.initiateRegister(details);

		}
		// print this if it was able to reach this point
		System.out.println("Client Listen RMI Server is running...\n");
	}

	
	// This method is not working at moment of submission but leaving it here to show that i tried.
	// idea was to invoke openLogin from server when name is already taken
	// it open only blank window.
	public void openLogin(String message, boolean sameName) {	
		
		RegisterLoginPopup.disposeWindow();
		Window[] windows = Window.getWindows();
        for (Window window : windows) {
            if (window instanceof JDialog) {
                JDialog dialog = (JDialog) window;
                if (dialog.getContentPane().getComponentCount() == 1
                    && dialog.getContentPane().getComponent(0) instanceof JOptionPane){
                    dialog.dispose();                    
                }
            }
        }
		
	}
	
	// Server send meesage and keyword which is the font colour of this user
	@Override
	public void messageFromServer(String message,  SimpleAttributeSet keyWord) throws RemoteException {			
		try { 
			// first argument sets the position of the meesage to go in
        	chatGUI.doc.insertString(chatGUI.doc.getLength(), message + "\n", keyWord); 
        }
        catch (BadLocationException e){
        	System.out.print(e);
        }        

	}
	// remove usersPanel in the UI when updating users list and add it new one again
	public void showUserList(String[] users) throws RemoteException {
		chatGUI.usersPanel.remove(chatGUI.scrollPane);	
		chatGUI.updateUsersPanel(users);
		chatGUI.usersPanel.repaint();
		chatGUI.usersPanel.revalidate();
	}

}