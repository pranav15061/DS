import mpi.MPIException;
import mpi.MPI;
import java.util.*;




public class Reciprocal{
	public static void main(String []args) throws MPIException{
	
	
		MPI.Init(args);
		
		int rank=MPI.COMM_WORLD.Rank();
		int size=MPI.COMM_WORLD.Size();
		
		
		int root=0;
		
		
		int [] sendBuffer=new int[size];
		int [] recvBuffer=new int[1];
		double [] gatherBuffer=new double[size];
		
		
		if(rank==root)
		{
			sendBuffer=new int [size];
			System.out.println("Intitalising array at root: ");
			
			for(int i=0;i<size;i++)
			{
				sendBuffer[i]=i+1;
				
				System.out.println("Element "+i+"="+sendBuffer[i]);
			}
		}
		
		
		
		MPI.COMM_WORLD.Scatter(sendBuffer,0,1,MPI.INT,
					recvBuffer,0,1,MPI.INT,
					root);
					
					
		double localreciprocal=1.0/recvBuffer[0];
		
		System.out.println("Process "+rank+" "+"received: "+recvBuffer[0]+" and generated: "+localreciprocal);
		
		
		
		MPI.COMM_WORLD.Gather(new double[]{localreciprocal},0,1,MPI.DOUBLE,
					gatherBuffer,0,1,MPI.DOUBLE,
					root);
					
					
		if(rank==root){
			System.out.println("Final Array of Reciprocal at root: ");
			
			for(int i=0;i<size;i++)
			{
				System.out.printf("1/%d= %.4f\n",sendBuffer[i],gatherBuffer[i]);
			}
		}
		
		MPI.Finalize();
		
		
		
	}
}
		
		
		
					
					
		
		
