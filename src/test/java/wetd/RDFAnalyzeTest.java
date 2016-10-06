package wetd;
//import static org.junit.Assert.*;

import org.apache.jena.rdf.model.ModelFactory;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import wetd.RDFAnalyze;

public class RDFAnalyzeTest {

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
		
		
		RDFAnalyze ra = new RDFAnalyze(ModelFactory.createDefaultModel().read("./RDF_EXAMPLES/film_released_100/dataset.nt", "N-TRIPLES"));
		
		String property = ra.possibleProperties().get(0); //runtime
		
		System.out.println("for property "+property+"\n");
		
		String[][] datatypes = ra.countDatatype(property);
		
		for(int i = 0; i < datatypes.length; i++) {
			System.out.println(datatypes[i][1]+"\t"+datatypes[i][0]);
		}
		
	}

}
