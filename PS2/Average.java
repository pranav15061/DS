import mpi.MPIException;
import mpi.MPI;
import java.util.*;



public class Average{
	public static void main(String []args) throws MPIException{
	
	
		MPI.Init(args);
		
		int rank=MPI.COMM_WORLD.Rank();
		int size=MPI.COMM_WORLD.Size();
		
		
		int root=0;
		
		int TotalElements=16;
		
		int ElementsperProcess=TotalElements/size;
		
		
		double [] Fullarray=new double [TotalElements];
		double [] Subarray=new double [ElementsperProcess];
		double [] GatherAverages=new double [size];
		
		
		if(rank==root)
		{
			Fullarray=new double [TotalElements];
			
			Random rand=new Random();
			System.out.println("Intitalising Random array at root: ");
			
			for(int i=0;i<TotalElements;i++)
			{
				Fullarray[i]=(double)(rand.nextInt(100)+1);
				System.out.print(Fullarray[i]+" ");
			}
			System.out.println();
			
		}
		
		
		MPI.COMM_WORLD.Scatter(Fullarray,0,ElementsperProcess,MPI.DOUBLE,
					Subarray,0,ElementsperProcess,MPI.DOUBLE,
					root);
					
					
		double localsum=0;
		
		for(double val:Subarray)
		{
			localsum+=val;
		}
		
		double localavg=(1.0*localsum)/ElementsperProcess;
		System.out.println("Process"+rank+" local average: "+localavg);
		
		
		MPI.COMM_WORLD.Gather(new double[]{localavg},0,1,MPI.DOUBLE,
					GatherAverages,0,1,MPI.DOUBLE,
					root);
					
					
					
		if(rank==root){
			double totalavg=0;
			
			for(double avg:GatherAverages)
			{
				totalavg+=avg;
			}
			
			totalavg=(1.0*totalavg)/size;
			
			System.out.println("Final Average is: "+totalavg);
			

		}
		
		
		MPI.Finalize();
					
		
		
		
		
		
		
		
	}
}
