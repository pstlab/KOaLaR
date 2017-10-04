package it.cnr.istc.pst.koala.dataset.owl;

/**
 * 
 * @author alessandro
 *
 */
enum OWLNameSpace 
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
	KOALA("http://pst.istc.cnr.it/ontologies/2017/1/koala#");
	
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
