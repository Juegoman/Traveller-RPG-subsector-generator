/**
 *
 * @author Ruben
 */
import java.io.*;
import java.util.*;

public class TravellerSubsectorGen {
    public static void main(String[] args) throws IOException {
        Random rand = new Random();
        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("subsector.txt")));
        String[] params = new String[3];
        params[0] = "In";
        params[1] = "Ht";
        params[2] = "Hi";
        Subsector sect = new Subsector(params);
        out.println(sect);
        out.close();
        System.out.println("Success!\n" + sect);
        System.exit(0);
    }
}
