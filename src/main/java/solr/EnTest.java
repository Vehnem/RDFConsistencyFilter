package solr;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.jena.base.Sys;
import org.junit.Test;

import aksw.org.kg.KgException;
import aksw.org.sdw.kg.handler.solr.KgSolrResultDocument;
import aksw.org.sdw.kg.handler.solr.SolrHandler;
import aksw.org.sdw.kg.handler.solr.SolrHandler.TAGGER_LANGUAGE;

public class EnTest {
	
	static String solrUrl = "http://localhost:8983/solr/companies";

	
	@Test 
	public void test0() {
		System.out.println(TAGGER_LANGUAGE.ENGLISH);
		
		TAGGER_LANGUAGE lang;
		
		lang = TAGGER_LANGUAGE.ENGLISH;
		
		System.out.println(lang);
	}
	
	//@Test
	public void test() throws KgException, IOException {
		
		
		SolrHandler solrHandler = new SolrHandler(solrUrl);
		
		List<KgSolrResultDocument> results = null;
		
		String[] fq = null;
		
		//Filter?
		List<String> fq_list = new ArrayList<String>();
		if(fq == null) {
			fq_list = null;
		} else {
			fq_list = Arrays.asList(fq);
		}

		results = solrHandler.executeQuery("nameEn:\"Germany\"", fq_list);
		
		solrHandler.close();
		
		List<String> out = new ArrayList<String>();
		
		int i = 0;
		
		for(i = 10; i < 20 ; i++) {
			
			KgSolrResultDocument r = results.get(i);
			
			List<String> l = r.getFieldValueAsStringList("nameEn");
			
			System.out.println(Arrays.asList(l));
			
			for(String k : l) {
				System.out.print(k);
			}
			
			System.out.print("\n");
		}
		
	}

}
