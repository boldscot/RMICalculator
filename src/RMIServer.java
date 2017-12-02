/*
 * @Author: Stephen Collins
 * @Date: 28/11/2017
 * @Filename: RMIServer.java
 * @Brief: RMI server
 */

import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIServer {
	public static void main(String[] args) {
		try {
			RemoteUtilImpl impl=new RemoteUtilImpl();
			Registry registry = LocateRegistry.createRegistry( 1099 );
			registry.rebind("RMIServer", impl);

			//Naming.rebind("RMIServer", impl);
		} catch(Exception e) {
			System.out.println("Exception: " + e);
		}
	}
}
