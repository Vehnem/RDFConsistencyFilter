package springAPI;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(ControllerKGUtil.class)
public class TestControllerKGUtil {

	static final String VERSION = "v1";

	@Autowired
	private MockMvc mockMvc;

	/**
	 * Test Api for query Solr
	 * 
	 * @throws Exception
	 */
	@Test
	public void testQuerySolr() throws Exception {
		MvcResult ra = this.mockMvc
				.perform(get("/kgutil/queryExecutionSolr")
				.param("q", "nameDe:\"Deutschland\"")
				.param("fq", "")
				.accept(MediaType.parseMediaType("application/" + VERSION + "+json;charset=UTF-8")))
				.andExpect(status().isOk())
				.andExpect(content().contentType("application/" + VERSION + "+json;charset=UTF-8")).andReturn();
		
		assertNotNull("Got no results",ra.getResponse());
		assertFalse("Got no results", ra.getResponse().toString().isEmpty());
	}

	/**
	 * Test Api for getEntitiesFromText
	 * 
	 * @throws Exception
	 */
	@Test
	public void testEntitiesFromText() throws Exception {

		MvcResult ra = this.mockMvc
				.perform(get("/kgutil/getEntitiesFromText").param("text", "Berlin is a great city in Germany")
						.param("lang", "ENGLISH").param("ovlap", "ALL")
						.accept(MediaType.parseMediaType("application/" + VERSION + "+json;charset=UTF-8")))
				.andExpect(status().isOk())
				.andExpect(content().contentType("application/" + VERSION + "+json;charset=UTF-8")).andReturn();

		assertNotNull("Got no Result", ra.getResponse());
		assertFalse("Got no Result", ra.getResponse().getContentAsString().isEmpty());
	}

}
