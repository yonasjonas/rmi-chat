package Client;

import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.Arrays;
import java.util.Locale;

import javax.swing.*;

/** @see https://stackoverflow.com/a/3002830/230513 */
class RegisterLoginPopup {
	
	static void displayLogin() {
		
		//Boolean chatInitiated = false;
		Country[] listCountry = createCountryList();
		JComboBox<Country> combo = new JComboBox<>(listCountry);
		//JComboBox<String> combo = new JComboBox<>(countryCodes);

		JTextField name = new JTextField();
		JPanel panel = new JPanel(new GridLayout(0, 1));
		panel.add(new JLabel("\n Enter you Name"));
		panel.add(name);
		
		// Panel for pop up
		
		// Frame for pop up
		JFrame f = new JFrame();
		
		// Name label and field
		JPanel p = new JPanel();
		final JTextField text = new JTextField(20);
		p.add(new JLabel("\n Date of Birth:"));
		p.add(text);
		JButton b = new JButton("open calendar");
		
		// DOB panel, label and field
		
		
		
		
		//add button
		p.add(b);
		
		panel.add(p);
		
		// Country label and field
		p.add(new JLabel("\n Country:"));
		panel.add(combo);
		panel.setVisible(true);
		b.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				text.setText(new DatePicker(f).setPickedDate());
			}
		});
		

		int result = JOptionPane.showConfirmDialog(null, panel, "Enter your details to login", JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.PLAIN_MESSAGE);
		if (result == JOptionPane.OK_OPTION) {
			
			
			Country selectedCountry = (Country) combo.getSelectedItem();
			ChatUI.name = name.getText();
			ChatUI.dob = text.getText();
			ChatUI.country = selectedCountry.toString();
				
			System.out.println("Name: " + name.getText() + "DOB: " + text.getText() + "Country: " + combo.getSelectedItem());
			ChatUI.SEND_ACTION = "sendAction";
			
			if (ChatUI.name != null) {
				//panel.dispatchEvent(new WindowEvent(f, WindowEvent.WINDOW_CLOSING));
				ChatUI ch = new ChatUI();
				ch.loginPopUp();
				//panel.dispatchEvent(new WindowEvent(f, WindowEvent.WINDOW_CLOSING));
			}
		} else {
			System.out.println("Cancelled");
			//display();
		}
	}
	
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
				displayLogin();
			}
		});
	}
}