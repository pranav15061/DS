import java.rmi.*;
import java.util.*;


public class Client{

	public static void main(String [] args)
	{
		
		try{
			String serverUrl="rmi://localhost/Server";
			
			ServerIntf serverIntf= (ServerIntf) Naming.lookup(serverUrl);
			

		
			Scanner sc=new Scanner(System.in);
			
			
			double num1=sc.nextDouble();
			double num2=sc.nextDouble();
			
			
			
			
			System.out.println("Num1 is: "+num1);
			System.out.println("Num2 is: "+num2);
			
			double result1=serverIntf.add(num1,num2);
			double result2=serverIntf.sub(num1,num2);
			double result3=serverIntf.mul(num1,num2);
			double result4=serverIntf.div(num1,num2);
			
			System.out.println("Result of Addition = "+result1);
			System.out.println("Result of Subtraction = "+result2);
			System.out.println("Result of Multiplication = "+result3);
			System.out.println("Result of Division = "+result4);
			
		   }
		   catch(Exception e){
		   	System.out.println("Exception Occured"+e.getMessage());
		   }
	}
	
	
	
}
