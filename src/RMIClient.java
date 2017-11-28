/*
 * @Author: Stephen Collins
 * @Date: 28/11/2017
 * @Filename: RemoteUtil.java
 * @Brief: RMI Client
 */

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.*;
import java.rmi.server.ServerNotActiveException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

public class RMIClient implements ActionListener{
	private JFrame frame;
	private JTextArea display;
	private static JTextArea systemMessage;
	public RemoteUtil remoteServer = null;
	private String math = "";

	public RMIClient() {
		makeGui();
		try {
			String serverURL="//localhost/RMIServer";
			remoteServer= (RemoteUtil)Naming.lookup(serverURL);
			systemMessage.append(remoteServer.getMessage());
		} catch(Exception e) {
			System.out.println(e.getStackTrace());
			systemMessage.setText("");
			systemMessage.append("Error 3: server connection");
		}
	}
	
	// Create GUI for client interface
	private void makeGui() {
		frame = new JFrame("Calculator");
		JPanel contentPane = (JPanel)frame.getContentPane();
		contentPane.setLayout(new BorderLayout(8, 8));
		contentPane.setBorder(new EmptyBorder( 10, 10, 10, 10));
		
		display = new JTextArea(3, 10);
		systemMessage = new JTextArea(3, 10);
		contentPane.add(display, BorderLayout.NORTH);
		contentPane.add(systemMessage, BorderLayout.SOUTH);

		JPanel buttonPanel = new JPanel(new GridLayout(4, 4, 3, 3));
		addButton(buttonPanel, "/");
		addButton(buttonPanel, "7");
		addButton(buttonPanel, "8");
		addButton(buttonPanel, "9");
		addButton(buttonPanel, "*");
		addButton(buttonPanel, "4");
		addButton(buttonPanel, "5");
		addButton(buttonPanel, "6");
		addButton(buttonPanel, "-");
		addButton(buttonPanel, "1");
		addButton(buttonPanel, "2");
		addButton(buttonPanel, "3");
		addButton(buttonPanel, "+");
		addButton(buttonPanel, "0");
		addButton(buttonPanel, "Submit");
		contentPane.add(buttonPanel, BorderLayout.CENTER);

		frame.pack();
		frame.setVisible(true);
		// need to close frame as default behavior is to hide on exit
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}

	//Add a button to the button panel.
	private void addButton(Container p, String buttonText) {
		JButton button = new JButton(buttonText);
		button.addActionListener(this);
		p.add(button);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch(e.getActionCommand().charAt(0)) {
		case '+':
		case '-':
		case '*':
		case '/':
		case '0':
		case '1':
		case '2':
		case '3':
		case '4':
		case '5':
		case '6':
		case '7':
		case '8':
		case '9':
			if (math.equals("")) display.setText(""); // clear display if new math problem
			display.append(e.getActionCommand()); // add button presses to display
			math+=e.getActionCommand(); // add button presses to math string
			break;
		case 'S':
			try {
				display.setText(""); // clear the display
				if (remoteServer != null)  {
					display.append(remoteServer.solve(math)); //get answer from server
					systemMessage.setText("");
					systemMessage.append("Data recieved from server!");
				}
				else  {
					systemMessage.setText("");
					systemMessage.append("Error 1: server connection");
				}
				math = ""; //reset math string for new math problem
			} catch (RemoteException | ServerNotActiveException e1) {
				systemMessage.setText("");
				systemMessage.append("Error 2: server connection");
			}
			break;
		}
	}

	public static void main(String[] args){
		RMIClient rmic = new RMIClient();
	}
}
