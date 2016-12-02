package springAPI;
	
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;

import rdfcf.SingletonStore;
import rdfcf.helper.DatakeyResponse;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(ControllerRDFCF.class)
public class TestControllerRDFCF {

	static final String VERSION = "v1";


	static SingletonStore store;
	
	@Autowired
	private MockMvc mockMvc;

	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
		//Init Storing Method
		store = SingletonStore.getInstance();
		//useMemory ?
		store.init(true);
		store.loadExample();
		
		//Get 1 datakey
	
	}
	
	@AfterClass
	public static void tearDownAfterClass() {
		store.cleanStore();
	}
	
	static final String query = "Construct { ?film  a <http://dbpedia.org/ontology/Film> ;"
			+ "<http://dbpedia.org/property/runtime>  ?runtime"
        	+ "} Where { ?film  a <http://dbpedia.org/ontology/Film> ;"
			+ "<http://dbpedia.org/property/runtime>  ?runtime "
			+ "}  Limit 10";
	
	/**
	 * Test Api for rdfcf query
	 * 
	 * @throws Exception
	 */
	@Test
	public void testQuery() throws Exception {
		MvcResult mr = this.mockMvc.perform(post("/rdfcf/query")
				.param("endpoint", "http://dbpedia.org/sparql")
				.param("limit", "10000")
				.param("query", query)
				.accept(MediaType.parseMediaType("application/"+VERSION+"+json;charset=UTF-8")))
				.andExpect(status().isOk())
				.andExpect(content().contentType("application/"+VERSION+"+json;charset=UTF-8"))
				.andReturn();
		
		ObjectMapper mapper = new ObjectMapper();
		String jsonInString = mr.getResponse().getContentAsString();
		
		DatakeyResponse dr = mapper.readValue(jsonInString, DatakeyResponse.class);
		
		assertNotNull("Got result", dr.getDatakey());
		assertFalse("Got Result", dr.getDatakey().isEmpty());
	}
	
	/*
	 * Test Api for rdfcf Anaylze
	 */
    @Test
    public void testAnalyze() throws Exception {
    	
		MvcResult mr = this.mockMvc.perform(get("/rdfcf/analyze/example")
				.accept(MediaType.parseMediaType("application/"+VERSION+"+json;charset=UTF-8")))
				.andExpect(status().isOk())
				.andExpect(content().contentType("application/"+VERSION+"+json;charset=UTF-8"))
				.andReturn();
		
		assertNotNull("Got result", mr.getResponse());
    }
    
//    @Test
//    public void testFilter() throws Exception {
//		
//    }
    
    @Test
    public void testShow() throws Exception {
    	
		MvcResult mr = this.mockMvc.perform(get("/rdfcf/show/example")
				.param("format", "TURTLE")
				.accept(MediaType.parseMediaType("application/"+VERSION+"+json;charset=UTF-8")))
				.andExpect(status().isOk())
				.andExpect(content().contentType("application/"+VERSION+"+json;charset=UTF-8"))
				.andReturn();
		
		assertNotNull("Got result", mr.getResponse());
		assertFalse("Got result", mr.getResponse().getContentAsString().isEmpty());
    }
    
}
