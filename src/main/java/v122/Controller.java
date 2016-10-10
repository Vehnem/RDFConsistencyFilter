package v122;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
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
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;


/**
 * Rest-controller for RDFConsistencyFilter
 * 
 * TODO JSON or POJO Response
 * 
 * only encoded queries supported (because of <...> parts)   
 */
@RestController
@Api(value="rdf-cf", description="RDF consistency filter - RestController")
@RequestMapping(value = "/rdf-cf")
public class Controller {
	
//	private final String defaultSelQuery = "prefix dbo: <http://dbpedia.org/ontology/>"+
//										   "prefix dbp: <http://dbpedia.org/property/>"+
//										   "select * where { ?film a dbo:Film. ?film dbp:runtime ?runtime.} Limit 100"; 
	
	private final String defaultConQuery = "prefix dbo: <Chttp://dbpedia.org/ontology/> "+
			  							   "prefix dbp: <Chttp://dbpedia.org/property/> "+
			  							   "construct {?film a dbo:Film. ?film dbp:runtime ?runtime.} "+				
			  							   "where {?film a dbo:Film. ?film dbp:runtime ?runtime.} Limit 100";
    
    /**
     * Get RDF from a Sparql-endpoint over a CONSTRUCT Query
     * and save the result at the file-system
     * 
     * @param endpoint
     * @param limit
     * @param query
     * @return
     */
    @ApiOperation(value = "load", nickname="loadRDF")
    @ApiImplicitParams({
    	@ApiImplicitParam(name = "endpoint", value = "SPARQL Endpoint", required = true, dataType = "string", paramType = "query", defaultValue="http://dbpedia.org/sparql"),
    	@ApiImplicitParam(name = "limit", value = "Endpoint return limit", required = true, dataType = "string", paramType = "query", defaultValue="10000"),
    	@ApiImplicitParam(name = "query", value = "Construct Query", required = true, dataType = "string", paramType = "query", defaultValue=defaultConQuery)
    })
    @RequestMapping(value="/load", method=RequestMethod.POST)
    public @ResponseBody String loadRDF_query(@RequestParam(value="endpoint",required=true, defaultValue="")String endpoint,
    										 @RequestParam(value="limit",required=true, defaultValue="")long limit,
    										 @RequestParam(value="query",required=true, defaultValue="")String query) {
    	try{
    		RDFGetter rg = new RDFGetter(endpoint, limit, query);
    		return "{ \"sessiondatakey\" : \""+rg.getDataKey()+"\" }";
    	}
    	catch(Exception e) {
    		return "{ \"sessiondatakey\" : \"error\" }";	
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
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbdatakeyden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")}) 
    @RequestMapping(value="/show/{datakey}", method=RequestMethod.GET, produces={"text/text;charset=UTF-8"})
    public @ResponseBody String showRDF(@PathVariable String datakey,
    									@RequestParam(value="format", defaultValue="TURTLE") String format) {
    	Model model = ModelFactory.createDefaultModel();
		
    	switch (format) {
    		case "TURTLE":  
    	    	try( final ByteArrayOutputStream os = new ByteArrayOutputStream() ){
    	    		model.read("./RDF_DATA/"+datakey+"/result_dataset.nt", "N-TRIPLES");
    	    		model.write(os, "TURTLE");
    	    		return os.toString();
    	    	} catch (IOException e) {
    				e.printStackTrace();
    				return null;
    			}
    		case "N-TRIPLES":
    	    	try( final ByteArrayOutputStream os = new ByteArrayOutputStream() ){
    	    		model.read("./RDF_DATA/"+datakey+"/result_dataset.nt", "N-TRIPLES");
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
    	RDFAnalyze ra = new RDFAnalyze(ModelFactory.createDefaultModel().read("./RDF_DATA/"+datakey+"/result_dataset.nt", "N-TRIPLES"));
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
        @ApiImplicitParam(name = "datakey", value = "Key", required = true, dataType = "string", paramType = "path", defaultValue="film_runtime_100"),
        @ApiImplicitParam(name = "property", value = "Key", required = true, dataType = "string", paramType = "query", defaultValue="http://dbpedia.org/property/runtime"),
        @ApiImplicitParam(name = "datatype", value = "Key", required = true, dataType = "string", paramType = "query", defaultValue="http://www.w3.org/2001/XMLSchema#integer"),
        @ApiImplicitParam(name = "query", value = "Key", required = true, dataType = "string", paramType = "query", defaultValue=defaultConQuery),
        @ApiImplicitParam(name = "dublicate_filter", value = "Key", required = true, dataType = "string", paramType = "query", defaultValue="1"),
      })
    @RequestMapping(value="/filter/", method=RequestMethod.POST , produces={"application/json"})
    public @ResponseBody String filterRDF(@RequestParam( value = "datakey", defaultValue="film_runtime_100")String datakey, 
    										  @RequestParam( value = "property", defaultValue="http://dbpedia.org/property/runtime")String property,
    										  @RequestParam( value = "datatypes", defaultValue="http://www.w3.org/2001/XMLSchema#integer")String datatypes,      	
    										  @RequestParam( value = "remove_duplicates", defaultValue="0")boolean remove_duplicates,
    										  @RequestParam( value = "consistent", defaultValue="true")boolean consistent) {
       	
    	RDFFilter rf = new RDFFilter(ModelFactory.createDefaultModel().read("./RDF_DATA/"+datakey+"/result_dataset.nt", "N-TRIPLES"));
    	
    	
    	List<String> datatypelist = Arrays.asList(datatypes.split("\\s*,\\s*"));
    	Model model = rf.new_filter( property , datatypelist , remove_duplicates , consistent);
    	
    	try( final ByteArrayOutputStream os = new ByteArrayOutputStream() ){
    		model.write(os, "N-TRIPLES");
    		return os.toString();
    	} catch (IOException e) {
			e.printStackTrace();
			return null;
    	}
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
    public void deleteDataset(@PathVariable(value="datakey") String datakey) {
    	File folder = new File("./RDF_DATA/"+datakey);
    	new RDFStore().deleteFolder(folder);    	
    }
}