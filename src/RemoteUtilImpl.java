/*
 * @Author: Stephen Collins
 * @Date: 28/11/2017
 * @Filename: RemoteUtilImpl.java
 * @Brief: Interface implementation for RMI server
 */

import java.awt.BorderLayout;
import java.rmi.RemoteException;
import java.time.ZonedDateTime;
import java.rmi.server.ServerNotActiveException;
import java.rmi.server.UnicastRemoteObject;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;

@SuppressWarnings("serial")
public class RemoteUtilImpl extends UnicastRemoteObject implements RemoteUtil {
	private JFrame frame = new JFrame();
	//Text area for displaying contents
	private JTextArea jta = new JTextArea();
	private int opnd1 = 0;
	private int opnd2 = 0;
	private char oper = '?';
	private String ans = "";
	
	public RemoteUtilImpl() throws RemoteException {
		// Place text area on the frame
		frame.setLayout(new BorderLayout());
		frame.add(new JScrollPane(jta), BorderLayout.CENTER);
		frame.setTitle("Server");
		frame.setSize(500, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true); // It is necessary to show the frame here!
	}

	@Override
	public String getMessage() throws RemoteException, ServerNotActiveException {
		jta.append("Client connected at ip"+ getClientHost() +"\n");
		return "Hi! from Server\nToday is "+ ZonedDateTime.now();
	}

	@Override
	public String solve(String math) throws RemoteException, ServerNotActiveException {
		//Math string will be 3 chars in length and be in format of int operator int if valid
		if (math.length() <= 0 || math.length() > 3) return error();
		if (!(isInteger(math.charAt(0)) && isOperator(math.charAt(1)) && isInteger(math.charAt(2))))
			return error();

		opnd1 = Character.getNumericValue(math.charAt(0));
		opnd2 = Character.getNumericValue(math.charAt(2));
		oper = math.charAt(1);

		switch(oper) {
		case '+':
			ans = Integer.toString(opnd1 + opnd2);
			break;
		case '-':
			ans = Integer.toString(opnd1 - opnd2);
			break;
		case '*':
			ans = Integer.toString(opnd1 * opnd2);
			break;
		case '/':
			if (opnd2 != 0) ans = Float.toString((float) opnd1 / opnd2);
			else ans = "Undifined, division  by zero!";
			break;
		}
		updateServerText();
		return ans;
	}

	@Override
	public String error() throws RemoteException {
		return "Invalid format, valid format= n op n \n"
				+ "where n is a single digit and op is an operator!";
	}
	
	public boolean isInteger(char n) {
		int num = Character.getNumericValue(n); 
		return num >=0 && num <=9;
	}

	public boolean isOperator(char o) {
		return o== '+' || o== '-' || o=='*' || o=='/';
	}
	
	private void updateServerText() throws ServerNotActiveException {
		jta.append("Request from client:\n");
		jta.append("Opnd1: " + opnd1 +"\n");
		jta.append("Opnd2: " + opnd2 +"\n");
		jta.append("Oper: " + oper +"\n");
		jta.append("Data to client: " + ans +"\n");
	}
}