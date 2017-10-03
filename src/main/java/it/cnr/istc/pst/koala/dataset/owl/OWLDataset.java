package it.cnr.istc.pst.koala.dataset.owl;

import java.util.ArrayList;
import java.util.List;

import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.InfModel;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.reasoner.Reasoner;
import org.apache.jena.reasoner.ReasonerRegistry;
import org.apache.jena.reasoner.rulesys.GenericRuleReasonerFactory;
import org.apache.jena.vocabulary.ReasonerVocabulary;

/**
 * 
 * @author alessandro
 *
 */
public class OWLDataset 
{
	private static final String FEATURE_EXTRACTION_RULE_SET_VERSION = "1.0.0";
	private static final String FEATURE_EXTRACTION_RULE_SET_FILE = "etc/ontology/feature_extraction_v" + FEATURE_EXTRACTION_RULE_SET_VERSION + ".rules";
	private static final String ONTOLOGY_VERSION = "1.0.0";
	private static final String ONTOLOGY_FILE = "etc/ontology/koala_v" + ONTOLOGY_VERSION + ".owl";
	private OntModel tbox;
//	private Model abox;
	private InfModel infModel;
	
	/**
	 * 
	 */
	protected OWLDataset() {
		// create the model schema from the ontology 
		this.tbox = ModelFactory.createOntologyModel();
		// read the schema model (TBox)
		this.tbox.getDocumentManager().addAltEntry(OWLNameSpace.KOALA.getNs(), "file:" + ONTOLOGY_FILE);
		this.tbox.read(OWLNameSpace.KOALA.getNs(), "RDF/XML");
		
		
		Model m = ModelFactory.createDefaultModel();
		Resource configuration = m.createResource();
		configuration.addProperty(ReasonerVocabulary.PROPruleSet, FEATURE_EXTRACTION_RULE_SET_FILE);
//		
//		// create a rule-based reasoner to process knowledge
//		Reasoner rr = ReasonerRegistry.getOWLMicroReasoner();
//		rr.setParameter(ReasonerVocabulary.PROPruleSet, 
//				FEATURE_EXTRACTION_RULE_SET_FILE);

		// create reasoner
//		this.reasoner = ModelFactory.createInfModel(rr, this.tbox);
		Reasoner r = GenericRuleReasonerFactory.theInstance().create(configuration);
		this.infModel = ModelFactory.createInfModel(r, this.tbox);
	
	}
	
	/**
	 * 
	 * @param classURI
	 * @return
	 * @throws Exception
	 */
	public Resource createIndividual(String classURI) 
			throws Exception
	{
		// get ontology class 
		OntClass clazz = this.tbox.getOntClass(classURI);
		if (clazz == null) {
			throw new Exception("No class found with URI= \"" + classURI + "\" in KOaLa ontology");
		}
		
		// create resource
		Resource resource = this.infModel.createResource(clazz);
		// get created individual
		return resource;
	}
	
	/**
	 * 
	 * @param referenceId
	 * @param propertyId
	 * @param targetId
	 * @throws Exception
	 */
	public void assertProperty(String referenceId, String propertyId, String targetId) 
			throws Exception
	{
		// get reference resource
		Resource reference = this.infModel.getResource(referenceId);
		if (reference == null) {
			throw new Exception("Individual with URI=\"" + referenceId + "\" not found in the knowledge base");
		}
		// get target
		Resource target = this.infModel.getResource(targetId);
		if (target == null) {
			throw new Exception("Individual with URI=\"" + targetId + "\" not found in the knowledge base");
		}
		
		// get property
		Property property = this.tbox.getProperty(propertyId);
		if (property == null) {
			throw new Exception("Property with URI=\"" + propertyId + "\" not found in the ontology");
		}
		
		// add statement to the knowledge base
		this.infModel.add(reference, property, target);
	}
	
	/**
	 * 
	 * @param classURI
	 * @return
	 * @throws Exception
	 */
	public List<Resource> getIndividualsByClass(String classURI) 
			throws Exception
	{
		// list of individual
		List<Resource> list = new ArrayList<Resource>();
		// get class from ontology
		OntClass clazz = this.tbox.getOntClass(classURI);
		// get ontology class
		if (clazz == null) {
			throw new Exception("No class found with URI=\"" + classURI + "\" in KOaLa ontology"); 
		}
		
		// get property 
		Property p = this.tbox.getProperty(OWLNameSpace.RDF + "type");
		if (p == null) {
			throw new Exception("No property found with URI=\"" + OWLNameSpace.RDF + "type" + "\" in KOaLa ontology"); 
		}
		
		// get statements
		StmtIterator it = this.infModel.listStatements(null, p, clazz);
		while (it.hasNext()) {
			// get statement
			Statement s = it.next();
			// get the subject of the statement
			Resource reference = s.getSubject();
			list.add(reference);
		}
		
		// get the list
		return list;
	}
}
