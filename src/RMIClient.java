/*
 * @Author: Stephen Collins
 * @Date: 28/11/2017
 * @Filename: RMIClient.java
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
import javax.swing.border.EmptyBorder;

public class RMIClient implements ActionListener{
	private JFrame frame;
	private JTextArea display;
	private static JTextArea systemMessage;
	public RemoteUtil remoteServer = null;
	private String math = "";
	private static final String ERROR = "Invalid format, valid format= opnd1 operator opnd2\n"
											+ "where opnd1 and opnd2 are a single Integer!";
	public int opnd1=0, opnd2=0;
	public char oper = ' ';

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
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
					String a = calculate(math); //get answer from server
					display.append(a); 
					systemMessage.setText("");
					if (!a.equals(ERROR)) systemMessage.append("Data recieved from server!");
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
	
	// Function that checks if the math string is valid then accesses remote objects if it is
	private String calculate(String math) throws RemoteException, ServerNotActiveException {
		//Math string will be 3 chars in length and be in format of int operator int if valid
		if (math.length() <= 0 || math.length() > 3) return ERROR;
		if (!(isInteger(math.charAt(0)) && isOperator(math.charAt(1)) && isInteger(math.charAt(2))))
			return ERROR;
		
		String ans = "";
		opnd1 = Character.getNumericValue(math.charAt(0));
		opnd2 = Character.getNumericValue(math.charAt(2));
		oper = math.charAt(1);
	
		switch(oper) {
		case '+':
			ans = remoteServer.add(opnd1, opnd2);
			break;
		case '-':
			ans = remoteServer.subtract(opnd1, opnd2);
			break;
		case '*':
			ans = remoteServer.mult(opnd1, opnd2);
			break;
		case '/':
			ans = remoteServer.div(opnd1, opnd2);
			break;
		}
		return ans;
	}
	
	//Helper method to determine if a char is a num
	private boolean isInteger(char n) {
		int num = Character.getNumericValue(n); 
		return num >=0 && num <=9;
	}

	//Helper method to determine if a char is a operator
	private boolean isOperator(char o) {
		return o== '+' || o== '-' || o=='*' || o=='/';
	}

	public static void main(String[] args){
		RMIClient rmic = new RMIClient();
	}
}
