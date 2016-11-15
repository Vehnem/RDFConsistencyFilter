package v122;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;

import javax.annotation.security.RunAs;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.junit.Test;
import org.junit.runners.Parameterized.Parameters;

import v122.RDFAnalyze;

public class RDFAnalyzeTest {

	@Test
	public void test() {
		
		Model m = ModelFactory.createDefaultModel().read("./RDF_EXAMPLES/example/dataset.nt");
		
		RDFAnalyze ra = new RDFAnalyze(m);

		ArrayList<String> expected_prop = new ArrayList<String>();
		
		expected_prop.add("pb");
		expected_prop.add("pa");
		
		ArrayList<String> actual_prop = ra.possibleProperties();
		
		
		String[][] expected_counter = {{"db"},{"1"}};
		
		String[][] actual_counter = new String[actual_prop.size()][2];
		
		for(int i = 0; i < actual_prop.size(); i++) {
			
			assertEquals(expected_prop.get(i), actual_prop.get(i));
	
		}
	}
}
