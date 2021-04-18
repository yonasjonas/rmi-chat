package Client;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IChatClient extends Remote {
	
	public void messageFromServer(String message) throws RemoteException;
	public void showUserList(String[] users) throws RemoteException;
}
