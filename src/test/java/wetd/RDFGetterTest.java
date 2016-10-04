package wetd;
//import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import wetd.RDFGetter;

public class RDFGetterTest {

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
		
		/*
		 * http://dbpedia.org/sparql
		 * http://live.dbpedia.org/sparql
		 * http://enipedia.tudelft.nl/sparql
		 * http://rdf.farmbio.uu.se/chembl/sparql
		 */
		
		String test = "Construct { ?film  a <http://dbpedia.org/ontology/Film> ;"
			+ "<http://dbpedia.org/property/runtime>  ?runtime"
        	+ "} Where { ?film  a <http://dbpedia.org/ontology/Film> ;"
			+ "<http://dbpedia.org/property/runtime>  ?runtime "
			+ "}  Limit 10000";
		
		String test2 = "prefix dbo: <http://dbpedia.org/ontology/> "+
				"prefix dbp: <http://dbpedia.org/property/> "+
				"Construct { ?film a dbo:Film. ?film dbp:released ?released. ?film dbp:runtime ?runtime. } "+
				"Where { ?film a dbo:Film. ?film dbp:released ?released. ?film dbp:runtime ?runtime. } Limit 100 ";
		
		String test3 = "prefix dbo: <http://dbpedia.org/ontology/> "+
				"prefix dbp: <http://dbpedia.org/property/> "+
				"construct {?film a dbo:Film. ?film dbp: ?released.} "+	
				"where {?film a dbo:Film. ?film dbp:released ?released.} Limit 100";
		
		String test4 = "prefix dbo: <http://dbpedia.org/ontology/> "+
			"prefix dbp: <http://dbpedia.org/property/> "+
			"Construct {?film a dbo:Film. ?film dbp:gross ?gross.} "+		
			"Where {?film a dbo:Film. ?film dbp:gross ?gross.} Limit 100";
		
		/*String cq_film_runtime =
				  "prefix dbo: <http://dbpedia.org/ontology/>"+
				  "prefix dbp: <http://dbpedia.org/property/>"+
				  "construct {?film a dbo:Film. ?film dbp:runtime ?runtime.} "+				
				  "where {?film a dbo:Film. ?film dbp:runtime ?runtime.} Limit 100";*/
		
		RDFGetter rg = new RDFGetter("http://live.dbpedia.org/sparql",10000,test4);
		
		System.out.println(rg.getDataId());
	}
}
