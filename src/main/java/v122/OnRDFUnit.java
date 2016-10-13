package v122;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/* 
 * Use RDF Unit over the data sets
 */

/**
 * 
 * @author marvin
 *
 */
public class OnRDFUnit {
	
	/**
	 * Call RDFUnit over Runtime
	 * 
	 * @param params
	 */
	public void runRDFUnit_cmdline(String params) {
		String result ="";
		
		String[] cmd = { "/bin/sh", "-c", "cd ./RDFUnit-0.8; bin/rdfunit -d"+params};
		
		try {
		Process p = Runtime.getRuntime().exec(cmd);

	    BufferedReader in =
	        new BufferedReader(new InputStreamReader(p.getInputStream()));
	    String inputLine;
	    while ((inputLine = in.readLine()) != null) {
	        System.out.println(inputLine);
	        result += inputLine;
	    }
	    in.close();
	    
	    System.out.println(result);

		} catch (IOException e) {
			    System.out.println(e);
		}
	}
	
	public void runRDFUnit() {
		
	}
	
	/* bin/rdfunit 
	 * 
	 * -d Path to File
	 * -v no LOV
	 * 
	 */
	
	//Runtime call example
	public void foo() {
		try {                 
			String result = "";
		    String[] cmd = { "/bin/sh", "-c", "cd ../RDFUnit-0.8; bin/rdfunit -h" };
		    Process p = Runtime.getRuntime().exec(cmd);

		    BufferedReader in =
		        new BufferedReader(new InputStreamReader(p.getInputStream()));
		    String inputLine;
		    while ((inputLine = in.readLine()) != null) {
		        System.out.println(inputLine);
		        result += inputLine;
		    }
		    in.close();
		    
		    System.out.println(result);

		} catch (IOException e) {
		    System.out.println(e);
		}
	}
}
