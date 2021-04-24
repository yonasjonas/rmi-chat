package Client;

import java.awt.Color;
import java.net.MalformedURLException;
import java.rmi.ConnectException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import javax.swing.JOptionPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

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
	private String colour;

	public ChatClient(ChatUI ChatGUI, String userName, String dateOfBirth, String selectedCountry, String colour )
			throws RemoteException {
		super();
		this.chatGUI = ChatGUI;
		this.name = userName;
		this.dob = dateOfBirth;
		this.country = selectedCountry;
		this.colour = colour;
		this.clientServiceName = "ClientListenService_" + userName;

	}

	public void startClient() throws RemoteException {
		String[] details = { name, dob, country, colour, clientServiceName, hostName };

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
	
//	@Override
//	public void messageFromServer(String message) throws RemoteException {
//
//		//IServer
//		//JPanel textPanel = new JPanel();
//		//chatGUI.outputArea.append("\n");
//		chatGUI.outputArea.setText(chatGUI.inputArea.getText());		
//		chatGUI.textPanel.add(chatGUI.outputArea);
//		//chatGUI.textPanel.add(chatGUI.outputArea);
//		
//		//chatGUI.outputArea.append(message);
//		System.out.print("From the moon: " + chatGUI.inputArea.getText());
//		chatGUI.inputArea.setText("");
//		chatGUI.textPanel.repaint();
//		chatGUI.textPanel.revalidate();
//
//	}
	
	public String checkAlive() {
		String alive = "alive";
		
		return alive; 
	}
	public void setUserColour(String colour) {
		SimpleAttributeSet keyWord = new SimpleAttributeSet();
		StyleConstants.setForeground(keyWord, Color.RED);
		
		
	}

	@Override
	public void messageFromServer(String message) throws RemoteException {

		//IServer
		//chatGUI.outputArea.append("\n");		
		//chatGUI.outputArea.append(message);
		//container.add(textPane, BorderLayout.EAST);
		chatGUI.doc = chatGUI.textPane.getStyledDocument();
		SimpleAttributeSet keyWord = new SimpleAttributeSet();
		StyleConstants.setForeground(keyWord, Color.RED);
		
		
		try {
			//chatGUI.doc.insertString(0, message, keyWord );
			chatGUI.doc.insertString(chatGUI.doc.getLength(), "\nEnd of text", keyWord );
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void showUserList(String[] users) throws RemoteException {

		chatGUI.defaulUsersPanel.remove(chatGUI.scrollPane);		
		chatGUI.updateUsersPanel(users);
		chatGUI.usersPanel.repaint();
		chatGUI.usersPanel.revalidate();

	}
}