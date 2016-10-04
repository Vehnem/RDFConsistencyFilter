package wetd;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;

/*
 * TODO Filter all 
 * Filter for more consistency in the RDFFile
 */
public class RDFFilter {

/*
 * Model for the RDF data graph 
 */
	private Model dataModel;
	private String dataKeyLocation = "";
	
/**
 * Constructor
 * 
 * @param dataModel RDF-InMemory Model
 */
	public RDFFilter(Model dataModel) {
		this.dataModel = dataModel;
	}
	
/*
 * Use filter over the data at an specific property 
 * and for a selected datatype
 * 
 * @param property
 * @param datatype
 */
	public void useFilter_p_o(String property, String datatype) {

		Model model = ModelFactory.createDefaultModel();
		model.read(dataModel+"/dataset.nt", "N-TRIPLES");
		
		Model filter_model = ModelFactory.createDefaultModel();
		
		StmtIterator iter = model.listStatements();
		
		while (iter.hasNext()) {
		    Statement stmt      = iter.nextStatement(); 
		    Property  predicate = stmt.getPredicate();
		    RDFNode   object    = stmt.getObject();
		    
		    if(!predicate.toString().equals(property)) {
		    	filter_model.add(stmt);
		    //TODO hasLiteral is important
		    } else if (predicate.toString().equals(property) && object.asLiteral().getDatatypeURI().toString().equals(datatype)) {
		    	filter_model.add(stmt);
		    }
		}
		    
		  //save new RDFmodel
		FileOutputStream fileStream;
		OutputStreamWriter outputWriter;
			
		try {
			fileStream = new FileOutputStream(new File(dataModel+"/result_dataset.nt"));
			outputWriter = new OutputStreamWriter(fileStream, "UTF-8");
				
			filter_model.write(outputWriter, "N-TRIPLES");
			outputWriter.close();
			fileStream.close();
			filter_model.close();
			model.close();
			}
		catch(IOException e) {System.out.println("ERROR at storing RDF");}
	}
	
	/*
	 * Creates a new dataset only with the resources that already have the right property/datatype
	 * You need the query from start
	 * 
	 * @param queryString query
	 */
	public void remove_without_p (String queryString) {
		Model m = ModelFactory.createDefaultModel();
		m.read(dataModel+"/result_dataset.nt", "N-TRIPLES");
		
		
		Query query = QueryFactory.create(queryString);
		
		QueryExecution qexec = QueryExecutionFactory.create(query, m);
		
		Model n = ModelFactory.createDefaultModel();
		
		n = qexec.execConstruct();
		m.close();
		
		//save new RDFmodel
		FileOutputStream fileStream;
		OutputStreamWriter outputWriter;
			
		try {
			fileStream = new FileOutputStream(new File(dataKeyLocation+"/result_dataset.nt"));
			outputWriter = new OutputStreamWriter(fileStream, "UTF-8");
				
			n.write(outputWriter, "N-TRIPLES");
			outputWriter.close();
			fileStream.close();
			n.close();
		}
		catch(IOException e) {System.out.println("ERROR at storing RDF");}
	}
	
	/* TODO Test by saving all duplicates in a independent file
	 * remove duplicated values for one property (take the first)
	 * 
	 * @param property 
	 * @param filename
	 */
	public void remove_dublicates(String property, String filename) {
		Model model = ModelFactory.createDefaultModel();
		model.read(dataKeyLocation+"/"+filename, "N-TRIPLES");
		Model new_model = ModelFactory.createDefaultModel();
		
		StmtIterator iter = model.listStatements();
		String old_subject = "";
		boolean property_found = false;
		
		while (iter.hasNext()) {
		    Statement stmt      = iter.nextStatement(); 
		    Property  predicate = stmt.getPredicate();
		    Resource  subject   = stmt.getSubject();
		    
		    
		    if(old_subject.toString().equals(subject.toString())) {
		    	if(predicate.toString().equals(property) && !property_found) {
		    		new_model.add(stmt);
		    		property_found = true;
		    	} else if(!predicate.toString().equals(property)) {
		    		new_model.add(stmt);
		    	}
		    } else {
		    	new_model.add(stmt);
		    	old_subject = subject.toString();
		    	property_found = false;
		    }
		}
		
		model.close();
		
		//save new RDFmodel 
		FileOutputStream fileStream;
		OutputStreamWriter outputWriter;
			
		try {
			fileStream = new FileOutputStream(new File(dataKeyLocation+"/result_dataset.nt"));
			outputWriter = new OutputStreamWriter(fileStream, "UTF-8");
				
			new_model.write(outputWriter, "N-TRIPLES");
			outputWriter.close();
			fileStream.close();
			
		}
		catch(IOException e) {System.out.println("ERROR at storing RDF");}
		
		new_model.close();
	}
	
	public void new_filter(String dataKeyLocation, String property, String datatype) {
		Model new_model = ModelFactory.createDefaultModel();
		
		StmtIterator iter = dataModel.listStatements();
		
		String temp_subject = "";
		
		boolean changed = false;
		
		while(iter.hasNext()) {
		
			Statement stmt      = iter.nextStatement(); 
		    Property  predicate = stmt.getPredicate();
		    Resource  subject   = stmt.getSubject();
		    RDFNode   object    = stmt.getObject();
		    
		    if(!temp_subject.equals(subject.toString())) {
		    	temp_subject = subject.toString();
		    	changed = true;
		    }
		    
		    //TODO naiv
		    if(predicate.toString().equals(property)) {
		    	if(object.isLiteral()) {
		    		if(object.asLiteral().toString().equals(datatype));
		    			new_model.add(stmt);
		    	} else {
		    		if(object.toString().equals(datatype)) {
		    			new_model.add(stmt);
		    		}
		    	}
		    } else {
		    	new_model.add(stmt);
		    }
		    
		    
		    /*
			//save new RDFmodel 
			FileOutputStream fileStream;
			OutputStreamWriter outputWriter;
				
			try {
				fileStream = new FileOutputStream(new File(data+"/result_dataset.nt"));
				outputWriter = new OutputStreamWriter(fileStream, "UTF-8");
					
				new_model.write(outputWriter, "N-TRIPLES");
				outputWriter.close();
				fileStream.close();
				
			}
			catch(IOException e) {System.out.println("ERROR at storing RDF");}*/
		    
			iter.next();
		}
		
		dataModel.removeAll();
		
		
		dataModel.add(new_model);
		new_model.close();
	}
	
	public Model getModel() {
		return this.dataModel;
	}
}
