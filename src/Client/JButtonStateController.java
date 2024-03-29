package Client;

import javax.swing.JButton;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

//found this on stackoverflow
// sets button toEnabled if length of text is more that 0 characters and vice versa
class JButtonStateController implements DocumentListener {
    private JButton button;

    JButtonStateController(JButton b) {
        button = b;
    }
    public void changedUpdate(DocumentEvent e) {
        disableIfEmpty(e);
    }
    public void insertUpdate(DocumentEvent e){
        disableIfEmpty(e);
    }
    public void removeUpdate(DocumentEvent e){
        disableIfEmpty(e);
    }
    public void disableIfEmpty(DocumentEvent e) {  
        button.setEnabled(e.getDocument().getLength() > 0);
    }	
}