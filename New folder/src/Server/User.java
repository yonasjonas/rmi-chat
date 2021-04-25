package Server;
import Client.IChatClient;

public class User {
	
	String name;
	String dob;
	String country;
	String fcolour;
	IChatClient clientInfo;
	
	public User(String name, String dob, String country, String fcolour, IChatClient clientInfo) {
		this.name = name;
		this.dob = dob;
		this.country = country;
		this.clientInfo = clientInfo;
		this.fcolour = fcolour;
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
	public String getColour() {
		return fcolour;
	}
	public void setColour(String colour) {
		this.fcolour = colour;
	}
	public IChatClient getClientInfo() {
		return clientInfo;
	}
}
