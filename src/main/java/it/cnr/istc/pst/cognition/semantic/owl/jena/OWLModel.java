package it.cnr.istc.pst.cognition.semantic.owl.jena;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.rdf.model.InfModel;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.reasoner.ValidityReport;
import org.apache.jena.reasoner.rulesys.GenericRuleReasoner;
import org.apache.jena.reasoner.rulesys.Rule;
import org.apache.jena.vocabulary.ReasonerVocabulary;

/**
 * 
 * @author alessandro
 *
 */
public abstract class OWLModel 
{
	protected final AtomicLong idCounter;
	protected String ontologyFile;
	protected String inferenceRuleFile;
	protected OntModel model;
	protected InfModel infModel;
	
	/**
	 * 
	 * @param ontologyFile
	 * @param inferenceRuleFile
	 * @param domainNameSpace
	 */
	protected OWLModel(String ontologyFile, String inferenceRuleFile, String domainNameSpace) 
	{
		// set ID counter
		this.idCounter = new AtomicLong(0);
		// set ontology file 
		this.ontologyFile = ontologyFile;
		// set inference rule file
		this.inferenceRuleFile = inferenceRuleFile;
		// create the model schema from the ontology 
		this.model = ModelFactory.createOntologyModel(OntModelSpec.RDFS_MEM_TRANS_INF);		// create in-memory ontology model with RDFS language and transitive rule-based reasoner with RDFS rules
		// use DocumentManager API to specify that ontology is replicated locally on disk
		this.model.getDocumentManager().addAltEntry(domainNameSpace, "file:" + this.ontologyFile);
		// read ontology file
		this.model.read(domainNameSpace);
		
		// parse the list of inference rules for feature extraction 
		List<Rule> rules = Rule.rulesFromURL("file:" + inferenceRuleFile);
		// create a generic rule-based reasoner
		GenericRuleReasoner reasoner = new GenericRuleReasoner(rules);
		// configure reasoner - user forward chaining RETE rule engine
		reasoner.setParameter(ReasonerVocabulary.PROPruleMode, "forwardRETE");
		// create an inference model on the raw data model
		this.infModel = ModelFactory.createInfModel(reasoner, this.model);
	}
	
	/**
	 * 
	 * @return
	 */
	public String getOntologyFile() {
		return ontologyFile;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getInferenceRuleFile() {
		return inferenceRuleFile;
	}
	
	/**
	 * 
	 * @param model
	 * @param domainNameSpace
	 */
	protected void setup(Model model, String domainNameSpace) 
	{
		// setup a new model
		this.model = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM_RDFS_INF, model);
		// use DocumentManager API to specify that ontology is replicated locally on disk
		this.model.getDocumentManager().addAltEntry(domainNameSpace, "file:" + this.ontologyFile);
		// read ontology file
		this.model.read(domainNameSpace);
		
		// parse the list of inference rules for feature extraction 
		List<Rule> rules = Rule.rulesFromURL("file:" + inferenceRuleFile);
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
	 */
	public List<Resource> getIndividuals(String classURI)
	{
		// list of found individuals 
		List<Resource> list = new ArrayList<Resource>();
		// get resource class
		Resource c = this.getResource(classURI);
		if (c != null) {
			// iterate over individuals
			Iterator<Individual> it = this.model.listIndividuals(c);
			while (it.hasNext()) {
				list.add(it.next());
			}
		}
		
		// get the list
		return list;
	}
	
	
	/**
	 * 
	 * @return
	 */
	public void rebind() {
		// re-bind the inference model to the underlying dat model
		this.infModel.rebind();
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean validate() {
		// re-bind the inference model to the underlying dat model
		this.infModel.rebind();
		// validate the inference model of the knowledge base
		ValidityReport report = this.infModel.validate();
		// check if the model is valid
		return report.isValid();
	}
	
	/**
	 * 
	 * @return
	 */
	public Iterator<Statement> iterator() {
		// get an iterator over the statements of the model
		return this.infModel.listStatements();
	}
	
	/**
	 * 
	 * @return
	 */
	protected Model getModel(String domainNameSpace) 
	{
		// create a new model
		OntModel model = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM_RDFS_INF, this.model);
		// use DocumentManager API to specify that ontology is replicated locally on disk
		model.getDocumentManager().addAltEntry(domainNameSpace, "file:" + this.ontologyFile);
		// read ontology file
		model.read(domainNameSpace);
		
		// add inferred statements
		Iterator<Statement> it = this.infModel.getDeductionsModel().listStatements();
		while (it.hasNext()) {
			// add inferred statement
			model.add(it.next());
		}
		
		// get model 
		return model;
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
		Resource resource = this.infModel.createResource(res.getLocalName().toLowerCase() + "_" + this.idCounter.getAndIncrement(), res);
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
	 * @param referenceURI
	 * @param propertyURI
	 * @param value
	 * @throws Exception
	 */
	public void assertDataProperty(String referenceURI, String propertyURI, long value) 
			throws Exception
	{
		// check whether a resource with "referenceURI" actually exists
		Resource reference = this.infModel.getResource(referenceURI);
		if (reference == null) {
			throw new Exception("Resource with URI=\"" + referenceURI + "\" not found in the knowledge-base");
		}
	
		// check whether a resource with "propertyURI" actually exists
		Property property = this.infModel.getProperty(propertyURI);
		if (property == null) {
			throw new Exception("Property with URI=\"" + propertyURI + "\" not found in the knowledge-base");
		}
		
		// add literal and related property
		this.infModel.addLiteral(reference, property, value);
	}
	
	/**
	 * 
	 * @param referenceURI
	 * @param propertyURI
	 * @param value
	 * @throws Exception
	 */
	public void assertDataProperty(String referenceURI, String propertyURI, String value) 
			throws Exception
	{
		// check whether a resource with "referenceURI" actually exists
		Resource reference = this.infModel.getResource(referenceURI);
		if (reference == null) {
			throw new Exception("Resource with URI=\"" + referenceURI + "\" not found in the knowledge-base");
		}
	
		// check whether a resource with "propertyURI" actually exists
		Property property = this.infModel.getProperty(propertyURI);
		if (property == null) {
			throw new Exception("Property with URI=\"" + propertyURI + "\" not found in the knowledge-base");
		}
		
		// add literal and related property
		this.infModel.add(reference, property, value);
	}
	
	/**
	 * 
	 * @param referenceURI
	 * @param propertyURI
	 * @param value
	 * @throws Exception
	 */
	public void assertDataProperty(String referenceURI, String propertyURI, double value) 
			throws Exception
	{
		// check whether a resource with "referenceURI" actually exists
		Resource reference = this.infModel.getResource(referenceURI);
		if (reference == null) {
			throw new Exception("Resource with URI=\"" + referenceURI + "\" not found in the knowledge-base");
		}
	
		// check whether a resource with "propertyURI" actually exists
		Property property = this.infModel.getProperty(propertyURI);
		if (property == null) {
			throw new Exception("Property with URI=\"" + propertyURI + "\" not found in the knowledge-base");
		}
		
		// add literal and related property
		this.infModel.addLiteral(reference, property, value);
	}
	
	/**
	 * 
	 * @param referenceURI
	 * @param propertyURI
	 * @param value
	 * @throws Exception
	 */
	public void assertDataProperty(String referenceURI, String propertyURI, boolean value) 
			throws Exception
	{
		// check whether a resource with "referenceURI" actually exists
		Resource reference = this.infModel.getResource(referenceURI);
		if (reference == null) {
			throw new Exception("Resource with URI=\"" + referenceURI + "\" not found in the knowledge-base");
		}
	
		// check whether a resource with "propertyURI" actually exists
		Property property = this.infModel.getProperty(propertyURI);
		if (property == null) {
			throw new Exception("Property with URI=\"" + propertyURI + "\" not found in the knowledge-base");
		}
		
		// add literal and related property
		this.infModel.addLiteral(reference, property, value);
	}
	
	/**
	 * 
	 * @param referenceURI
	 * @param propertyURI
	 * @param value
	 * @throws Exception
	 */
	public void assertDataProperty(String referenceURI, String propertyURI, int value) 
			throws Exception
	{
		// check whether a resource with "referenceURI" actually exists
		Resource reference = this.infModel.getResource(referenceURI);
		if (reference == null) {
			throw new Exception("Resource with URI=\"" + referenceURI + "\" not found in the knowledge-base");
		}
	
		// check whether a resource with "propertyURI" actually exists
		Property property = this.infModel.getProperty(propertyURI);
		if (property == null) {
			throw new Exception("Property with URI=\"" + propertyURI + "\" not found in the knowledge-base");
		}
		
		// add literal and related property
		this.infModel.addLiteral(reference, property, value);
	}
	
	/**
	 * 
	 * @param id
	 * @param domainNameSpace
	 * @return
	 * @throws Exception
	 */
	protected Resource getResourceById(Long id, String domainNameSpace) 
			throws Exception
	{
		// get property
		Property property = this.infModel.getProperty(domainNameSpace + "hasId");
		// check statements
		Iterator<Statement> it = this.infModel.listLiteralStatements(null, property, id);
		if (!it.hasNext()) {
			throw new Exception("Resource with ID " + id + " not found into the knowledge base");
		}
		
		// get statement
		Statement s = it.next();
		// get resource
		return s.getSubject();
	}
	
	/**
	 * 
	 * @param resURI
	 * @return
	 */
	public Resource getResource(String resURI) {
		return this.infModel.getResource(resURI);
	}
	
	/**
	 * 
	 * @param propertyId
	 * @return
	 * @throws Exception
	 */
	public List<Statement> getStatements(String propertyId) 
			throws Exception  {
		// list of statements
		return this.getStatements(null, propertyId, null);
	}
	
	/**
	 * 
	 * @param propertyId
	 * @param targetId
	 * @return
	 * @throws Exception
	 */
	public List<Statement> getStatements(String propertyId, String targetId) 
			throws Exception {
		// get statements
		return this.getStatements(null, propertyId, targetId);
	}
	
	/**
	 * 
	 * @param subjectId
	 * @param propertyId
	 * @param targetId
	 * @return
	 * @throws Exception
	 */
	public List<Statement> getStatements(String subjectId, String propertyId, String targetId) 
			throws Exception 
	{
		// list of statements
		List<Statement> list = new ArrayList<Statement>();
		
		// check resource parameter
		Resource subject = null;
		if (subjectId != null) {
			// get resource 
			subject = this.infModel.getResource(subjectId);
			if (subject == null) {
				throw new Exception("Resource with URI=\"" + subjectId + "\" not found in the knowledge-base");
			}
		}
		
		// check property parameter
		Property prop = null;
		if (propertyId != null) {
			// get property
			prop = this.infModel.getProperty(propertyId);
			if (prop == null) {
				throw new Exception("Property with URI=\"" + propertyId + "\" not found in the knowledge-base");
			}
		}
		
		// check target parameter
		RDFNode target = null;
		if (targetId != null) {
			// get target
			target = (RDFNode) this.infModel.getResource(targetId);
			if (target == null) {
				throw new Exception("Target with URI=\"" + targetId + "\" not found in the knowledge-base");
			}
		}
		
		
		// iterate over statements
		Iterator<Statement> it = this.infModel.listStatements(subject, prop, target);
		while (it.hasNext()) {
			// add statement to list
			list.add(it.next());
		}
			
		
		// get list
		return list;
	}
	
	/**
	 * 
	 * @param propertyId
	 * @return
	 * @throws Exception
	 */
	public Property getProperty(String propertyId) 
			throws Exception
	{
		// get property
		Property prop = this.infModel.getProperty(propertyId);
		if (prop == null) {
			throw new Exception("Property with URI=\"" + propertyId + "\" not found in the knowledge-base");
		}
		
		// get property 
		return prop;
	}
}
