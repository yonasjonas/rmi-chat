package Client;

import javax.swing.JButton;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

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