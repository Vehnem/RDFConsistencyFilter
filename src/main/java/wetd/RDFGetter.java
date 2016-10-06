package wetd;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.UUID;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.riot.WebContent;
import org.apache.jena.sparql.engine.http.QueryEngineHTTP;

/*
 * Query a construct query to get RDF data from the 
 * endpoint, without endpoint limit problems
 */

public class RDFGetter {

/*
 * 	Root to the RDF files
 */
	private static String rootLocation = "./RDF_DATA/";
	
/*
 * Location of the RDF Data (timestamped)
 */
	private static String dataId;
	private static String dataKey;
	
/*
 * Constructor 
 * 
 * @param endpoint
 * @param endpoint_limit
 * @param queryString
 */
	public RDFGetter(String endpoint, long endpointLimit, String queryString) {
		getRDF(queryString,rootLocation,endpoint,endpointLimit);
	}

/*
 * Store the RDF N_Triples file from the construct query without limit problems
 * 
 * @param queryString query
 * @param file_location location of the dataset
 */
	public static void getRDF(final String queryString, final String rootLocation, final String endpoint, final long endpointLimit) {
		
		// Declare
	
		Query query = QueryFactory.create(queryString);
		QueryEngineHTTP queryExec;
		Model model;
		FileOutputStream fileStream;
		OutputStreamWriter outputWriter;
		long packetsize = getPackagesize("") + endpointLimit/10 ; // TODO function
		
		//Analyse Limit
		long limit = 0;
		boolean noLimit = true;
		if(query.hasLimit()){
			noLimit = false;
			limit = query.getLimit();
			if(limit > packetsize) {
				query.setLimit(packetsize);
			}
		} else {
			query.setLimit(packetsize);
		}
		
		//Analyse offset
		long start_offset;
		if(query.hasOffset()){
			 start_offset = query.getOffset();
		}
		else {
			 start_offset = 0;
		}
		
		dataKey = genDataKey();
		
		//store all packages in one file
		try {
			new File(rootLocation+dataKey).mkdirs();
			fileStream = new FileOutputStream(new File(rootLocation+dataKey+"/dataset.nt"));
			outputWriter = new OutputStreamWriter(fileStream, "UTF-8");
	
			do {
				
				query.setOffset(start_offset);
				
				queryExec = QueryExecutionFactory.createServiceRequest(endpoint, query);
				queryExec.setModelContentType(WebContent.contentTypeJSONLD);
			
				model = queryExec.execConstruct();
				model.write(outputWriter, "N-Triples");
			
				start_offset += packetsize;
			
				if(limit > 0) {
					limit -= packetsize;
					if(limit < packetsize) {
					query.setLimit(limit);
					}
				}
			
			} while(!model.isEmpty() && (limit > 0 || noLimit));
			
			outputWriter.close();
			fileStream.close();
			model.close();
		}
		catch(IOException e) {System.out.println("ERROR at storing RDF");} //TODO better catch fex: del unused folder
	}

/*
 * get file location
 * 
 * @return 			data location
 */
	public String getDataId() {
		return this.dataId;
	}	

	public String getDataKey() {
		return this.dataKey;
	}	
	
	private static String genDataKey() {
		return UUID.randomUUID().toString();
	}
	
		
/*
* data location name with time stamp
*   
* @return timestamp 	data location
*/
	private static String genDataId() {
		long millis = System.currentTimeMillis();
		return String.valueOf(millis);
	}
	
/* Unused
 * Get the perfect packet size for the query 
 * 
 * @param 	queryString 	query input
 * @return					package size 
 */
	private static long getPackagesize(String queryString) {
		return 0;
	}
}
