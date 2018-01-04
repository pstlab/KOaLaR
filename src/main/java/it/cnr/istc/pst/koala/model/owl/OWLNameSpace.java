package it.cnr.istc.pst.koala.model.owl;

/**
 * 
 * @author alessandro
 *
 */
public enum OWLNameSpace 
{
	/**
	 * 
	 */
	RDF("http://www.w3.org/1999/02/22-rdf-syntax-ns#"),
	
	/**
	 * 
	 */
	RDFS("http://www.w3.org/2000/01/rdf-schema#"),
	
	/**
	 * 
	 */
	DUL("http://www.loa-cnr.it/ontologies/DUL.owl#"),
	
	/**
	 * 
	 */
	SSN("http://purl.oclc.org/NET/ssnx/ssn#"),

	/**
	 * 
	 */
	KOALA("http://pst.istc.cnr.it/ontologies/2017/1/koala#"),
	
	/**
	 * 
	 */
	KOALA_ON_STATE_INDIVIDUAL("http://pst.istc.cnr.it/ontologies/2017/1/koala#on_state"),
	
	/**
	 * 
	 */
	KOALA_FAILURE_STATE_INDIVIDUAL("http://pst.istc.cnr.it/ontologies/2017/1/koala#failure_state"),
	
	/**
	 * 
	 */
	KOALA_MAINTENANCE_STATE_INDIVIDUAL("http://pst.istc.cnr.it/ontologies/2017/1/koala#maintenance_state"),
	
	/**
	 * 
	 */
	KOALA_OFF_STATE_INDIVIDUAL("http://pst.istc.cnr.it/ontologies/2017/1/koala#off_state"),;
	
	private String ns;
	
	/**
	 * 
	 * @param ns
	 */
	private OWLNameSpace(String ns) {
		this.ns = ns;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getNs() {
		return ns;
	}
	
	/**
	 * 
	 */
	@Override
	public String toString() {
		return this.ns;
	}
}
