package it.cnr.istc.pst.cognition.test.sharework.stiima;

import java.util.List;
import java.util.Map;

import org.apache.jena.rdf.model.Resource;

/**
 * 
 * @author alessandroumbrico
 *
 */
public class StiimaKnowledgeReasonerTest 
{
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args)
	{
		try
		{
			// create onto model
			SohoOWLModel model = new SohoOWLModel(
					"etc/ontology/sharework/soho_demo_v1.1.owl", 
					"etc/ontology/sharework/soho_rules_v1.0.rules");
			
			// get taxonomy of function
			Map<Resource, List<Resource>> taxonomy = model.getTaxonomyOfFunctions();
			// print the taxonomy 
			for (Resource res : taxonomy.keySet()) {
				System.out.println("> " + res.getLocalName() + " (" + res.getURI() + ")");
				for (Resource child : taxonomy.get(res)) {
					System.out.println("\t> " + child.getLocalName() + " (" + child.getURI() + ")");
					// get individuals
					for (Resource i : model.getIndividuals(child.getURI())) {
						System.out.println("\t\t| " + i.getLocalName() + " (" + i.getURI() + ")");
					}
				}
			}
		}
		catch (Exception ex) {
			ex.printStackTrace();
			System.err.println(ex.getMessage());
		}
	}
}
