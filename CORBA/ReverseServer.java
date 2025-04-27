import ReverseApp.*;
import org.omg.CORBA.*;
import org.omg.PortableServer.*;
import org.omg.PortableServer.POA;
import java.io.*;

public class ReverseServer {
    public static void main(String args[]) {
        try {
            ORB orb = ORB.init(args, null);
            POA rootpoa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
            rootpoa.the_POAManager().activate();

            ReverseImpl reverseImpl = new ReverseImpl();
            reverseImpl.setORB(orb);

            org.omg.CORBA.Object ref = rootpoa.servant_to_reference(reverseImpl);
            Reverse href = ReverseHelper.narrow(ref);

            // Write IOR to a file
            FileOutputStream file = new FileOutputStream("ReverseIOR.txt");
            PrintWriter writer = new PrintWriter(file);
            writer.println(orb.object_to_string(href));
            writer.close();

            System.out.println("Server ready...");
            orb.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
