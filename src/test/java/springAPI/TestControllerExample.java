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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(ControllerExample.class)
public class TestControllerExample {

	static final String VERSION = "v1";
	
	@Autowired
    private MockMvc mockMvc;
	
    /**
     * Unit Test for REsT Api example
     * 
     * @throws Exception
     */
    @Test
    public void exampleTest() throws Exception {
    	
        MvcResult ra = this.mockMvc.perform(get("/test/unitTestExample")
        		.param("param", "Ireland")
        		.accept(MediaType.parseMediaType("application/"+VERSION+"+json;charset=UTF-8")))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/"+VERSION+"+json;charset=UTF-8"))
        		.andReturn();
        
        String expected = "[\"Berlin\",\"Germany\",\"Ireland\"]";
        String actual =ra.getResponse().getContentAsString();
       
        assertTrue("right results",expected.equals(actual));
    }

    /**
     * Test accept header based version
     * 
     * @throws Exception
     */
    @Test
    public void checkVersioning() throws Exception {
    	
        MvcResult ra = this.mockMvc.perform(get("/test/checkversion")
        		.accept(MediaType.parseMediaType("application/"+"v1"+"+json;charset=UTF-8")))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/"+"v1"+"+json;charset=UTF-8"))
        		.andReturn();
               
        String version1 = ra.getResponse().getContentAsString();
        
        ra = this.mockMvc.perform(get("/test/checkversion")
        		.accept(MediaType.parseMediaType("application/"+"v2"+"+json;charset=UTF-8")))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/"+"v2"+"+json;charset=UTF-8"))
        		.andReturn();
       
        String version2 = ra.getResponse().getContentAsString();
        
        assertEquals("Version 1.0", version1);
        assertEquals("Version 2.0", version2);
    }
}
