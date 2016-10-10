package v122;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Query a construct query to get RDF data from the 
 * endpoint, without endpoint limit problems
 */

public class RDFGetter {
	
	/**
	 * Logger
	 */
	private static final Logger log = LoggerFactory.getLogger(Scheduler.class);

	/**
	 * 	Root folder of the RDF files
	 */
	private static final String rootLocation = "./RDF_DATA/";
	
	/**
	 * RDF-data-key points on a folder
	 */
	private static String dataKey;
	
	/**
	 * Constructor 
	 * 
	 * @param endpoint
	 * @param endpoint_limit
	 * @param queryString
	 */
	public RDFGetter(String endpoint, long endpointLimit, String queryString) {
		getRDF(queryString,rootLocation,endpoint,endpointLimit);
	}
	
	/**
	 * Get data-key
	 * 
	 * @return 			data location
	 */
	public String getDataKey() {
		return dataKey;
	}	
	
	/**
	 * Data-key of the RDF-file location
	 * 
	 * @return Unique data-key
	 */
	private static String genDataKey() {
		return UUID.randomUUID().toString();
	}
	
		
	/** UNUSED
	 * 
	 * Package-Size
	 * 
	 * @param queryString
	 * @return
	 */
	private static long getPackagesize(String queryString) {
		return 0;
	}
	
	/**
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
		catch ( IOException e ) {
			
			log.info("failed to get RDF-data");
			
			new File(rootLocation+dataKey).delete();
		} 
	}
}
