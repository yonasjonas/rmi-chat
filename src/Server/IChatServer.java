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
    String sayHello() throws RemoteException;
    public void updateChat(String name, String chatMessage) throws RemoteException;
	public void registerUser(String[] details)throws RemoteException;
	public void initiateRegister(String[] details) throws RemoteException;
	public void sendPrivate(int[] list, String message) throws RemoteException;
	void leaveChat(String name) throws RemoteException;
	
	
}