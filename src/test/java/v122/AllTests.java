package v122;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;


/**
 * Testing the rdfcf module
 * 
 * @author marvin
 *
 */
@RunWith(Suite.class)
@SuiteClasses({
	QueryManagerTest.class, RDFGetterTest.class})
public class AllTests {

	
}
