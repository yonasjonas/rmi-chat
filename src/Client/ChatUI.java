package Client;

import javax.swing.*;

import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import Client.ChatClient;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import javax.swing.event.ListSelectionListener;

public class ChatUI extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	static String name, message, dob, country, fcolour;
	private ChatClient chatClient;
	private JPanel textPanel;
	public JScrollPane scrollPane;
	private Integer usersCount = 0;
	// private DefaultListModel<String> listModel;
	static String SEND_ACTION = "loginAction";
	JTextPane textPane;
	//private static final String PM_ACTION = "PMAction";

	JTextField countUsersArea;
	protected JTextArea outputArea, userArea, inputArea, userList;
	protected JFrame frame;
	protected JButton privateMsgButton, startButton, sendButton, sendMsgButton;
	protected JPanel defaulUsersPanel, usersPanel, buttonSpace;
	protected JList<String> jList;
	protected DefaultListModel<String> defaultList;
	protected DefaultListModel<String> defaultMessageList;
	private Document document;
	StyledDocument doc;

	public static void main(String[] args) {

		// set the look
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
		} catch (Exception e) {
		}
		new ChatUI();

	}

	public ChatUI() {

		frame = new JFrame("Welcome");
		Container container = getContentPane();
		JPanel mainPanel = new JPanel(new BorderLayout());
		
		mainPanel.add(addOutputPanel(), BorderLayout.CENTER);		
		mainPanel.add(addInputPanel(), BorderLayout.SOUTH);		
		mainPanel.add(usersCount(usersCount), BorderLayout.NORTH);		
		container.setLayout(new BorderLayout());
		container.add(mainPanel, BorderLayout.CENTER);
		container.add(addRightSide(), BorderLayout.EAST);
		
		frame.add(container);
		//frame.setResizable(false);
		frame.setSize(900, 500);
		//frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		

		//loginPopUp();
		
		//
		//JOptionPaneTest.display();	
		
	 

		// String input = JOptionPane.showInputDialog(null);

		frame.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {

				if (chatClient != null) {
					try {
						sendMessage("Bye all, I am leaving");
						chatClient.IServer.leaveChat(name);
						
					} catch (RemoteException e) {
						e.printStackTrace();
					}
				}
				System.exit(0);
			}
		});
	}
	
	
	public JTextArea loginPopUp() {
		
		
		//outputArea.setText("");
		try {
			doc.insertString(0, "Name: " + name + "DOB: " + dob + "Country: " + country + "\n. Everyone say hi!\n", null );
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//outputArea.append("Name: " + name + "DOB: " + dob + "Country: " + country + "\n. Everyone say hi!\n");
		frame.setTitle("Chat away " + name);
		
		System.out.println("Name: " + name + "DOB: " + dob + "Country: " + country + "Colour: " + fcolour);
		try {
			getConnected(name, dob, country, fcolour);
			//System.out.println("getConnected(name, dob, country);"  +name+ dob+ country);
		} catch (RemoteException e1) {
			e1.printStackTrace();
		}
		

		return outputArea;
	}

	public JPanel addInputPanel() {
		
//		if (name == null) {
//			JOptionPaneTest.display();			
//		}

		JPanel inputPanel = new JPanel(new BorderLayout());
		inputArea = new JTextArea();
		inputPanel.setBorder(new EmptyBorder(5, 5, 10, 10));
		inputPanel.setPreferredSize(new Dimension(150,90));  
		inputPanel.add(addButton(), BorderLayout.EAST);
		inputPanel.add(inputArea);
		return inputPanel;
		
	}
	
	public JPanel addOutputPanel() {
		
		textPane = new JTextPane();
		textPane.setText( "Intro from add outpupnel" );
		doc = textPane.getStyledDocument();
		

		//  Define a keyword attribute

		SimpleAttributeSet keyWord = new SimpleAttributeSet();
		StyleConstants.setForeground(keyWord, Color.RED);

		//  Add some text

		try
		{
		    doc.insertString(0, "Welcome\n", null );
		    
		}
		catch(Exception e) { System.out.println(e); }

		//String welcomeMsg = "Enter your name \n";

		//outputArea = new JTextArea(welcomeMsg, 20, 40);
		//outputArea.setForeground(Color.RED);
		
		//outputArea.setMargin(new Insets(10, 10, 10, 10));
		//outputArea.setLineWrap(true);
//		outputArea.setWrapStyleWord(true);
//		outputArea.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(textPane);
		textPanel = new JPanel();
		scrollPane.setSize(700, 500);
		
		textPanel.add(scrollPane);
		
		

		return textPanel;

	}
	
	public JPanel addRightSide() {
		
		//usersPanel = new JPanel(new BorderLayout());
		usersPanel = new JPanel(new BorderLayout());
		usersPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		JLabel usersLabel = new JLabel("Users");
		usersLabel.setLayout(null);
		
		String[] noUsers = {"Login to view"};
		updateUsersPanel(noUsers);		
		usersPanel.add(usersLabel);		
		return usersPanel;		
		
	}
	
	public JPanel updateUsersPanel(String[] list) {

		usersCount = 0;
		defaulUsersPanel = new JPanel();
		defaultList = new DefaultListModel<String>();
		
		
		for(String u : list){
			defaultList.addElement("User: " + u);
			usersCount++;
			//System.out.print("Count: " + usersCount);
	    }
		
		jList = new JList<String>(defaultList);		
		jList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		jList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		jList.setVisibleRowCount(30);
		//jList.addListSelectionListener( new SelectingUser());
		
		//jList.setSelectedIndex(0);
		//jList.addListSelectionListener((ListSelectionListener) this);
		
		scrollPane = new JScrollPane(jList);

		defaulUsersPanel.add(scrollPane, BorderLayout.CENTER);	
		usersPanel.add(defaulUsersPanel, BorderLayout.CENTER);
		usersPanel.repaint();
		usersPanel.revalidate();
		
		countUsersArea.setText("Users:" + usersCount);
		countUsersArea.repaint();
		countUsersArea.revalidate();

		return usersPanel;
	}
	
	public JPanel usersCount(int usersCount) {
		JPanel countPanel = new JPanel();		
		countUsersArea = new JTextField();
		countUsersArea.setSize(20, 50);		
		countUsersArea.setMargin(new Insets(10, 10, 10, 10));
		countUsersArea.setEditable(false);
		countPanel.add(countUsersArea, BorderLayout.CENTER);
		return countPanel;
	}
	

	

	public JButton addButton() {

		buttonSpace = new JPanel(new GridLayout(1, 1, 5, 5));
		JButton sendMsgButton = new JButton("Send to all");
		sendMsgButton.setBounds(650, 250, 200, 150);
		sendMsgButton.setActionCommand(SEND_ACTION);
		sendMsgButton.addActionListener(this);
		buttonSpace.add(sendMsgButton);
		
		//inputArea.addKeyListener(this);
		inputArea.setFocusable(true);
		
		message = inputArea.getText();
		sendMsgButton.setEnabled(false);
		document = inputArea.getDocument();
		document.addDocumentListener(new JButtonStateController(sendMsgButton));
		
		
		return sendMsgButton;
	}
	
	
	


	public void actionPerformed(ActionEvent e) {
		//String count = usersCount.toString();
		countUsersArea.setText("Users:" + usersCount);
		System.out.print("Here is the value: " + jList.getSelectedValue());
		System.out.print("name: " + SEND_ACTION);
		message = inputArea.getText();
		int[] privateList = jList.getSelectedIndices();
		System.out.print("getSelectionIndex():" + privateList  );
		
		if (privateList.length > 0) {
			SEND_ACTION = "pmAction";
			
			//sendMsgButton.setText("Send to " + name );
		} else {
			SEND_ACTION = "sendAction";
			//sendMsgButton.setText("Send to all" );
		}
		// System.out.println("false: " + e.getActionCommand());
		if (SEND_ACTION == "loginAction") {
			
			//loginPopUp();
		} 
		if (SEND_ACTION == "pmAction") {
			sendMsgButton.setText("Send private" );
			//selected indices, in increasing order
			
			
			for(int i=0; i<privateList.length; i++){
				System.out.println("selected index :" + privateList[i]);
			}
			message = inputArea.getText();
			inputArea.setText("");
			try {
				sendPrivateMessage(privateList);
			} catch (RemoteException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}
		if (SEND_ACTION == "sendAction") {
			// .out.print("name: " + name);
			
			
			inputArea.setText("");

			try {
				sendMessage(message);
			} catch (RemoteException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

	}
	
	// rmi methods
	
	private void sendPrivateMessage(int[] list) throws RemoteException {
		String pm = name + " just sent you private a message: \n" + message + "\n";
		chatClient.IServer.sendPrivate(list, pm);
	}

	void getConnected(String name, String dob, String country, String colour) throws RemoteException {
		// remove whitespace and non word characters to avoid malformed url
		name = name.replaceAll("\\s+", "_");
		name = name.replaceAll("\\W+", "_");
		try {
			chatClient = new ChatClient(this, name, dob, country, colour);
			chatClient.startClient();
			
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	private void sendMessage(String chatMessage) throws RemoteException {
		chatClient.IServer.updateChat(name, chatMessage);
	}

	public void getUserList() {
		
	}

}

