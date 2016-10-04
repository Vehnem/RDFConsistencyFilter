package wetd;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.jena.atlas.json.JSON;
import org.apache.jena.rdf.model.Model;
import org.springframework.beans.factory.annotation.Autowired;
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


/* TODO better names for call and function
 * Spring RestController
 * 
 * only encoded queries supported (because of <...> parts)   
 */
@RestController
@Api(value="Rest Controller", description="Default Controller")
@RequestMapping(value = "/Rest")
public class Controller {
	
	private final String defaultSelQuery = "prefix dbo: <http://dbpedia.org/ontology/>"+
										   "prefix dbp: <http://dbpedia.org/property/>"+
										   "select * where { ?film a dbo:Film. ?film dbp:runtime ?runtime.} Limit 100"; 
	
	private final String defaultConQuery = "prefix dbo: <Chttp://dbpedia.org/ontology/> "+
			  							   "prefix dbp: <Chttp://dbpedia.org/property/> "+
			  							   "construct {?film a dbo:Film. ?film dbp:runtime ?runtime.} "+				
			  							   "where {?film a dbo:Film. ?film dbp:runtime ?runtime.} Limit 100";


	
	/**
    @RequestMapping(value="/buildquery", method=RequestMethod.POST)
    public @ResponseBody String buildconquery(@RequestParam(value="query", defaultValue=defaultSelQuery) String query) {
   
		QueryManager qm = new QueryManager(query);
		
    	return qm.buildConQuery();
    	
    }**/
    
    /* 
     * Executes a construct and saves the RDF
     * 
     * 
     */
    @ApiOperation(value = "execQuery")
    @ApiImplicitParams({
    	@ApiImplicitParam(name = "endpoint", value = "SPARQL Endpoint", required = true, dataType = "string", paramType = "query", defaultValue="http://dbpedia.org/sparql"),
    	@ApiImplicitParam(name = "limit", value = "Endpoint return limit", required = true, dataType = "string", paramType = "query", defaultValue="10000"),
    	@ApiImplicitParam(name = "query", value = "Construct Query", required = true, dataType = "string", paramType = "query", defaultValue=defaultConQuery)
    })
    @RequestMapping(value="/execquery", method=RequestMethod.POST)
    public @ResponseBody String execConQuery(@RequestParam(value="endpoint",required=true, defaultValue="")String endpoint,
    										 @RequestParam(value="limit",required=true, defaultValue="")long limit,
    										 @RequestParam(value="query",required=true, defaultValue="")String query) {
    	try{
    		RDFGetter rg = new RDFGetter(endpoint, limit, query);
    		return "{ \"sessionid\" : \""+rg.getDataId()+"\" }";
    	}
    	catch(Exception e) {
    		return "{ \"sessionid\" : \"error\" }";	
    	}
    }
    /* TODO return result_dataset
     * 
     * Get RDF data 
     * 
	 * N-Triples,RDF/XML,JSON-LD,RDF/JSON,TriG,NQuads,TriX,RDF Thrift
     */
    @ApiOperation(value = "getRDF", nickname = "getRDFResult")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "Key", required = true, dataType = "string", paramType = "path", defaultValue="film_runtime_100"),
        @ApiImplicitParam(name = "format", value = "Format", required = false, dataType = "string", paramType = "query", defaultValue="TURTLE")
      })
    @ApiResponses(value = { 
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")}) 
    @RequestMapping(value="/dataset/{id}", method=RequestMethod.GET, produces={"text/text;charset=UTF-8"})
    public @ResponseBody String getResultRDF(@PathVariable String id,
    										@RequestParam(value="format", defaultValue="TURTLE") String format) {
    	Model model;
    	
    	switch (format) {
    		case "TURTLE":  
    	    	try( final ByteArrayOutputStream os = new ByteArrayOutputStream() ){
    	    		model = new RDFView("./RDF_DATA/"+id).getNT("result_dataset.nt");
    	    		model.write(os, "TURTLE");
    	    		return os.toString();
    	    	} catch (IOException e) {
    				e.printStackTrace();
    				return null;
    			}
    		case "N-TRIPLES":
    	    	try( final ByteArrayOutputStream os = new ByteArrayOutputStream() ){
    	    		model = new RDFView("./RDF_DATA/"+id).getNT("result_dataset.nt");
    	    		model.write(os, "N-TRIPLES");
    	    		return os.toString();
    	    	} catch (IOException e) {
    				e.printStackTrace();
    				return null;
    			}
    		default: return "Unknown Format";
    	}
    }
    
    /* TODO Analyze all
     * Analyze RDF
     * 
     */
    @ApiOperation(value = "analyze", nickname = "analyzeRDF")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "Key", required = true, dataType = "string", paramType = "path", defaultValue="film_runtime_100"),
      })
    @RequestMapping(value="/analyze/{id}", method=RequestMethod.GET, produces={"application/json"})
    public @ResponseBody String analyzeRDF(@PathVariable(value="id")String id) {
    	
    	
    	String response = "{ ";
    	RDFAnalyze ra = new RDFAnalyze("./RDF_DATA/"+id);
    	ArrayList<String> properties = ra.possibleProperties();
    	
    	for(int i = 0; i < properties.size(); i++) {
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
    
    /* TODO better remove_without_p | More Tests | Better return
     * Filter existing data
     *
    @ApiOperation(value = "filter", nickname = "filterRDF")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "Key", required = true, dataType = "string", paramType = "query", defaultValue="film_runtime_100"),
        @ApiImplicitParam(name = "property", value = "Key", required = true, dataType = "string", paramType = "query", defaultValue="http://dbpedia.org/property/runtime"),
        @ApiImplicitParam(name = "datatype", value = "Key", required = true, dataType = "string", paramType = "query", defaultValue="http://www.w3.org/2001/XMLSchema#integer"),
        @ApiImplicitParam(name = "query", value = "Key", required = true, dataType = "string", paramType = "query", defaultValue=defaultConQuery),
        @ApiImplicitParam(name = "dublicate_filter", value = "Key", required = true, dataType = "string", paramType = "query", defaultValue="1"),
      })
    @RequestMapping(value="/filter", method=RequestMethod.POST , produces={"application/json"})
    public @ResponseBody String filterDataset(@RequestParam(value="id", defaultValue="film_runtime_100")String id, 
    											@RequestParam(value="property", defaultValue="http://dbpedia.org/property/runtime")String p,
    											@RequestParam(value="query", defaultValue=defaultConQuery)String query,
    											@RequestParam(value="duplicate_filter", defaultValue="0")int duplicate_filter,
    											@RequestParam(value="datatype", defaultValue="http://www.w3.org/2001/XMLSchema#integer")String o) {
    	try{
    		RDFFilter rf = new RDFFilter("./RDF_DATA/"+id);
    		rf.useFilter_p_o(p, o);
    		rf.remove_without_p(query);
    		if(duplicate_filter == 1) {
    			rf.remove_dublicates(p, "result_dataset.nt");
    		}
    		return "{ \"answer\" : \"true\" }";
    	}
    	catch(Exception ee) {
    		return "{ \"answer\" : \"false\" }";
    	}
    }
    
    @RequestMapping(value="/deldataset", method=RequestMethod.DELETE )
    public void deleteDataset(@RequestParam(value="id") String id) {
    	
    }*/
    
    
/**TEST**************************************************************************************************************
    
    @RequestMapping(value="/test", method=RequestMethod.GET, produces={"application/json"})
    public @ResponseBody String testrest(@RequestParam(value="param1", defaultValue="param1")String param1,
    									 @RequestParam(value="param2", defaultValue="param2")String param2,
    									 HttpServletResponse  response) {
    	
    	
    	
    	return "{\"name\":\"unknown\", \"age\":-1 , \"field\" : {\"1\" : \"a\" , \"2\" : \"b\"}}";
    	
    }

    @RequestMapping(value="/{time}", method = RequestMethod.GET)
    public @ResponseBody MyData getMyData(
            @PathVariable long time) {

        return new MyData(time, "REST GET Call !!!");
    }

    @RequestMapping(method = RequestMethod.PUT)
    public @ResponseBody MyData putMyData(
            @RequestBody MyData md) {

        return md;
    }

    @RequestMapping(method = RequestMethod.POST)
    public @ResponseBody MyData postMyData() {

    	return new MyData(System.currentTimeMillis(),
            "REST POST Call !!!");
    }

    @RequestMapping(value="/{time}", method = RequestMethod.DELETE)
    public @ResponseBody MyData deleteMyData(
            @PathVariable long time) {

        return new MyData(time, "REST DELETE Call !!!");
    }*/
}