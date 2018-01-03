package it.cnr.istc.pst.koala.dataset.owl;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.rdf.model.InfModel;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.reasoner.rulesys.GenericRuleReasoner;
import org.apache.jena.reasoner.rulesys.Rule;
import org.apache.jena.vocabulary.ReasonerVocabulary;

/**
 * 
 * @author alessandro
 *
 */
public class OWLDataset 
{
	private static final AtomicLong IDCOUNTER = new AtomicLong(0);
	private static final String FEATURE_EXTRACTION_RULE_SET_VERSION = "1.0";
	private static final String FEATURE_EXTRACTION_RULE_SET_FILE = "etc/ontology/feature_extraction_v" + FEATURE_EXTRACTION_RULE_SET_VERSION + ".rules";
	private static final String ONTOLOGY_VERSION = "1.0";
	private static final String ONTOLOGY_FILE = "etc/ontology/koala_v" + ONTOLOGY_VERSION + ".owl";
	private OntModel model;
	private InfModel infModel;
	
	/**
	 * 
	 */
	protected OWLDataset() 
	{
		// create the model schema from the ontology 
//		this.model = ModelFactory.createOntologyModel();									// create in-memory ontology model with OWL-Full language and sub-class, sub-property inference
//		this.model = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM_RULE_INF);	// create in-memory ontology model with OWL-DL language and rule-based reasoner with OWL rules (computationally expansive)
//		this.model = ModelFactory.createOntologyModel(OntModelSpec.RDFS_MEM_TRANS_INF);		// create in-memory ontology model with RDFS language and transitive rule-based reasoner with RDFS rules
		this.model = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM_RDFS_INF);
		// use DocumentManager API to specify that KOALA ontology is replicated locally on disk
		this.model.getDocumentManager().addAltEntry(OWLNameSpace.KOALA.getNs(), "file:" + ONTOLOGY_FILE);
		// read ontology file
		this.model.read(OWLNameSpace.KOALA.getNs());
		
		
		// parse the list of inference rules for feature extraction 
		List<Rule> rules = Rule.rulesFromURL("file:" + FEATURE_EXTRACTION_RULE_SET_FILE);
		// create a generic rule-based reasoner
		GenericRuleReasoner reasoner = new GenericRuleReasoner(rules);
		// configure reasoner - user forward chaining RETE rule engine
		reasoner.setParameter(ReasonerVocabulary.PROPruleMode, "forwardRETE");
		// create an inference model on the raw data model
		this.infModel = ModelFactory.createInfModel(reasoner, this.model);
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
		// check whether a resource with "classURI" actually exists 
		Resource res = this.infModel.getResource(classURI);
		if (res == null) {
			throw new Exception("No resource found with URI= \"" + classURI + "\" in the knowledge-base");
		}
		
		// create an individual of the class
		Resource resource = this.infModel.createResource(res.getLocalName().toLowerCase() + "_" + IDCOUNTER.getAndIncrement(), res);
		// get created individual
		return resource;
	}
	
	/**
	 * 
	 * @param referenceURI
	 * @param propertyURI
	 * @param targetURI
	 * @throws Exception
	 */
	public void assertProperty(String referenceURI, String propertyURI, String targetURI) 
			throws Exception
	{
		// check whether a resource with "referenceURI" actually exists
		Resource reference = this.infModel.getResource(referenceURI);
		if (reference == null) {
			throw new Exception("Resource with URI=\"" + referenceURI + "\" not found in the knowledge-base");
		}
		
		// get target
		Resource target = this.infModel.getResource(targetURI);
		if (target == null) {
			throw new Exception("Resource with URI=\"" + targetURI + "\" not found in the knowledge-base");
		}
		
		// check whether a resource with "propertyURI" actually exists
		Property property = this.infModel.getProperty(propertyURI);
		if (property == null) {
			throw new Exception("Property with URI=\"" + propertyURI + "\" not found in the knowledge-base");
		}
		
		// add statement to the knowledge base
		this.infModel.add(reference, property, target);
	}
	
	/**
	 * 
	 */
	public void listStatements() {
		// get iterator over statements
		StmtIterator it = this.infModel.listStatements();
		while (it.hasNext()) {
			System.out.println("[Statement: " + it.next() + "]");
		}
	}
	
	/**
	 * 
	 * @param propertyURI
	 * @throws Exception
	 */
	public void listStatements(String propertyURI) 
			throws Exception
	{
		// get property 
		Property property = this.infModel.getProperty(propertyURI);
		if (property == null) {
			throw new Exception("Property with URI=\"" + propertyURI + "\" not found in the knowledge-base"); 
		}
		
		// get iterator over statements
		StmtIterator it = this.infModel.listStatements((Resource) null, property, (RDFNode) null);
		while (it.hasNext()) {
			System.out.println("[Statement: " + it.next() + "]");
		}
	}
	
	/**
	 * 
	 * @param classURI
	 * @return
	 * @throws Exception
	 */
	public void listIndividualsOfClass(String classURI) 
			throws Exception
	{
		// check whether a resource with "classURI" actually exists
		Resource resource = this.infModel.getResource(classURI);
		if (resource == null) {
			throw new Exception("Resource with URI=\"" + classURI + "\" not found in the knowledge-base");
		}
		
		// get property type
		Property property = this.infModel.getProperty(OWLNameSpace.RDF + "type");
		// check statements
		StmtIterator it = this.infModel.listStatements((Resource) null, property, resource.as(RDFNode.class));
		while (it.hasNext()) {
			System.out.println("[Statement: " + it.next() + "]");
		}
	}
}
