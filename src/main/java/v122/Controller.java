package v122;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.jena.rdf.model.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponses;


/**
 * Rest-controller for RDFConsistencyFilter
 * 
 * TODO JSON or POJO Response
 * 
 * only encoded queries supported (because of <...> parts)   
 */
@RestController
@Api(value="rdfcf-v1", description="RDF consistency filter - RestController")
@RequestMapping(value = "/rdfcf/v1")
public class Controller {
	
//	private final String defaultSelQuery = "prefix dbo: <http://dbpedia.org/ontology/>"+
//										   "prefix dbp: <http://dbpedia.org/property/>"+
//										   "select * where { ?film a dbo:Film. ?film dbp:runtime ?runtime.} Limit 100"; 
	
	private static final Logger log = LoggerFactory.getLogger(Scheduler.class);
	
	private final String defaultConQuery = "empty";
    
    /**
     * Get RDF from a Sparql-endpoint over a CONSTRUCT Query
     * and save the result at the file-system
     * 
     * @param endpoint
     * @param limit
     * @param query
     * @return
     */
    @ApiOperation(value = "query", nickname="queryRDF")
    @ApiImplicitParams({
    	@ApiImplicitParam(name = "endpoint", value = "SPARQL Endpoint", required = true, dataType = "string", paramType = "query", defaultValue="http://dbpedia.org/sparql"),
    	@ApiImplicitParam(name = "limit", value = "Endpoint return limit", required = true, dataType = "string", paramType = "query", defaultValue="10000"),
    	@ApiImplicitParam(name = "query", value = "Construct Query", required = true, dataType = "string", paramType = "query", defaultValue=defaultConQuery)
    })
    @RequestMapping(value="/query", method=RequestMethod.POST)
    public @ResponseBody String queryRDF(@RequestParam(value="endpoint",required=true, defaultValue="")String endpoint,
    										 @RequestParam(value="limit",required=true, defaultValue="")long limit,
    										 @RequestParam(value="query",required=true, defaultValue="")String query) {
    	try{
    		Model model = new RDFGetter().getRDF(query, endpoint, limit);
    		
    		SingletonStore store = SingletonStore.getInstance();
    		
    		String datakey = store.addRDFData(model);
    		log.info("loaded");
    		return "{ \"datakey\" : \""+datakey+"\" }";
    	}
    	catch(Exception e) {
    		return "{ \"datakey\" : \"\" }";
    	}
    }
    
//    /**
//     * Import existing RDF-data-file
//     * 
//     * @return
//     */
//    @ApiOperation( value = "import" , nickname = "importRDF" )
//    @RequestMapping(value="import", method=RequestMethod.POST)
//    public @ResponseBody String importRDF_upload() {
//    	return "UNUSED";
//    }
    
    /**
     * Returns the result data-set
     * 
     * @param datakey
     * @param format
     * @return
     */
    @ApiOperation(value = "show", nickname = "showRDF")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "datakey", value = "Key", required = true, dataType = "string", paramType = "path", defaultValue="film_runtime_100"),
        @ApiImplicitParam(name = "format", value = "Format", required = false, dataType = "string", paramType = "query", defaultValue="TURTLE")
      })
    @ApiResponses(value = { 
//            @ApiResponse(code = 200, message = "Success"),
//            @ApiResponse(code = 401, message = "Unauthorized"),
//            @ApiResponse(code = 403, message = "Forbdatakeyden"),
//            @ApiResponse(code = 404, message = "Not Found"),
//            @ApiResponse(code = 500, message = "Failure")
            }) 
    @RequestMapping(value="/show/{datakey}", method=RequestMethod.GET, produces={"text/text;charset=UTF-8"})
    public @ResponseBody String showRDF(@PathVariable String datakey,
    									@RequestParam(value="format", defaultValue="TURTLE") String format) {
    	
    	SingletonStore store = SingletonStore.getInstance();
    	
    	Model model = store.getRDFDataResult(datakey);
    	
    	
    	switch (format) {
    		case "TURTLE":  
    	    	try( final ByteArrayOutputStream os = new ByteArrayOutputStream() ){
    	    		model.write(os, "TURTLE");
    	    		return os.toString();
    	    	} catch (IOException e) {
    				e.printStackTrace();
    				return null;
    			}
    		case "N-TRIPLES":
    	    	try( final ByteArrayOutputStream os = new ByteArrayOutputStream() ){
    	    		model.write(os, "N-TRIPLES");
    	    		return os.toString();
    	    	} catch (IOException e) {
    				e.printStackTrace();
    				return null;
    			}
    		default: return "Unknown Format";
    	}
    }
    
    /**
     * 
     * Analyze a RDF data-set
     * 
     * @param datakey
     * @return
     */
    @ApiOperation(value = "analyze", nickname = "analyzeRDF")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "datakey", value = "Key", required = true, dataType = "string", paramType = "path", defaultValue="film_runtime_100"),
      })
    @RequestMapping(value="/analyze/{datakey}", method=RequestMethod.GET, produces={"application/json"})
	public @ResponseBody String analyzeRDF(@PathVariable(value="datakey")String datakey) {
    	
    	String response = "{ ";
    	SingletonStore store = SingletonStore.getInstance();
    	RDFAnalyze ra = new RDFAnalyze(store.getRDFData(datakey));
    	
    	ArrayList<String> properties = ra.possibleProperties();
    	
    	for ( int i = 0 ; i < properties.size(); i++ ) {
    		String[][] datatypes = ra.countDatatype(properties.get(i));
    		
    		response += " \"properties\" : \""+properties.size()+"\""+
    					" , \""+i+"\" : { "+
    					" \"property\" : \""+properties.get(i)+"\" ,"+
    					" \"numberofdatatypes\" : \""+datatypes.length+"\" ,"+
    					" \"datatypes\" : {";
    		
    		for(int k = 0; k < datatypes.length; k++) {
    			
    			response += " \""+k+"\" : {"+
    						" \"name\" : \""+datatypes[k][0]+"\" ,"+
    						" \"value\" : \""+datatypes[k][1]+"\" }";
    			if((k+1) != datatypes.length) {
    				response += " ,";
    			} else {
    				response += " }";
    			}
    		}
    		
    		if((i+1) != properties.size()) {
    			response += " } ,";
    		} else {
    			response += " } }";
    		}
    	}
    	
    	return response;
    }
    
    /**
     * Filter the RDF-data
     * 
     * @param datakey
     * @param property
     * @param datatypes
     * @param remove_duplicates
     * @param consistent
     * @return
     */
    @ApiOperation(value = "filter", nickname = "filterRDF")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "datakey", value ="Key", required = true, dataType = "string", paramType = "path", defaultValue="film_runtime_100"),
        @ApiImplicitParam(name = "property",  value ="", required = true, dataType = "string", paramType = "query", defaultValue="http://dbpedia.org/property/runtime"),
        @ApiImplicitParam(name = "datatypes",  value ="Datatypes comma seperated list", required = true, dataType = "string", paramType = "query", defaultValue="http://www.w3.org/2001/XMLSchema#integer"),
        @ApiImplicitParam(name = "remove_duplicates",value ="", required = false, dataType = "boolean", paramType = "query", defaultValue="true"),
        @ApiImplicitParam(name = "consistent", value ="", required = false, dataType = "boolean", paramType = "query", defaultValue="true"),
        @ApiImplicitParam(name = "rdfunit_params", value ="", required = false, dataType = "string", paramType = "query", defaultValue="skip")
        
    })
    @RequestMapping(value="/filter/{datakey}", method=RequestMethod.POST , produces={"application/json"})
    public @ResponseBody String filterRDF(@PathVariable String datakey, 
    										  @RequestParam( value = "property", defaultValue="http://dbpedia.org/property/runtime")String[] properties,
    										  @RequestParam( value = "datatyp", defaultValue="http://www.w3.org/2001/XMLSchema#integer")String[] datatypes,      	
    										  @RequestParam( value = "remove_duplicates", defaultValue="true")boolean remove_duplicates,
    										  @RequestParam( value = "consistent", defaultValue="true")boolean consistent,
    										  @RequestParam( value = "rdfunit_params", defaultValue="")String rdfunit_params ) {
    	
    	SingletonStore store = SingletonStore.getInstance();
    	RDFFilter rf = new RDFFilter(store.getRDFData(datakey));
    	List<List<String>> datatypelist = new ArrayList<List<String>>();
    	for(String d : datatypes) {
    		datatypelist.add(Arrays.asList(d.split(",")));
    		log.info(Arrays.toString(d.split(",")));
    	}
    	List<String> propertylist = Arrays.asList(properties);
    	
    	log.info(Arrays.toString(properties));
    	
    	try {
    		String rdfunit_msg ="skipped";
        	Model model = rf.new_filter2( propertylist , datatypelist , remove_duplicates , consistent);
        
        	store.addRDFDataResult(datakey, model);
    		
//    		//RDFUnit TODO
//    		if( false == rdfunit_params.equals("skip")) {
//    			OnRDFUnit rdfu = new OnRDFUnit();
//    			String params = "-d ../RDFConsistencyFilter/RDF_EXAMPLES/film_runtime_100/result_dataset.nt";
//    			rdfunit_msg = rdfu.runRDFUnit_cmdline(params);
//    		}
        	return "{ \"message\" : \"filtered\" , \"rdfunit\" : \""+rdfunit_msg +"\"}";
    	} catch (Exception e) {
    		return "{ \"message\" : \"failed\" }";
    	}

    	
//    	try( final ByteArrayOutputStream os = new ByteArrayOutputStream() ){
//    		model.write(os, "N-TRIPLES");
//    		return os.toString();
//    	} catch (IOException e) {
//			e.printStackTrace();
//			return null;
//    	}
    }
    
    /**
     * deletes a data-set
     * 
     * @param datakey
     */
    @ApiOperation(value = "delete", nickname = "deleteRDF")
    @ApiImplicitParams({
    	@ApiImplicitParam(name = "datakey", value = "Key", required = true, dataType = "string", paramType = "path", defaultValue=""),
    })
    @RequestMapping(value="/delete/{datakey}", method=RequestMethod.DELETE )
    public String deleteDataset(@PathVariable(value="datakey") String datakey) {
    	
    	SingletonStore store = SingletonStore.getInstance();
    	
    	try
    	{
    		store.deleteRDFData(datakey);
        	return "{ \"message\" : \"deleted\" }";
    	} 
    	catch(Exception e) 
    	{
    		return "{ \"message\" : \"failed\" }";
    	}
    	
    }
    
    /**
     * Multiple Value Test
     */
    @RequestMapping(value="/test" , method=RequestMethod.GET)
    public String method(@RequestParam(value="filter") String[] filter){
           	
    	ArrayList<String> p = new ArrayList<String>();
    	ArrayList<String> o = new ArrayList<String>();
    	
    	for(String f : filter) {
    		p.add(f.split(":")[0]);
    		o.add(f.split(":")[1]);
    	}
    	
    	return p.toString()+"<br>"+o.toString();
    }
}