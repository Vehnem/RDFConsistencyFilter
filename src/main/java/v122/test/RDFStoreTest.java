package v122.test;

import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import v122.RDFStore;

public class RDFStoreTest {

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
		
		
		String datatypes = "a,b,c,d";
		List<String> datatypelist = Arrays.asList(datatypes.split("\\s*,\\s*"));
		for(int i = 0 ; i < datatypelist.size() ; i++ ) {
			System.out.println(datatypelist.get(i));
		}
		System.out.println("done");
	}

}
