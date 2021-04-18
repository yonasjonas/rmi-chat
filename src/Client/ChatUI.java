package Client;

import javax.swing.*;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;

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

public class ChatUI extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	static String name, message, dob, country;
	private ChatClient chatClient;
	private JPanel textPanel;
	// private DefaultListModel<String> listModel;
	static String SEND_ACTION = "loginAction";
	//private static final String PM_ACTION = "PMAction";

	protected JTextArea outputArea, userArea, inputArea, userList;
	protected JFrame frame;
	protected JButton privateMsgButton, startButton, sendButton, sendMsgButton;
	protected JPanel defaulUsersPanel, usersPanel, buttonSpace;
	protected JList<String> jList;
	protected DefaultListModel<String> defaultList;
	private Document document;

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
		container.setLayout(new BorderLayout());
		container.add(mainPanel, BorderLayout.CENTER);
		container.add(addRightSide(), BorderLayout.EAST);
		
		frame.add(container);
		frame.setResizable(false);
		frame.setSize(700, 500);
		frame.pack();
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
		
		outputArea.setText("");
		outputArea.append("New Person just joined : " + name + ". Everyone say hi!\n");
		frame.setTitle("Chat away " + name);
		//String details = name + "~~" + dob + "~~" + country;
		
		System.out.println("Name: " + name + "DOB: " + dob + "Country: " + country);
		try {			
			
			getConnected(name, dob, country);

		} catch (RemoteException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
				//Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
	    //int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
	    //int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
	  
		//outputArea.setLocation(null);

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
	
	public JPanel addRightSide() {
		
		
		String usersmsg = "Login to see who is online";
		//usersPanel = new JPanel(new BorderLayout());
		usersPanel = new JPanel(new GridLayout(0, 1, 0, -387));
		usersPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

		
		JLabel usersLabel = new JLabel(usersmsg);
		usersLabel.setLayout(null);
		
		usersPanel.add(usersLabel);
		
		return usersPanel;
		
		
	}
	
	public JPanel updateUsersPanel(String[] list) {

		
		//defaulUsersPanel = new JPanel();
		defaultList = new DefaultListModel<String>(); //data has type Object[]
		
		for(String u : list){
			defaultList.addElement("User: " + u);
	    }
		
		jList = new JList<String>(defaultList);		
		jList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		jList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		jList.setVisibleRowCount(-1);
		
		JScrollPane scrollPane = new JScrollPane(jList);
		usersPanel.add(scrollPane, BorderLayout.CENTER);

		return usersPanel;
	}

	public JPanel addOutputPanel() {

		String welcomeMsg = "Enter your name";

		outputArea = new JTextArea(welcomeMsg, 20, 40);
		outputArea.setSize(750, 500);
		
		outputArea.setMargin(new Insets(10, 10, 10, 10));
		outputArea.setLineWrap(true);
		outputArea.setWrapStyleWord(true);
		outputArea.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(outputArea);
		textPanel = new JPanel();
		textPanel.add(scrollPane);

		return textPanel;

	}

	public JButton addButton() {

		buttonSpace = new JPanel(new GridLayout(1, 1, 5, 5));
		JButton sendMsgButton = new JButton("Send");
		sendMsgButton.setBounds(550, 350, 100, 50);
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
//		
		System.out.print("name: " + SEND_ACTION);
		message = inputArea.getText();
//		if (e.getActionCommand().equals(SEND_ACTION)) {
//			System.out.println("true");
//		} else {
//			
//		}
		// System.out.println("false: " + e.getActionCommand());
		if (SEND_ACTION == "loginAction") {
			//loginPopUp();
		} 
		else if (SEND_ACTION == "sendAction") {
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

	void getConnected(String name, String dob, String country) throws RemoteException {
		// remove whitespace and non word characters to avoid malformed url
		name = name.replaceAll("\\s+", "_");
		name = name.replaceAll("\\W+", "_");
		try {
			chatClient = new ChatClient(this, name, dob, country);
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

