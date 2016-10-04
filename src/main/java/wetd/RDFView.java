package wetd;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;

/*
 * Returns a RDF model from the file System
 */
public class RDFView {
	
	/*
	 * Location of the data
	 */
	private String location;
	
	/*
	 * Constructor
	 * 
	 * @param location Location of the data
	 */
	public RDFView(String location) {
		this.location = location;
	}
	
	/*
	 * Retun the RDF as Model
	 * 
	 * @param filename name of the file
	 */
	public Model getNT(String filename) {
		Model model = ModelFactory.createDefaultModel();
		model.read(location+"/"+filename, "N-TRIPLES");
		return model;
	}
}
