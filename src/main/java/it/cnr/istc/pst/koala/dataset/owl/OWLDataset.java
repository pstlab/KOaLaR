package it.cnr.istc.pst.koala.dataset.owl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
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
import org.apache.jena.util.iterator.ExtendedIterator;
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
		this.model = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM_MICRO_RULE_INF);	// create in-memory ontology model with OWL-Full language and optimized rule-based reasoner with OWL rules
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
		
		// convert to ontology class
		OntClass cls = res.as(OntClass.class);
		// create an individual of the class
		Resource resource = this.infModel.createResource(cls.getLocalName().toLowerCase() + "_" + IDCOUNTER.getAndIncrement(), cls);
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
		Resource tr = this.infModel.getResource(targetURI);
		if (tr == null) {
			throw new Exception("Resource with URI=\"" + targetURI + "\" not found in the knowledge-base");
		}
		// convert to RDF node
		RDFNode target = tr.as(RDFNode.class);
		
		// check whether a resource with "propertyURI" actually exists
		Resource pr = this.infModel.getResource(propertyURI);
		if (pr == null) {
			throw new Exception("Resource with URI=\"" + propertyURI + "\" not found in the knowledge-base");
		}
		
		// convert to property
		Property property = pr.as(Property.class);
		// add statement to the knowledge base
		this.infModel.add(reference, property, target);
	}
	
	/**
	 * 
	 */
	public void listStatements() {
		StmtIterator it = this.infModel.listStatements();
		while (it.hasNext()) {
			System.out.println("---> " + it.next());
		}
	}
	
	/**
	 * 
	 * @param propertyURI
	 */
	public void listStatements(String propertyURI) {
		Property p = this.infModel.getProperty(propertyURI);
		StmtIterator it = this.infModel.listStatements((Resource) null, p, (RDFNode) null);
		while (it.hasNext()) {
			System.out.println("---> " + it.next());
		}
	}
	
	/**
	 * 
	 * @param classURI
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Resource> getIndividualsOfClass(String classURI) 
			throws Exception
	{
		// check whether a resource with "classURI" actually exists
		Resource r = this.infModel.getResource(classURI);
		if (r == null) {
			throw new Exception("Resource with URI=\"" + classURI + "\" not found in the knowledge-base");
		}
		
		// convert to ontology class
		OntClass cls = r.as(OntClass.class);
		// list of individual
		List<Resource> list = new ArrayList<Resource>();
		// list instances of the class
		ExtendedIterator<Individual> it = (ExtendedIterator<Individual>) cls.listInstances();
		while (it.hasNext()) {
			// get instance of the class
			Individual resource = it.next();
			list.add(resource);
		}
		
		// get the list
		return list;
	}
}
