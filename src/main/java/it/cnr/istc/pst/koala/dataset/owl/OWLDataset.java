package it.cnr.istc.pst.koala.dataset.owl;

import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.InfModel;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.reasoner.Reasoner;
import org.apache.jena.reasoner.ReasonerRegistry;
import org.apache.jena.vocabulary.ReasonerVocabulary;

/**
 * 
 * @author alessandro
 *
 */
public class OWLDataset 
{
	private static final String ONTOLOGY_RULE_SET_VERSION = "1.0.0";
	private static final String ONTOLOGY_RULE_SET_FILE = "etc/ontology/koala_v" + ONTOLOGY_RULE_SET_VERSION + ".rules";
	private static final String ONTOLOGY_NS = "http://pst.istc.cnr.it/ontologies/2017/1/koala#";
	private static final String ONTOLOGY_VERSION = "1.0.0";
	private static final String ONTOLOGY_FILE = "etc/ontology/koala_v" + ONTOLOGY_VERSION + ".owl";
	private OntModel tbox;
	private Model abox;
	private InfModel reasoner;
	
	/**
	 * 
	 */
	public OWLDataset() {
		// create the model schema from the ontology 
		this.tbox = ModelFactory.createOntologyModel();
		// read the schema model (TBox)
		this.tbox.getDocumentManager().addAltEntry(ONTOLOGY_NS, "file:");
		this.tbox.read(ONTOLOGY_NS, "RDF/XML");
		
		// create the knowledge base instance (KB)
		this.abox = ModelFactory.createDefaultModel();
		
		// create a rule-based reasoner to process knowledge
		Reasoner rr = ReasonerRegistry.getOWLMicroReasoner();
		rr.setParameter(ReasonerVocabulary.PROPruleSet, 
				ONTOLOGY_RULE_SET_FILE);
		// bind the rule-based reasoner to the schema
		rr.bindSchema(this.tbox);
		// instantiate the reasoner on the knowledge-base instance and ontology schema
		this.reasoner = ModelFactory.createInfModel(rr, this.abox);
	}
}
