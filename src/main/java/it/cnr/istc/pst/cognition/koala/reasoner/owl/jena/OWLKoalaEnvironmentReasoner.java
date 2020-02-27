package it.cnr.istc.pst.cognition.koala.reasoner.owl.jena;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;

import it.cnr.istc.pst.cognition.koala.reasoner.environment.EnvironmentReasoner;
import it.cnr.istc.pst.cognition.koala.reasoner.environment.parser.xml.Element;
import it.cnr.istc.pst.cognition.koala.reasoner.environment.parser.xml.Sensor;
import it.cnr.istc.pst.cognition.koala.reasoner.environment.parser.xml.XMLEnvironmentConfigurationParser;

/**
 * 
 * @author anacleto
 *
 */
public class OWLKoalaEnvironmentReasoner extends EnvironmentReasoner 
{
	public OWLKoalaModel kb;			// the knowledge base

	/**
	 * 
	 * @param ontologyFilePath
	 * @param ruleFilePath
	 */
	public OWLKoalaEnvironmentReasoner(String ontologyFilePath, String ruleFilePath) {
		// create a knowledge-base instance
		this.kb = new OWLKoalaModel(ontologyFilePath, ruleFilePath);
	}
	
	/**
	 * 
	 * @return
	 */
	@Override
	public Model getModel() {
		// get current knowledge base
		return this.kb.getModel();
	}
	
	/**
	 * Setup the knowledge-based by reading the XML configuration file of the environment
	 */
	public void init(String envConfigFilePath) 
	{
		try
		{
			// resource index
			Map<Element, Resource> index = new HashMap<Element, Resource>();
			
			// get XML configuration parser
			XMLEnvironmentConfigurationParser parser = new XMLEnvironmentConfigurationParser(envConfigFilePath);
			// get the list of elements
			List<Element> elements = parser.getElements();
			// check room elements
			for (Element element : elements)
			{
				// create room individual
				Resource resource = this.doCreateElementIndividual(element);
				// index element
				index.put(element, resource);
			}
			
			// assert structure information
			for (Element element : elements) 
			{
				// check part of 
				if (element.getPartOf() != null)
				{
					// get parent resource
					Resource rParent = index.get(element.getPartOf());
					Resource rChild = index.get(element);
					
					// locate object into the room
					this.doLocateObjectIntoRoom(rChild, rParent);
				}
			}
			
			
			// get list of sensors
			List<Sensor> sensors = parser.getSensors();
			for (Sensor sensor : sensors)
			{
				// create sensor individual
				Resource resource = this.doCreateSensorIndividual(sensor);
				// get sensor target
				Resource target = index.get(sensor.getTarget());
				// install sensor on object
				this.doInstallSensorOnElement(resource, target);
			}
		}
		catch (Exception ex) {
			throw new RuntimeException("Knowledge initialization error:\n- message: " + ex.getMessage() + "\n");
		}
	}
	
	/**
	 * 
	 * @param room
	 * @return
	 * @throws Exception
	 */
	private Resource doCreateElementIndividual(Element element) 
			throws Exception
	{
		// get room classification
		String classURI = element.getType();
		// create individual
		Resource resource = this.kb.createIndividual(classURI);
		// assert data property
		this.kb.assertDataProperty(resource.getURI(), OWLKoalaNameSpace.KOALA + "hasId", new Long(element.getId()));
		// create space region to model spatial information of the room
		Resource region = this.kb.createIndividual(OWLKoalaNameSpace.DUL + "SpaceRegion");
		// assert property
		this.kb.assertProperty(
				resource.getURI(), 
				OWLKoalaNameSpace.DUL + "hasRegion", 
				region.getURI());
		// get created individual
		return resource;
	}
	
	/**
	 * 
	 * @param sensor
	 * @return
	 * @throws Exception
	 */
	private Resource doCreateSensorIndividual(Sensor sensor) 
			throws Exception
	{
		// get sensor classification
		String classURI = sensor.getType();
		// create individual into the knowledge base
		Resource resource = this.kb.createIndividual(classURI);
		// assert data property
		this.kb.assertDataProperty(resource.getURI(), OWLKoalaNameSpace.KOALA + "hasId", new Long(sensor.getId()));
		// check sensor state
		switch (sensor.getState()) 
		{
			// check failure state
			case FAILURE: {
				// assert failure state
				this.kb.assertProperty(resource.getURI(), OWLKoalaNameSpace.KOALA.getNs() + "hasDiagnosis", OWLKoalaNameSpace.KOALA_FAILURE_STATE_INDIVIDUAL.getNs());
			}
			break;
				
			// check maintenance state
			case MAINENTANCE: {
				// assert maintenance state
				this.kb.assertProperty(resource.getURI(), OWLKoalaNameSpace.KOALA.getNs() + "hasDiagnosis", OWLKoalaNameSpace.KOALA_MAINTENANCE_STATE_INDIVIDUAL.getNs());
			}
			break;
			
			// check off state
			case OFF: {
				// assert off state
				this.kb.assertProperty(resource.getURI(), OWLKoalaNameSpace.KOALA.getNs() + "hasDiagnosis", OWLKoalaNameSpace.KOALA_OFF_STATE_INDIVIDUAL.getNs());
			}
			break;
			
			// check on state
			case ON: {
				// assert on state
				this.kb.assertProperty(resource.getURI(), OWLKoalaNameSpace.KOALA.getNs() + "hasDiagnosis", OWLKoalaNameSpace.KOALA_ON_STATE_INDIVIDUAL.getNs());
			}
			break;
		}
		
		// get created individual
		return resource;
	}
	
	/**
	 * 
	 * @param object
	 * @param room
	 * @throws Exception
	 */
	private void doLocateObjectIntoRoom(Resource object, Resource room) 
			throws Exception
	{
		// simply assert property - use DUL:hasPart for transitivity, DUL:hasComponent otherwise
		this.kb.assertProperty(room.getURI(), OWLKoalaNameSpace.DUL + "hasComponent", object.getURI());
	}
	
	/**
	 * 
	 * @param sensor
	 * @param element
	 * @throws Exception
	 */
	private void doInstallSensorOnElement(Resource sensor, Resource element) 
			throws Exception
	{
		// create a platform form the object 
		Resource platform = this.kb.createIndividual(OWLKoalaNameSpace.SSN + "Platform");
		// associate the platform to the holding element/object of the environment
		this.kb.assertProperty(element.getURI(), OWLKoalaNameSpace.DUL + "hasLocation", platform.getURI());
	
		// deploy sensor on platform
		Resource deployment = this.kb.createIndividual(OWLKoalaNameSpace.SSN + "Deployment");
		// assert related statements
		this.kb.assertProperty(sensor.getURI(), OWLKoalaNameSpace.SSN + "hasDeployment", deployment.getURI());
		this.kb.assertProperty(deployment.getURI(), OWLKoalaNameSpace.SSN + "deployedOnPlatform", platform.getURI());
	}
	
//	/**
//	 * 
//	 * @param args
//	 */
//	public static void main(String[] args) 
//	{
//		try
//		{
//			// create knowledge manager
//			OWLEnvironmentReasoner km = new OWLEnvironmentReasoner(
//					"etc/ontology/koala_v1.0.owl",
//					"etc/ontology/feature_extraction_v1.0.rules");
//			
//			// initialize reasoner
//			km.init("etc/environment/house_config.xml");
//			
//			
//			
//			System.out.println("-----------------------------------------------------------------------------------------");
//			km.kb.listStatements(OWLNameSpace.SSN + "deployedOnPlatform");
//			System.out.println("-----------------------------------------------------------------------------------------");
//			km.kb.listStatements(OWLNameSpace.DUL + "hasComponent");
//			System.out.println("-----------------------------------------------------------------------------------------");
//			km.kb.listStatements(OWLNameSpace.KOALA + "hasDiagnosis");
//			// check detected features of interest
//			System.out.println("\n-------------------------------------------------------------------------------------------------\n"
//					+ "\tList of detected observable features of the environment\n"
//					+ "-------------------------------------------------------------------------------------------------\n");
//			
//			// list detected observable features
//			km.kb.listIndividualsOfClass(OWLNameSpace.KOALA + "ObservableFeature");
//			System.out.println("|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
//			km.kb.listStatements(OWLNameSpace.KOALA + "hasObservableFeature");
//			
//			System.out.println("#######################################################################################################");
//			// list properties that can be observed through features
//			km.kb.listStatements(OWLNameSpace.KOALA + "hasObservableProperty");
//		}
//		catch (Exception ex) {
//			System.err.println(ex.getMessage());
//		}
//	}
}
