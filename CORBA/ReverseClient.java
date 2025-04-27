import ReverseApp.*;
import org.omg.CORBA.*;
import java.io.*;

public class ReverseClient {
    public static void main(String args[]) {
        try {
            ORB orb = ORB.init(args, null);

            // Read IOR from file
            BufferedReader br = new BufferedReader(new FileReader("ReverseIOR.txt"));
            String ior = br.readLine();
            br.close();

            org.omg.CORBA.Object objRef = orb.string_to_object(ior);
            Reverse reverseRef = ReverseHelper.narrow(objRef);

            String input = "CORBA is cool";
            String reversed = reverseRef.reverse_string(input);
            String upperCase = reverseRef.uppercase(input);

            System.out.println("Original: " + input);
            System.out.println("Reversed: " + reversed);
            System.out.println("Uppercase: " + upperCase);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
