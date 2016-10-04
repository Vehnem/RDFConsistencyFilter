package wetd;
//import static org.junit.Assert.*;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryFactory;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import wetd.QueryManager;

public class QueryManagerTest {
	
	/*
	
	q_film_runtime = "prefix dbo: <http://dbpedia.org/ontology/>"+
			  "prefix dbp: <http://dbpedia.org/property/>"+
			  "select * where { ?film a dbo:Film. ?film dbp:runtime ?runtime.} ";
	
	q_film_runtime = "prefix dbo: <http://dbpedia.org/ontology/>"+
			  "prefix dbp: <http://dbpedia.org/property/>"+
			  "select * where { ?film a dbo:Film. ?film dbp:runtime ?runtime.} ";

	cq_film_runtime ="prefix dbo: <http://dbpedia.org/ontology/>"+
			  "prefix dbp: <http://dbpedia.org/property/>"+
			  "construct {?film a dbo:Film. ?film dbp:runtime ?runtime.} "+				
			  "where {?film a dbo:Film. ?film dbp:runtime ?runtime.}";

	cq_person = "prefix dbo: <http://dbpedia.org/ontology/> "+
		 "prefix dbp: <http://dbpedia.org/property/> "+
		 "construct {?Person a dbo:Person.} "+
	   	 "where {?Person a dbo:Person.} Limit 200";

	cq_education = "construct {?s ?p <http://dbpedia.org/resource/Education> . }"+
		 "where { ?s ?p <http://dbpedia.org/resource/Education>} Offset 1000";

	cq_books_pages = "prefix dbo: <http://dbpedia.org/ontology/> "+
			  "prefix dbp: <http://dbpedia.org/property/> "+
			  "select * where { ?Book a dbo:Book. ?Book dbp:page ?page.} Limit 100"; 
			  
	*/

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
		
		String q_film_runtime = "prefix dbo: <http://dbpedia.org/ontology/>"+
				  "prefix dbp: <http://dbpedia.org/property/>"+
				  "select * where { ?film a dbo:Film. ?film dbp:runtime ?runtime.} Offset 10 Limit 20";
		
		String q_book_pages = " prefix dbo: <http://dbpedia.org/ontology/> prefix dbp:"
				+ " <http://dbpedia.org/property/> select * where {"
				+ " ?Book a dbo:Book. ?Book dbp:page ?page.} Limit 100 ";
		
		
		QueryManager qm = new QueryManager(q_film_runtime);
		
		System.out.println(qm.getQuery().toString());
	}

}
