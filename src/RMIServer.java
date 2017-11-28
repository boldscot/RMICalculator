/*
 * @Author: Stephen Collins
 * @Date: 28/11/2017
 * @Filename: RemoteUtil.java
 * @Brief: RMI server
 */

import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIServer {
	public static void main(String[] args) {
		try {
			RemoteUtilImpl impl=new RemoteUtilImpl();
			//Need the following two lines if running in eclipse for first time
			Registry registry = LocateRegistry.createRegistry( 1099 );
			registry.rebind("RMIServer", impl);

			//Naming.rebind("RMIServer", impl); // Need to comment out this if running for first time in eclipse
		} catch(Exception e) {
			System.out.println("Exception: " + e);
		}
	}
}
