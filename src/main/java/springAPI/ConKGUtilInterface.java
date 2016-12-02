package springAPI;

import java.util.List;

import aksw.org.sdw.kg.handler.solr.KgSolrResultDocument;

public interface ConKGUtilInterface {
	
//	- Text (lang) to URI
//	- Entitaetsname / Typ zu URI -> URI --> Autovervollstaendigen
	
	
	//TODO return type?
	//List<KgSolrResultDocument>
	public void executeQuery(String q, String[] fq);
	
	public void uriFromText();

	public void uriFromEntity();
	
	public void suggestEntity();
}
