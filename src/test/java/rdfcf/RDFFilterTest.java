package rdfcf;


import static org.junit.Assert.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.StmtIterator;

//import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import rdfcf.RDFFilter;


/**
 * Testing the rdfcf Filter
 * 
 * @author marvin
 *
 */
public class RDFFilterTest {

	@Rule
	public TemporaryFolder folder = new TemporaryFolder();
	
	static Model model = ModelFactory.createDefaultModel();
	
	static RDFFilter rf;
	
	static String filter = "pa:da1;pb:db2";
	
	static List<HashMap<String, List<String>>> list;
	
	static List<String> f_p;
	
	static List<List<String>> f_d;
	
	static Model filtered;
	
	@Test
	public void test() throws IOException {
		
		model.read("./RDF_EXAMPLES/example/dataset.nt", "N-TRIPLES");
		
		rf = new RDFFilter(model);;
		
		list = rf.get_pos(filter);
		
		f_p = rf.get_p(filter);
		
		f_d = rf.get_o(filter);
		
		filtered = rf.new_filter2(f_p, f_d, false, false);
	
		
		FileOutputStream fileStream;
		OutputStreamWriter outputWriter;
				
		final File expected = new File("./RDF_EXAMPLES/example/expected_filter.nt");
		final File output = folder.newFile("filter.nt");
		
		try{
			fileStream = new FileOutputStream(output);
			outputWriter = new OutputStreamWriter(fileStream, "UTF-8");
			
			filtered.write(outputWriter, "N-TRIPLES");
			} catch (IOException e){
				
			}
	
		try {
			assertEquals(FileUtils.readFileToString(expected), FileUtils.readFileToString(output));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}

}
