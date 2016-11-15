package v122;

import java.io.File;

import org.apache.http.util.Asserts;
import org.apache.jena.rdf.model.Model;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import junit.framework.Assert;
import v122.RDFGetter;

public class RDFGetterTest {

	static String endpoint = "http://dbpedia.org/sparql";
	
	static long limit = 10000;
	
	static String test = "Construct { ?film  a <http://dbpedia.org/ontology/Film> ;"
			+ "<http://dbpedia.org/property/runtime>  ?runtime"
        	+ "} Where { ?film  a <http://dbpedia.org/ontology/Film> ;"
			+ "<http://dbpedia.org/property/runtime>  ?runtime "
			+ "}  Limit 10";
		
	static String test2 = "prefix dbo: <http://dbpedia.org/ontology/> "+
				"prefix dbp: <http://dbpedia.org/property/> "+
				"Construct { ?film a dbo:Film. ?film dbp:released ?released. ?film dbp:runtime ?runtime. } "+
				"Where { ?film a dbo:Film. ?film dbp:released ?released. ?film dbp:runtime ?runtime. } Limit 100 ";
		
	static String test3 = "prefix dbo: <http://dbpedia.org/ontology/> "+
				"prefix dbp: <http://dbpedia.org/property/> "+
				"construct {?film a dbo:Film. ?film dbp: ?released.} "+	
				"where {?film a dbo:Film. ?film dbp:released ?released.} Limit 100";
		
	static String test4 = "prefix dbo: <http://dbpedia.org/ontology/> "+
			"prefix dbp: <http://dbpedia.org/property/> "+
			"Construct {?film a dbo:Film. ?film dbp:gross ?gross.} "+		
			"Where {?film a dbo:Film. ?film dbp:gross ?gross.} Limit 100";
	
	static Model result;
	
	@Test
	public void test() {
		
		/*
		 * http://dbpedia.org/sparql
		 * http://live.dbpedia.org/sparql
		 * http://enipedia.tudelft.nl/sparql
		 * http://rdf.farmbio.uu.se/chembl/sparql
		 */
		
		RDFGetter rg = new RDFGetter();
		
		result = rg.getRDF(test, endpoint, limit);
		
		result.write(System.out, "N-TRIPLES");
		
		//TODO check with file 
		
	}
}
