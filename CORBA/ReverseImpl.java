import ReverseApp.*;
import org.omg.CORBA.*;

public class ReverseImpl extends ReversePOA {
    private ORB orb;

    public void setORB(ORB orb_val) {
        orb = orb_val;
    }

    public String reverse_string(String str) {
        return new StringBuilder(str).reverse().toString();
    }
    public String uppercase(String str) {
	return str.toUpperCase();
    }
}
