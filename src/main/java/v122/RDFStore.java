package v122;

import java.io.File;
import java.util.Date;
import java.util.HashMap;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;

/**
 * Model storing/loading
 * 
 * @author Marvin Hofer
 *
 */
public class RDFStore {
	
	private HashMap<String, Model> store;
	
	private HashMap<String, Date> timer;
	
	public String getExisting() {
		return "";
	}
	
	/**
	 * Constructor
	 */
	public RDFStore() {
		
	}
	
	public void deleteFolder(File folder) {
        File[] files = folder.listFiles();
       
        if (null != files) { //some JVMs return null for empty dirs
            
        	for (File f: files) {
                
        		if (f.isDirectory()) {
        			
                    deleteFolder(f);
                
        		} else {
        			
        			f.delete();
        			
                }
            }
        }
        
     // has to delete file or empty folder
        folder.delete();
	}
}
