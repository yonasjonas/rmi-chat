package Server;
/*
 * import java.rmi.RemoteException;
 * 
 * public interface ChatInterface extends java.rmi.Remote { boolean
 * checkClientCredintials(ChatInterface ci,String name, String pass) throws
 * RemoteException; void broadcastMessage(String name,String message) throws
 * RemoteException; void sendMessageToClient(String message) throws
 * RemoteException; }
 */


import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.RemoteRef;

public interface IChatServer extends Remote {
<<<<<<< HEAD


	public void registerUser(String[] details)throws RemoteException;
	public void initiateRegister(String[] details) throws RemoteException;
	public void sendPrivate(int[] list, String message) throws RemoteException;
=======
    void updateChat(String name, String chatMessage) throws RemoteException;
	void registerUser(String[] details)throws RemoteException;
	void initiateRegister(String[] details) throws RemoteException;
	void sendPrivate(int[] list, String message) throws RemoteException;
>>>>>>> 1c2013c7db3989daa785cc368e5129f975bdcc03
	void leaveChat(String name) throws RemoteException;
	public void massSend(String chatMessage, String name) throws RemoteException; 

	
	
	
}