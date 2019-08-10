package it.cnr.istc.pst.koala.reasoner.owl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.rdf.model.InfModel;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.reasoner.rulesys.GenericRuleReasoner;
import org.apache.jena.reasoner.rulesys.Rule;
import org.apache.jena.vocabulary.ReasonerVocabulary;

/**
 * 
 * @author alessandro
 *
 */
public class OWLModel 
{
	private final AtomicLong idCounter;
	private String ontologyFile;
	private String inferenceRuleFile;
	private OntModel model;
	private InfModel infModel;
	
	/**
	 * 
	 * @param ontologyFile
	 * @param inferenceRuleFile
	 */
	protected OWLModel(String ontologyFile, String inferenceRuleFile) 
	{
		// initialize ID counter
		this.idCounter = new AtomicLong(0);
		
		// set ontology file 
		this.ontologyFile = ontologyFile;
		// set inference rule file
		this.inferenceRuleFile = inferenceRuleFile;
		// create the model schema from the ontology 
		this.model = ModelFactory.createOntologyModel(OntModelSpec.RDFS_MEM_TRANS_INF);		// create in-memory ontology model with RDFS language and transitive rule-based reasoner with RDFS rules
		// use DocumentManager API to specify that KOALA ontology is replicated locally on disk
		this.model.getDocumentManager().addAltEntry(OWLNameSpace.KOALA.getNs(), "file:" + this.ontologyFile);
		// read ontology file
		this.model.read(OWLNameSpace.KOALA.getNs());
		
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
	 */
	public void setup(Model model) 
	{
		// setup a new model
		this.model = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM_RDFS_INF, model);
		// use DocumentManager API to specify that KOALA ontology is replicated locally on disk
		this.model.getDocumentManager().addAltEntry(OWLNameSpace.KOALA.getNs(), "file:" + this.ontologyFile);
		// read ontology file
		this.model.read(OWLNameSpace.KOALA.getNs());
		
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
	public Model getModel() 
	{
		// create a new model
		OntModel model = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM_RDFS_INF, this.model);
		// use DocumentManager API to specify that KOALA ontology is replicated locally on disk
		model.getDocumentManager().addAltEntry(OWLNameSpace.KOALA.getNs(), "file:" + this.ontologyFile);
		// read ontology file
		model.read(OWLNameSpace.KOALA.getNs());
		
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
	 * @return
	 * @throws Exception
	 */
	public Resource getResourceById(Long id) 
			throws Exception
	{
		// get property
		Property property = this.infModel.getProperty(OWLNameSpace.KOALA.getNs() + "hasId");
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
	public Resource getResourceByURI(String resURI) {
		return this.infModel.getResource(resURI);
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
	
//	
	
	/**
	 * 
	 * @param subjectURI
	 * @param propertyId
	 * @return
	 * @throws Exception
	 */
	public List<Statement> getStatements(String subjectURI, String propertyId) 
			throws Exception 
	{
		// list of statements
		List<Statement> list = new ArrayList<Statement>();
		// get resource 
		Resource subject = this.infModel.getResource(subjectURI);
		if (subject == null) {
			throw new Exception("Resource with URI=\"" + subject + "\" not found in the knowledge-base");
		}
		
		// get property
		Property prop = this.infModel.getProperty(propertyId);
		if (prop == null) {
			throw new Exception("Property with URI=\"" + prop + "\" not found in the knowledge-base");
		}
		
		// iterate over statements
		Iterator<Statement> it = this.infModel.listStatements(subject, prop, (RDFNode) null);
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
	public List<Statement> getStatements(String propertyId) 
			throws Exception 
	{
		// list of statements
		List<Statement> list = new ArrayList<Statement>();
		
		// get property
		Property prop = this.infModel.getProperty(propertyId);
		if (prop == null) {
			throw new Exception("Property with URI=\"" + prop + "\" not found in the knowledge-base");
		}
		
		Iterator<Statement> it = this.infModel.listStatements((Resource) null, prop, (RDFNode) null);
		while (it.hasNext()) {
			// add statement to list
			list.add(it.next());
		}
		
		// get list
		return list;
	}
	
	/**
	 * 
	 * @param observableFeatureURI
	 * @param propertyTypeURI
	 * @return
	 * @throws Exception
	 */
	public Resource getObservedPropertyByType(String sensorURI, String propertyTypeURI) 
			throws Exception
	{
		// property instance
		Resource pInstance = null;
		// get sensor 
		Resource sensor = this.infModel.getResource(sensorURI);
		// get property
		Property prop = this.infModel.getProperty(OWLNameSpace.KOALA.getNs() + "isObservableThrough");
		// get observable features
		Iterator<Resource> it = this.infModel.listResourcesWithProperty(prop, sensor.asNode());
		while (it.hasNext() && pInstance == null)
		{
			// get observable feature
			Resource feature = it.next();
			// get property 
			prop = this.infModel.getProperty(OWLNameSpace.KOALA.getNs() + "hasObservableProperty");
			// get statements 
			Iterator<Statement> itt = feature.listProperties(prop);
			while (itt.hasNext() && pInstance == null)
			{
				// get statement 
				Statement s = itt.next();
				// get observable property
				Resource obsProp = s.getObject().asResource();
				
				// get property 
				prop = this.infModel.getProperty(OWLNameSpace.SSN.getNs() + "observes");
				// get property instance
				Statement result = obsProp.getProperty(prop);
				// get property instance
				Resource i = result.getObject().asResource();
				// check type
				prop = this.infModel.getProperty(OWLNameSpace.RDF.getNs() + "type");
				Resource type = i.getProperty(prop).getObject().asResource();
				// set property instance
				pInstance = type.getURI().equals(propertyTypeURI) ? i : null;
			}
		}
		
		// check if property instance has been found
		if (pInstance == null) {
			throw new Exception("No observed property with URI= \"" + propertyTypeURI + "\" associated to resource with URI=\"" + sensorURI +"\" has been found");
		}
		
		// get found property instance
		return pInstance;
	}
}
