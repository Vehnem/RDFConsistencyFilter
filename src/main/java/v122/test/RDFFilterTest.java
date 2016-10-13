package v122.test;


import java.util.ArrayList;
import java.util.List;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;

//import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import v122.RDFFilter;

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
		
		model.read("./RDF_EXAMPLES/film_released_100/dataset.nt", "N-TRIPLES");
		System.out.println("1");
		RDFFilter rf = new RDFFilter(model);
		System.out.println("2");
		
		List<String> list = new ArrayList<String>();
		
		list.add("http://www.w3.org/2001/XMLSchema#gMonthDay");
		list.add("http://www.w3.org/1999/02/22-rdf-syntax-ns#langString");
		
		Model model2 = rf.new_filter("http://dbpedia.org/property/released", list, false, true);
		System.out.println("3");
		
		/*
		rf.useFilter_p_o("http://dbpedia.org/property/gross", "http://dbpedia.org/datatype/usDollar");
				
		rf.remove_without_p("prefix dbo: <http://dbpedia.org/ontology/>"+
				  "prefix dbp: <http://dbpedia.org/property/>"+
				  "construct {?film a dbo:Film. ?film dbp:runtime ?runtime.} "+				
				  "where {?film a dbo:Film. ?film dbp:runtime ?runtime.}");
		System.out.println("done");
		
		rf.remove_dublicates("http://dbpedia.org/property/runtime", "new_dataset.nt");
		
		System.out.println("done");
		*/
	}

}
