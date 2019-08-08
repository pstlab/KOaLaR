package it.cnr.istc.pst.koala.reasoner.owl;

import java.util.List;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;

import it.cnr.istc.pst.koala.reasoner.environment.EnvironmentReasoner;
import it.cnr.istc.pst.koala.reasoner.environment.parser.lang.Room;
import it.cnr.istc.pst.koala.reasoner.environment.parser.lang.RoomObject;
import it.cnr.istc.pst.koala.reasoner.environment.parser.lang.Sensor;
import it.cnr.istc.pst.koala.reasoner.environment.parser.xml.XMLEnvironmentConfigurationParser;

/**
 * 
 * @author anacleto
 *
 */
public class OWLEnvironmentReasoner extends EnvironmentReasoner 
{
	public OWLModel kb;			// the knowledge base

	/**
	 * 
	 * @param ontologyFilePath
	 * @param ruleFilePath
	 */
	public OWLEnvironmentReasoner(String ontologyFilePath, String ruleFilePath) {
		// create a knowledge-base instance
		this.kb = new OWLModel(ontologyFilePath, ruleFilePath);
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
			// get XML configuration parser
			XMLEnvironmentConfigurationParser parser = new XMLEnvironmentConfigurationParser(envConfigFilePath);
			// get the list of rooms
			List<Room> rooms = parser.getListOfRooms();
			for (Room room : rooms)
			{
				// create room individual
				Resource iRoom = this.doCreateRoomIndividual(room);
				
				// parse room objects
				List<RoomObject> objects = parser.getListOfObjectsByRoom(room);
				for (RoomObject object : objects)
				{
					// create room object individual
					Resource iObject = this.doCreateRoomObjectIndividual(object);
					// locate object into the room
					this.doLocateObjectIntoRoom(iObject, iRoom);
					
					// check object installed sensors if any
					List<Sensor> sensors = parser.getListOfSensorsByObject(object);
					for (Sensor sensor : sensors) 
					{
						// create sensor individual
						Resource iSensor = this.doCreateSensorIndividual(sensor);
						// install sensor on object
						this.doInstallSensorOnElement(iSensor, iObject);
					}
				}
				
				// parse room sensors
				List<Sensor> sensors = parser.getListOfSensorsByRoom(room);
				for (Sensor sensor : sensors) 
				{
					// create sensor individual
					Resource iSensor = this.doCreateSensorIndividual(sensor);
					// install sensor on room
					this.doInstallSensorOnElement(iSensor, iRoom);
				}
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
	private Resource doCreateRoomIndividual(Room room) 
			throws Exception
	{
		// get room classification
		String classURI = room.getType();
		// create individual
		Resource resource = this.kb.createIndividual(classURI);
		// assert data property
		this.kb.assertDataProperty(resource.getURI(), OWLNameSpace.KOALA + "hasId", new Long(room.getId()));
		// create space region to model spatial information of the room
		Resource region = this.kb.createIndividual(OWLNameSpace.DUL + "SpaceRegion");
		// assert property
		this.kb.assertProperty(
				resource.getURI(), 
				OWLNameSpace.DUL + "hasRegion", 
				region.getURI());
		// get created individual
		return resource;
	}
	
	/**
	 * 
	 * @param object
	 * @return
	 * @throws Exception
	 */
	private Resource doCreateRoomObjectIndividual(RoomObject object) 
			throws Exception
	{
		// get object classification
		String classURI = object.getType();
		// create individual
		Resource resource = this.kb.createIndividual(classURI);
		// assert data property
		this.kb.assertDataProperty(resource.getURI(), OWLNameSpace.KOALA + "hasId", new Long(object.getId()));
		// create space region to model spatial information of the room
		Resource region = this.kb.createIndividual(OWLNameSpace.DUL + "SpaceRegion");
		// assert property
		this.kb.assertProperty(
				resource.getURI(), 
				OWLNameSpace.DUL + "hasRegion", 
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
		this.kb.assertDataProperty(resource.getURI(), OWLNameSpace.KOALA + "hasId", new Long(sensor.getId()));
		// check sensor state
		switch (sensor.getState()) 
		{
			// check failure state
			case FAILURE: {
				// assert failure state
				this.kb.assertProperty(resource.getURI(), OWLNameSpace.KOALA.getNs() + "hasDiagnosis", OWLNameSpace.KOALA_FAILURE_STATE_INDIVIDUAL.getNs());
			}
			break;
				
			// check maintenance state
			case MAINENTANCE: {
				// assert maintenance state
				this.kb.assertProperty(resource.getURI(), OWLNameSpace.KOALA.getNs() + "hasDiagnosis", OWLNameSpace.KOALA_MAINTENANCE_STATE_INDIVIDUAL.getNs());
			}
			break;
			
			// check off state
			case OFF: {
				// assert off state
				this.kb.assertProperty(resource.getURI(), OWLNameSpace.KOALA.getNs() + "hasDiagnosis", OWLNameSpace.KOALA_OFF_STATE_INDIVIDUAL.getNs());
			}
			break;
			
			// check on state
			case ON: {
				// assert on state
				this.kb.assertProperty(resource.getURI(), OWLNameSpace.KOALA.getNs() + "hasDiagnosis", OWLNameSpace.KOALA_ON_STATE_INDIVIDUAL.getNs());
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
		this.kb.assertProperty(room.getURI(), OWLNameSpace.DUL + "hasComponent", object.getURI());
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
		Resource platform = this.kb.createIndividual(OWLNameSpace.SSN + "Platform");
		// associate the platform to the holding element/object of the environment
		this.kb.assertProperty(element.getURI(), OWLNameSpace.DUL + "hasLocation", platform.getURI());
	
		// deploy sensor on platform
		Resource deployment = this.kb.createIndividual(OWLNameSpace.SSN + "Deployment");
		// assert related statements
		this.kb.assertProperty(sensor.getURI(), OWLNameSpace.SSN + "hasDeployment", deployment.getURI());
		this.kb.assertProperty(deployment.getURI(), OWLNameSpace.SSN + "deployedOnPlatform", platform.getURI());
	}
	
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) 
	{
		try
		{
			// create knowledge manager
			OWLEnvironmentReasoner km = new OWLEnvironmentReasoner(
					"etc/ontology/koala_v1.0.owl",
					"etc/ontology/feature_extraction_v1.0.rules");
			
			// initialize reasoner
			km.init("etc/environment/house_config.xml");
			
			
			
			System.out.println("-----------------------------------------------------------------------------------------");
			km.kb.listStatements(OWLNameSpace.SSN + "onPlatform");
			System.out.println("-----------------------------------------------------------------------------------------");
			km.kb.listStatements(OWLNameSpace.DUL + "hasComponent");
			System.out.println("-----------------------------------------------------------------------------------------");
			km.kb.listStatements(OWLNameSpace.KOALA + "hasDiagnosis");
			// check detected features of interest
			System.out.println("\n-------------------------------------------------------------------------------------------------\n"
					+ "\tList of detected observable features of the environment\n"
					+ "-------------------------------------------------------------------------------------------------\n");
			
			// list detected observable features
			km.kb.listIndividualsOfClass(OWLNameSpace.KOALA + "ObservableFeature");
			System.out.println("|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
			km.kb.listStatements(OWLNameSpace.KOALA + "hasObservableFeature");
			
			System.out.println("#######################################################################################################");
			// list properties that can be observed through features
			km.kb.listStatements(OWLNameSpace.KOALA + "hasObservableProperty");
		}
		catch (Exception ex) {
			System.err.println(ex.getMessage());
		}
	}
}
