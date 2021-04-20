package Server;
import Client.IChatClient;

public class User {
	
	String name;
	String dob;
	String country;
	IChatClient clientInfo;
	
	public User(String name, String dob, String country, IChatClient clientInfo) {
		this.name = name;
		this.dob = dob;
		this.country = country;
		this.clientInfo = clientInfo;
	}
	
	
	public String getName() {
		return name;
	}
	public String getDOB() {
		return dob;
	}
	public String getCountry() {
		return country;
	}
	public IChatClient getClientInfo() {
		return clientInfo;
	}
}
