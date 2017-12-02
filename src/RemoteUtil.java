/*
 * @Author: Stephen Collins
 * @Date: 28/11/2017
 * @Filename: RemoteUtil.java
 * @Brief: Interface for RMI server
 */

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.ServerNotActiveException;

public interface RemoteUtil extends Remote {
	public String getMessage() throws RemoteException, ServerNotActiveException;
	public String add(int opnd1, int opnd2) throws RemoteException, ServerNotActiveException;
	public String subtract(int opnd1, int opnd2) throws RemoteException, ServerNotActiveException;
	public String mult(int opnd1, int opnd2) throws RemoteException, ServerNotActiveException;
	public String div(int opnd1, int opnd2) throws RemoteException, ServerNotActiveException;
}