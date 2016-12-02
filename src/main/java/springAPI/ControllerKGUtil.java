package springAPI;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import aksw.org.kg.KgException;
import aksw.org.sdw.kg.handler.solr.EntityHandler;
import aksw.org.sdw.kg.handler.solr.KgSolrResultDocument;
import aksw.org.sdw.kg.handler.solr.SolrHandler;
import aksw.org.sdw.kg.handler.solr.SolrHandler.AnnotationInfo;
import aksw.org.sdw.kg.handler.solr.SolrHandler.TAGGER_ANNOTATION_OVERLAP;
import aksw.org.sdw.kg.handler.solr.SolrHandler.TAGGER_LANGUAGE;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import rdfcf.Scheduler;

@RestController
@Api(value = "kg-util", description = "REsT KG Util")
@RequestMapping(value = "/kgutil")
public class ControllerKGUtil {

	static final String VERSION = "v1";
	
	static String solrUrl = "http://localhost:8983/solr/companies";
	
    private static final Logger log = LoggerFactory.getLogger(Scheduler.class);
	
	/**
	 * 
	 * Controller for query execution on solr over REsT
	 * 
	 * @param q
	 * @param fq
	 * @return
	 * @throws IOException
	 * @throws KgException
	 */
	@ApiOperation(value = "queryExecution")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "q", value = "query", required = true, dataType = "string", paramType = "query", defaultValue = "nameDe:\"Salechard\""),
		//@ApiImplicitParam(name = "fq", value = "filter query", required = false, paramType = "query", defaultValue = "")
	})
	@RequestMapping(value = "queryExecutionSolr", method = RequestMethod.GET, produces = "application/"+VERSION+"+json")
	public List<String> queryExecutionSolr(
			@RequestParam(value = "q", required = true, defaultValue="") String q,
			@RequestParam(value = "fq[]", required = false, defaultValue="") String[] fq
			) throws IOException, KgException {
		
		SolrHandler solrHandler = new SolrHandler(solrUrl);
		
		List<KgSolrResultDocument> results = null;
		
		//Filter?
		List<String> fq_list = new ArrayList<String>();
		if(fq == null) {
			fq_list = null;
		} else {
			fq_list = Arrays.asList(fq);
		}

		results = solrHandler.executeQuery(q, fq_list);
		
		solrHandler.close();
		
		List<String> ou = new ArrayList<String>();
		
		int i = 1;
		
		for(KgSolrResultDocument d : results) {
			
			ou.add(d.toString());
		}
		
		return ou;
	}
	
	
	/**
	 * 
	 * @param entityName
	 * @return
	 * @throws KgException
	 * @throws IOException
	 */
	@ApiOperation(value = "entitiesFromText")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "text", value = "Input text which is supposed to be annotated.", required = true, dataType = "string", paramType = "query", defaultValue = "Berlin is a great city in Germany"),
		@ApiImplicitParam(name = "lang", value = "specifies the text language", required = true, dataType = "string", paramType = "query", defaultValue = "ENGLISH"),
		@ApiImplicitParam(name = "ovlap", value = "Overlap [ALL,NO_SUB,", required = true, dataType = "string", paramType = "query", defaultValue = "ALL")
	})
	@RequestMapping(value = "getEntitiesFromText", method = RequestMethod.GET, produces = "application/"+VERSION+"+json")
	public Set<AnnotationInfo> getEntitiesFromText(
			@RequestParam(value="text",required=true, defaultValue="Berlin is a great city in Germany")String text,
			@ApiParam(value = "SOLR filter queries (e.g. filter on type) ", required = false) @RequestParam(value="fq",required=false, defaultValue="")String[] fq,
			@ApiParam(value = "fields which are required from the SOLR document", required = false) @RequestParam(value="rf",required=false, defaultValue="")String[] rf,
			@RequestParam(value="lang",required=true, defaultValue="ENGLISH")String lang,
			@RequestParam(value="ovlap",required=true, defaultValue="ALL")String ovlap
			) throws KgException, IOException {
		
		TAGGER_LANGUAGE t_lang = TAGGER_LANGUAGE.ENGLISH;
		
		if(lang == "GERMAN") {
			t_lang = TAGGER_LANGUAGE.GERMAN;
		}
		
		TAGGER_ANNOTATION_OVERLAP t_ovlap = TAGGER_ANNOTATION_OVERLAP.ALL;
		
		if(ovlap == "NO_SUB") {
			t_ovlap = TAGGER_ANNOTATION_OVERLAP.NO_SUB;
		}
		else if (ovlap == "LONGEST_DOMINANT_RIGHT"){
			t_ovlap = TAGGER_ANNOTATION_OVERLAP.LONGEST_DOMINANT_RIGHT;
		}
		
		List<String> fq_list = Arrays.asList(fq);
		
		Set<String> rf_set = new HashSet<String>(Arrays.asList(rf));
		
		SolrHandler solrHandler = new SolrHandler(solrUrl);

		Map<AnnotationInfo, List<KgSolrResultDocument>> result = solrHandler.getNamedEntitiesFromText(text, fq_list, rf_set,
				  t_lang, t_ovlap);
		
		solrHandler.close();

		return result.keySet();
	}
	
	@RequestMapping(value="suggest", method = RequestMethod.GET )
	public List<String> autocomplete() {
		
		return null;
	}
	
}
