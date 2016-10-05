package wetd;

import java.util.Date;
import java.util.HashMap;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;

/*
 * Returns a RDF model from the file System
 */
public class RDFStore {
	
	private HashMap<String, Model> store;
	
	private HashMap<String, Date> timer;
	
}
