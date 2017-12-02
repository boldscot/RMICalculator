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
	
	private void updateServerText() throws ServerNotActiveException {
		jta.append("Request from client:\n");
		jta.append("Opnd1: " + opnd1 +"\n");
		jta.append("Opnd2: " + opnd2 +"\n");
		jta.append("Oper: " + oper +"\n");
		jta.append("Data to client: " + ans +"\n");
	}

	@Override
	public String add(int x, int y) throws RemoteException, ServerNotActiveException {
		opnd1 = x; opnd2 = y; oper = '+';
		ans =Integer.toString(opnd1 + opnd2);
		updateServerText();
		return ans;
	}

	@Override
	public String subtract(int x, int y) throws RemoteException, ServerNotActiveException {
		opnd1 = x; opnd2 = y; oper = '-';
		ans =Integer.toString(opnd1 - opnd2);
		updateServerText();
		return ans;
	}

	@Override
	public String mult(int x, int y) throws RemoteException, ServerNotActiveException {
		opnd1 = x; opnd2 = y; oper = '*';
		ans =Integer.toString(opnd1 * opnd2);
		updateServerText();
		return ans;
	}

	@Override
	public String div(int x, int y) throws RemoteException, ServerNotActiveException {
		opnd1 = x; opnd2 = y; oper = '/';
		if (opnd2 != 0) ans=Float.toString((float) opnd1 / opnd2);
		else ans="Undifined, division  by zero!";
		
		updateServerText();
		return ans;
	}
}