package it.cnr.istc.pst.koala.dataset.owl;

import java.util.List;

import org.apache.jena.ontology.Individual;
import org.apache.jena.rdf.model.Resource;

import it.cnr.istc.pst.koala.dataset.KnowledgeManager;
import it.cnr.istc.pst.koala.reasoner.parser.lang.Room;
import it.cnr.istc.pst.koala.reasoner.parser.lang.RoomObject;
import it.cnr.istc.pst.koala.reasoner.parser.lang.Sensor;
import it.cnr.istc.pst.koala.reasoner.parser.xml.XMLEnvironmentConfigurationParser;

/**
 * 
 * @author alessandro
 *
 */
public class OWLKnowledgeManager implements KnowledgeManager 
{
	private OWLDataset kb;			// the knowledge-base

	/**
	 * 
	 */
	public OWLKnowledgeManager() {
		// create a knowledge-base instance
		this.kb = new OWLDataset();
		// initialize knowledge-base
		this.init();
	}
	
	/**
	 * Initialize the knowledge-based by reading the XML configuration file of the environment
	 */
	protected void init() 
	{
		try
		{
			// get XML configuration parser
			XMLEnvironmentConfigurationParser parser = XMLEnvironmentConfigurationParser.getInstance();
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
					Resource iObject = this.doCreateRoomObject(object);
					// locate object into the room
					this.doLocateObjectIntoRoom(iObject, iRoom);
					
					
					// check object installed sensors if any
					List<Sensor> sensors = parser.getListOfSensorsByObject(object);
					for (Sensor sensor : sensors) 
					{
						// create sensor individual
						Resource iSensor = this.doCreateSensor(sensor);
						// install sensor on object
						this.doInstallSensorOnElement(iSensor, iObject);
					}
				}
				
				// parse room sensors
				List<Sensor> sensors = parser.getListOfSensorsByRoom(room);
				for (Sensor sensor : sensors) 
				{
					// create sensor individual
					Resource iSensor = this.doCreateSensor(sensor);
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
	 * @throws Exception
	 */
	public void listInstancesOfClass(String URI) 
			throws Exception 
	{
		// list of installed sensors
		List<Resource> list = this.kb.getIndividualsByClass(URI);
		for (Resource i : list) {
			System.out.println("Instance: " + i);
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
	private Resource doCreateRoomObject(RoomObject object) 
			throws Exception
	{
		// get object classification
		String classURI = object.getType();
		// create individual
		Resource resource = this.kb.createIndividual(classURI);
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
	private Resource doCreateSensor(Sensor sensor) 
			throws Exception
	{
		// get sensor classification
		String classURI = sensor.getType();
		// create individual into the knowledge base
		Resource resource = this.kb.createIndividual(classURI);
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
		this.kb.assertProperty(room.getURI(), OWLNameSpace.DUL + "hasPart", object.getURI());
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
		// associate the platform to the object
	
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
			OWLKnowledgeManager km = new OWLKnowledgeManager();
			km.listInstancesOfClass(OWLNameSpace.KOALA + "ObservableFeature");
		}
		catch (Exception ex) {
			System.err.println(ex.getMessage());
		}
	}
}
