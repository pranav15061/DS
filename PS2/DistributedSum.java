import mpi.MPI;
import mpi.MPIException;

public class DistributedSum {

    public static void main(String[] args) throws MPIException {

        // Initialize MPI
        MPI.Init(args);
        int rank = MPI.COMM_WORLD.Rank();     // Current process ID
        int size = MPI.COMM_WORLD.Size();     // Total number of processes
        int root = 0;

        int[] sendBuffer=new int [size];
	int[] recvBuffer = new int[1]; // EVERY process must allocate this
	int[] gatherBuffer=new int [size];

	if (rank == root) {
	    sendBuffer = new int[size];
	    System.out.println("Root initializing data:");
	    for (int i = 0; i < size; i++) {
		sendBuffer[i] = i + 1;
		System.out.println("Element[" + i + "] = " + sendBuffer[i]);
	    }
	 
	}
        // Distribute one element to each process
        MPI.COMM_WORLD.Scatter(
                sendBuffer, 0, 1, MPI.INT,
                recvBuffer, 0, 1, MPI.INT,
                root);

       	
       	int localproduct=recvBuffer[0];
        System.out.println("Process " + rank + " received " + recvBuffer[0] +
                " and computed intermediate sum: " + localproduct);

        // Gather all intermediate sums to root
        MPI.COMM_WORLD.Gather(
                new int[]{localproduct}, 0, 1, MPI.INT,
                gatherBuffer, 0, 1, MPI.INT,
                root);

        // Root computes final sum
        if (rank == root) {
            int totalPro = 1;
            for (int val : gatherBuffer) {
                totalPro *= val;
            }
            System.out.println("Final sum at root: " + totalPro);
        }

        MPI.Finalize();
    }
}

