import java.rmi.*;
import java.rmi.server.*;


public class ServerImpl extends UnicastRemoteObject implements ServerIntf{ 

	public 	ServerImpl() throws RemoteException{
	}
	
	
	public double add(double d1, double d2) throws RemoteException{
		return d1+d2;
	}
	
	public double sub(double d1, double d2) throws RemoteException{
		return d1-d2;
	}
	
	public double mul(double d1, double d2) throws RemoteException{
		return d1*d2;
	}
	
	public double div(double d1, double d2) throws RemoteException{
		
		try{
			return d1/d2;
		}
		catch (ArithmeticException e)
		{
			System.out.print("Cannot divide by Zero"+e.getMessage());
			return Double.NaN;
		}
		
		
		
	}
}
