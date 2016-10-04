package wetd;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/* TODO
 * Use RDF Unit over the data sets
 */

public class OnRDFUnit {
	String result = null;
	
	public void foo() {
	try {                 

	    String[] cmd = { "/bin/sh", "-c", "cd ./RDFUnit-0.8; bin/rdfunit -h" };
	    Process p = Runtime.getRuntime().exec(cmd);

	    BufferedReader in =
	        new BufferedReader(new InputStreamReader(p.getInputStream()));
	    String inputLine;
	    while ((inputLine = in.readLine()) != null) {
	        System.out.println(inputLine);
	        result += inputLine;
	    }
	    in.close();

	} catch (IOException e) {
	    System.out.println(e);
	}
	}
}
