package wetd;


import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;

//import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class RDFFilterTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		Model model = ModelFactory.createDefaultModel();
		
		model.read("./RDF_EXAMPLES/film_runtime_100/dataset.nt", "N-TRIPLES");
		System.out.println("1");
		RDFFilter rf = new RDFFilter(model);
		System.out.println("1");
		rf.new_filter("", "http://dbpedia.org/property/runtime", "http://dbpedia.org/datatype/minute");
		System.out.println("1");
		rf.getModel().write(System.out, "N-TRIPLES");
		System.out.println("1");
		/*
		 * for property http://dbpedia.org/property/runtime

			93	http://dbpedia.org/datatype/second
		 */
		
		
		//rf.useFilter_p_o("http://dbpedia.org/property/gross", "http://dbpedia.org/datatype/usDollar");
		
		
		//rf.new_filter("","","");
		/* Crash new functions
		 * 
		 * System.out.println("done");
		
		rf.remove_without_p("prefix dbo: <http://dbpedia.org/ontology/>"+
				  "prefix dbp: <http://dbpedia.org/property/>"+
				  "construct {?film a dbo:Film. ?film dbp:runtime ?runtime.} "+				
				  "where {?film a dbo:Film. ?film dbp:runtime ?runtime.}");
		System.out.println("done");
		
		rf.remove_dublicates("http://dbpedia.org/property/runtime", "new_dataset.nt");
		
		System.out.println("done");*/
	}

}
