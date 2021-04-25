package Client;

import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.Arrays;
import java.util.Locale;

import javax.swing.*;

// this class implements UI initial prompt for user to enter their personal details that will be sent to the ChatClient for registration with server.
class RegisterLoginPopup {
	
	//Initial frame to live inside JOptionPane
	static JFrame f = new JFrame();
	
	static void displayLogin(String message, boolean sameName) {
		
		
		// if it is first attempt to login set message for label
		if (message == null) {
			message = "Hello. Enter your details";
		}
		// 
		//Boolean chatInitiated = false;
		
		
		
		// Main JPanel
		JPanel panel = new JPanel(new GridLayout(0, 1));
		
		// Adds a label 
		panel.add(new JLabel(message));
		
		// User name field
		JTextField name = new JTextField();
		
		// method to gather a list of all Countries 
		Country[] listCountry = createCountryList();
		// Drop down field for country list
		JComboBox<Country> combo = new JComboBox<>(listCountry);
		
		final JTextField datePicker = new JTextField(20);
		JButton b = new JButton("open calendar");
		
		// Adding elements to the panel one after another
		panel.add(new JLabel("\n Enter you Name"));
		panel.add(name);
		
		panel.add(new JLabel("\n Date of Birth:"));	
		
		// datePicker field where settext will be set
		panel.add(datePicker);
		//add button to open calendar
		panel.add(b);	
		
		// Country label and field
		panel.add(new JLabel("\n Country:"));
		panel.add(combo);
		
		panel.setVisible(true);
		
		// listener to set datePicker
		b.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				datePicker.setText(new DatePicker(f).setPickedDate());
			}
		});
		

		int result = JOptionPane.showConfirmDialog(null, panel, "Enter your details to login", JOptionPane.OK_CANCEL_OPTION,JOptionPane.PLAIN_MESSAGE);
		if (result == JOptionPane.OK_OPTION ) {
			
			
			// set values of the client info to be accessed in the ChatUI class
			// I am sure there is a better way to do it but I don't know it...
			Country selectedCountry = (Country) combo.getSelectedItem();
			ChatUI.name = name.getText();
			ChatUI.dob = datePicker.getText();
			ChatUI.country = selectedCountry.toString();
				
			System.out.println("Name: " + name.getText() + "DOB: " + ChatUI.dob + "Country: " + combo.getSelectedItem());
			
			// set button action to send to all clients
			ChatUI.SEND_ACTION = "sendAction";
			
			
			// this part is not working correctly 
			// due to lack of time it is not finished...
			if (!sameName) {
				panel.dispatchEvent(new WindowEvent(f, WindowEvent.WINDOW_CLOSING));
				disposeWindow();
				ChatUI ch = new ChatUI();
				ch.loginPopUp();
				
			}
			else {				 
				//main(null);
			}
		// if login promt is closed it prints in the console
		} else {
			System.out.println("Cancelled");

		}
	}
	// this is also not working but leaving with a hope that I was on the right path
	public static void disposeWindow() {
		f.dispose();
	}
	
	// function to create value pairs of code : country name
	private static Country[] createCountryList() {
        String[] countryCodes = Locale.getISOCountries();
        Country[] listCountry = new Country[countryCodes.length];
 
        for (int i = 0; i < countryCodes.length; i++) {
            Locale locale = new Locale("", countryCodes[i]);
            String code = locale.getCountry();
            String name = locale.getDisplayCountry(); 
            listCountry[i] = new Country(code, name);
        }
 
        Arrays.sort(listCountry); 
        return listCountry;
    }
	
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				displayLogin(null, false);
			}
		});
	}
	
	
}
