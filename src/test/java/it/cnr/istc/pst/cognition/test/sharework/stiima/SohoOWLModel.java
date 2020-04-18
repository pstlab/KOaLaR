package it.cnr.istc.pst.cognition.test.sharework.stiima;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;

import it.cnr.istc.pst.cognition.semantic.owl.jena.OWLModel;


/**
 * 
 * @author alessandroumbrico
 *
 */
public class SohoOWLModel extends OWLModel 
{
	public static final String RDF_NS = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
	public static final String RDFS_NS = "http://www.w3.org/2000/01/rdf-schema#";
	public static final String DUL_NS = "http://www.ontologydesignpatterns.org/ont/dul/DUL.owl#";
	public static final String SSN_NS = "http://purl.oclc.org/NET/ssnx/ssn#";
	public static final String SOHO_NS = "http://pst.istc.cnr.it/ontologies/2019/01/soho#"; 
	
	/**
	 * 
	 * @param ontoFile
	 */
	public SohoOWLModel(String ontoFile) {
		super(ontoFile, SOHO_NS);
	}
	
	/**
	 * 
	 * @param ontoFile
	 * @param infFile
	 */
	public SohoOWLModel(String ontoFile, String infFile) {
		super(ontoFile, infFile, SOHO_NS);
	}
	
	/**
	 * 
	 * @param model
	 */
	public void setup(Model model) {
		// setup the encapsulated semantic model
		super.setup(model, SOHO_NS);
	}
	
	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public Map<Resource, List<Resource>> getTaxonomyOfFunctions() 
			throws Exception 
	{
		// get taxonomy with Function as a root
		return this.getTaxonomy(SOHO_NS + "Function");
	}
	
	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	private Map<Resource, List<Resource>> getTaxonomy(String rootElementURI) 
			throws Exception
	{
		// get the taxonomy 
        Map<Resource, List<Resource>> taxonomy = new HashMap<Resource, List<Resource>>();
        // retrieve the root of the taxonomy
        Resource root = this.getResource(rootElementURI);
        
        // add the root into the search fringe
        List<Resource> fringe = new ArrayList<Resource>();
        fringe.add(root);
        
    	while (!fringe.isEmpty())
    	{
    		// remove the next element from the fringe
    		Resource parent = fringe.remove(0);
    		// update the graph
    		if (!taxonomy.containsKey(parent)) {
    			taxonomy.put(parent, new ArrayList<Resource>());
    		}
    		
    		// extract taxonomy children
	        List<Statement> list = this.getStatements(
	        		RDFS_NS + "subClassOf",
	        		parent.getURI());
	        
	        // check the list
	        for (Statement s : list) 
	        {
	        	// skip reflexive relationships
	        	if (!s.getSubject().equals(s.getResource()))
	        	{
    	        	// get child resource
    	        	Resource child = s.getSubject();
    	        	// update the taxonomy graph
    	        	taxonomy.get(parent).add(child);
    	        	// add the child to the fringe
    	        	fringe.add(child);
	        	}
	        }
    	}
    	
    	// get taxonomy
    	return taxonomy;
	}
}
