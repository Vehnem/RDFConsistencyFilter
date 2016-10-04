package wetd;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import wetd.RDFView;

public class RDFViewTest {

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
		RDFView rv = new RDFView("./RDF_DATA/film_runtime_released_full");
    	
		rv.getNT("dataset.nt").write(System.out, "N-TRIPLES");
	}

}
