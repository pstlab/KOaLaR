package it.cnr.istc.pst.cognition.koala.reasoner.owl.jena;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;

import it.cnr.istc.pst.cognition.semantic.owl.jena.OWLModel;

/**
 * 
 * @author alessandro
 *
 */
public class OWLKoalaModel extends OWLModel 
{
	/**
	 * 
	 * @param ontologyFile
	 * @param inferenceRuleFile
	 */
	public OWLKoalaModel(String ontologyFile, String inferenceRuleFile) {
		// call super constructor
		super(ontologyFile, inferenceRuleFile, OWLKoalaNameSpace.KOALA.getNs());
	}
	
	
	
	/**
	 * 
	 * @param model
	 */
	public void setup(Model model) {
		// setup the encapsulated semantic model
		super.setup(model, OWLKoalaNameSpace.KOALA.getNs());
	}
	
	/**
	 * 
	 * @return
	 */
	public Model getModel() {
		// get the encapsulated semantic model
		return super.getModel(OWLKoalaNameSpace.KOALA.getNs());
	}
	
	
	
	/**
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public Resource getResourceById(Long id) 
			throws Exception {
		return super.getResourceById(id, OWLKoalaNameSpace.KOALA.getNs());
	}
}
