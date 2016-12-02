package rdfcf;

import static org.junit.Assert.*;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryFactory;
import org.junit.Test;

import junit.framework.Assert;
import rdfcf.QueryManager;

/**
 * Creates a CONSTRUCT QUERY from a SELECT QUERY
 * 
 * @author marvin
 *
 */
@SuppressWarnings("deprecation")
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

	@Test
	public void test() {
		
		String q_film_runtime = "prefix dbo: <http://dbpedia.org/ontology/>"+
				  "prefix dbp: <http://dbpedia.org/property/>"+
				  "select * where { ?film a dbo:Film. ?film dbp:runtime ?runtime.} Offset 10 Limit 20";
		
		String q_book_pages = " prefix dbo: <http://dbpedia.org/ontology/> prefix dbp:"
				+ " <http://dbpedia.org/property/> select * where {"
				+ " ?Book a dbo:Book. ?Book dbp:page ?page.} Limit 100 ";
		
		
		String cq_film_runtime_org ="prefix dbo: <http://dbpedia.org/ontology/>"+
				  "prefix dbp: <http://dbpedia.org/property/>"+
				  "construct {?film a dbo:Film. ?film dbp:runtime ?runtime.} "+				
				  "where {?film a dbo:Film. ?film dbp:runtime ?runtime.}";
		
		String cq_film_runtime = "CONSTRUCT { "+
								 "?film <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://dbpedia.org/ontology/Film> . "+
								 "?film <http://dbpedia.org/property/runtime> ?runtime . "+
								 "} "+
								 "WHERE "+
								 "{ ?film  a                     <http://dbpedia.org/ontology/Film> ; "+
								 "<http://dbpedia.org/property/runtime>  ?runtime "+
								 "} "+
								 "OFFSET  10 "+
								 "LIMIT   20";
		
		QueryManager qm = new QueryManager(q_film_runtime);
		
		Query q = QueryFactory.create(cq_film_runtime_org);
		
		System.out.println(q.toString());
			
		assertEquals(QueryFactory.create(cq_film_runtime),QueryFactory.create(qm.getQuery()));
	}

}
