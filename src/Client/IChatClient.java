package Client;

import java.rmi.Remote;
import java.rmi.RemoteException;

import javax.swing.text.SimpleAttributeSet;

public interface IChatClient extends Remote {
	
<<<<<<< HEAD
	public void messageFromServer(String message, SimpleAttributeSet keyWord) throws RemoteException;
	public void openLogin(String newMessage, boolean b) throws RemoteException;
	public void showUserList(String[] users) throws RemoteException;	
=======
	public void messageFromServer(String message) throws RemoteException;
	public void openLogin(String newMessage) throws RemoteException;
	public void showUserList(String[] users) throws RemoteException;
	public String checkAlive() throws RemoteException;
	
	
>>>>>>> 1c2013c7db3989daa785cc368e5129f975bdcc03
}
