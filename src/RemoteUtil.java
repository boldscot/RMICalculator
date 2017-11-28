import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.ServerNotActiveException;

public interface RemoteUtil extends Remote {
	public String getMessage() throws RemoteException, ServerNotActiveException;
	public String solve(String math) throws RemoteException, ServerNotActiveException;
	public String error() throws RemoteException;
}