package it.cnr.istc.pst.cognition.koala.reasoner.owl.jena;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;

import it.cnr.istc.pst.cognition.koala.lang.dictionary.KoalaPropertyDictionary;
import it.cnr.istc.pst.cognition.koala.reasoner.environment.EnvironmentReasoner;
import it.cnr.istc.pst.cognition.koala.reasoner.observation.ObservationReasoner;
import it.cnr.istc.pst.cognition.koala.reasoner.observation.ObservationReasonerUpdate;

/**
 * 
 * @author anacleto
 *
 */
public class OWLKoalaObservationReasoner extends ObservationReasoner
{
	private OWLKoalaModel kb;											// the knowledge-base
	private Map<Statement, ObservationReasonerUpdate> cache;		// cache of notified observations
	
	private long inferenceTime;										// count time spent doing inference
	private int inferenceCounter;									// count number of inferred events
	private PrintWriter writer;
	
	/**
	 * 
	 * @param ontologyFilePath
	 * @param ruleFilePath
	 */
	public OWLKoalaObservationReasoner(String ontologyFilePath, String ruleFilePath) {
		super();
		// create a knowledge-base instance
		this.kb = new OWLKoalaModel(ontologyFilePath, ruleFilePath);
		// setup cache
		this.cache = new HashMap<Statement, ObservationReasonerUpdate>();
		this.writer = null;
		this.inferenceTime = 0;
	}
	
	/**
	 * 
	 */
	@Override
	protected void doInitialize(EnvironmentReasoner env) 
	{
		// set parameters
		this.inferenceTime = 0;
		this.inferenceCounter = 0;
		// current time
		long now = System.currentTimeMillis();
		// setup model
		this.kb.setup(env.getModel());
		// time
		long time = System.currentTimeMillis() - now;
		// update inference time
		this.inferenceTime += time;
		
		// setup cache
		this.cache.clear();
		
		try
		{
			// create data file
			File dataFile = new File("data/observation_reasoner_" + System.currentTimeMillis() + ".csv");
			// create file and write the header of the CSV
			this.writer = new PrintWriter(new BufferedWriter(new FileWriter(dataFile)));
			// print header
			this.writer.print("timestamp;number of inferred events;total inference time\n");
			this.writer.flush();
		}
		catch (IOException ex) {
			// set to null writer
			this.writer = null;
			// print error message 
			System.err.println("[ObservationReasoner] Error opening writer buffer for data\n- message: " + ex.getMessage());
		}
	}
	
	/**
	 * 
	 * @return
	 */
	@Override
	public OWLKoalaModel getModel() {
		// get current knowledge base
		return this.kb;
	}
	
	/**
	 * 
	 * @param sensorId
	 * @param observationValue
	 * @param propertyUri
	 * @throws Exception
	 */
	@Override
	public void doHandleObservation(String sensorId, Object observationValue, String propertyUri) 
			throws Exception
	{
		// get current time
		long now = System.currentTimeMillis();
		try
		{
			// retrieve sensor individual according to the ID
			Resource sensor = this.kb.getResourceById(new Long(sensorId));
			// create observation individual
			Resource observation = this.kb.createIndividual(OWLKoalaNameSpace.SSN.getNs() + "Observation");
			// create sensor output individual
			Resource output = this.kb.createIndividual(OWLKoalaNameSpace.SSN.getNs() + "SensorOutput");
			
			// assert properties
			this.kb.assertProperty(observation.getURI(), OWLKoalaNameSpace.SSN.getNs() + "hasOutput", output.getURI());
			this.kb.assertProperty(observation.getURI(), OWLKoalaNameSpace.SSN.getNs() + "observedBy", sensor.getURI());
			
			// check property in order to create observation value accordingly
			switch (KoalaPropertyDictionary.getPropertyByURI(propertyUri))
			{
				case KOALA_LUMINOSITY : 
				{	
					// assert observation property
					this.kb.assertProperty(
							observation.getURI(), 
							OWLKoalaNameSpace.SSN.getNs() + "observedProperty",
							OWLKoalaNameSpace.KOALA.getNs() + "Luminosity");
					
					
					// create observation value 
					Resource value = this.kb.createIndividual(
							OWLKoalaNameSpace.KOALA.getNs() + "LuminosityObservationValue");
				
					// assert data property
					this.kb.assertDataProperty(
							value.getURI(), 
							OWLKoalaNameSpace.KOALA.getNs() + "hasLuminosityValue",
							(Double) observationValue);
					
					// assert property
					this.kb.assertProperty(
							output.getURI(), 
							OWLKoalaNameSpace.SSN.getNs() + "hasValue", 
							value.getURI());
				}
				break;
	
				case KOALA_TEMPERATURE : 
				{
					// assert observation property
					this.kb.assertProperty(
							observation.getURI(), 
							OWLKoalaNameSpace.SSN.getNs() + "observedProperty", 
							OWLKoalaNameSpace.KOALA.getNs() + "Temperature");
					
					
					// create observation value 
					Resource value = this.kb.createIndividual(
							OWLKoalaNameSpace.KOALA.getNs() + "TemperatureObservationValue");
				
					// assert data property
					this.kb.assertDataProperty(
							value.getURI(), 
							OWLKoalaNameSpace.KOALA.getNs() + "hasTemperatureValue", 
							(Double) observationValue);
					
					// assert property
					this.kb.assertProperty(
							output.getURI(), 
							OWLKoalaNameSpace.SSN.getNs() + "hasValue", 
							value.getURI());
				}
				break;
				
				
				case KOALA_ENERGY : 
				{
					// assert observation property
					this.kb.assertProperty(
							observation.getURI(), 
							OWLKoalaNameSpace.SSN.getNs() + "observedProperty", 
							OWLKoalaNameSpace.KOALA.getNs() + "Energy");
					
					
					// create observation value 
					Resource value = this.kb.createIndividual(
							OWLKoalaNameSpace.KOALA.getNs() + "EnergyObservationValue");
				
					// assert data property
					this.kb.assertDataProperty(
							value.getURI(), 
							OWLKoalaNameSpace.KOALA.getNs() + "hasEnergyValue",
							(Double) observationValue);
					
					// assert property
					this.kb.assertProperty(
							output.getURI(), 
							OWLKoalaNameSpace.SSN.getNs() + "hasValue", 
							value.getURI());
				}
				break;
					
				case KOALA_PRESENCE : 
				{
					// assert observation property
					this.kb.assertProperty(
							observation.getURI(), 
							OWLKoalaNameSpace.SSN.getNs() + "observedProperty", 
							OWLKoalaNameSpace.KOALA.getNs() + "Presence");
					
					
					// create observation value 
					Resource value = this.kb.createIndividual(
							OWLKoalaNameSpace.KOALA.getNs() + "PresenceObservationValue");
				
					// assert data property
					this.kb.assertDataProperty(
							value.getURI(), 
							OWLKoalaNameSpace.KOALA.getNs() + "hasPresenceValue",
							(Boolean) observationValue);
					
					// assert property
					this.kb.assertProperty(
							output.getURI(), 
							OWLKoalaNameSpace.SSN.getNs() + "hasValue", 
							value.getURI());
					
				} 
				break;
				
				case KOALA_BATTERY_LEVEL: 
				{
					// assert observation property
					this.kb.assertProperty(
							observation.getURI(), 
							OWLKoalaNameSpace.SSN.getNs() + "observedProperty", 
							OWLKoalaNameSpace.KOALA.getNs() + "BatteryLevel");
					
					
					// create observation value 
					Resource value = this.kb.createIndividual(
							OWLKoalaNameSpace.KOALA.getNs() + "BatteryLevelObservationValue");
				
					// assert data property
					this.kb.assertDataProperty(
							value.getURI(), 
							OWLKoalaNameSpace.KOALA.getNs() + "hasBatteryLevelValue",
							(Double) observationValue);
					
					// assert property
					this.kb.assertProperty(
							output.getURI(), 
							OWLKoalaNameSpace.SSN.getNs() + "hasValue", 
							value.getURI());
				}
				break;
				
				
				case KOALA_BLOOD_PRESSURE : 
				{
					// get observed ranges
					Integer[] ranges = (Integer[]) observationValue;
					int min = ranges[0];
					int max = ranges[1];
					
					// assert observation property
					this.kb.assertProperty(
							observation.getURI(), 
							OWLKoalaNameSpace.SSN.getNs() + "observedProperty", 
							OWLKoalaNameSpace.KOALA.getNs() + "BloodPressure");
					
					
					// create observation value 
					Resource value = this.kb.createIndividual(
							OWLKoalaNameSpace.KOALA.getNs() + "BloodPressureObservationValue");
				
					// assert data property
					this.kb.assertDataProperty(
							value.getURI(), 
							OWLKoalaNameSpace.KOALA.getNs() + "hasMinBloodPressureValue",
							min);
					
					this.kb.assertDataProperty(
							value.getURI(), 
							OWLKoalaNameSpace.KOALA.getNs() + "hasMaxBloodPressureValue",
							max);
					
					// assert property
					this.kb.assertProperty(
							output.getURI(), 
							OWLKoalaNameSpace.SSN.getNs() + "hasValue", 
							value.getURI());
				}
				break;
				
				
				case KOALA_BLOOD_SUGAR : 
				{
					// assert observation property
					this.kb.assertProperty(
							observation.getURI(), 
							OWLKoalaNameSpace.SSN.getNs() + "observedProperty", 
							OWLKoalaNameSpace.KOALA.getNs() + "BloodSugar");
					
					
					// create observation value 
					Resource value = this.kb.createIndividual(
							OWLKoalaNameSpace.KOALA.getNs() + "BloodSugarObservationValue");
				
					// assert data property
					this.kb.assertDataProperty(
							value.getURI(), 
							OWLKoalaNameSpace.KOALA.getNs() + "hasBloodSugarValue",
							(Double) observationValue);
					
					// assert property
					this.kb.assertProperty(
							output.getURI(), 
							OWLKoalaNameSpace.SSN.getNs() + "hasValue", 
							value.getURI());
				}
				break;
				
				case KOALA_BODY_TEMPERATURE : 
				{
					// assert observation property
					this.kb.assertProperty(
							observation.getURI(), 
							OWLKoalaNameSpace.SSN.getNs() + "observedProperty", 
							OWLKoalaNameSpace.KOALA.getNs() + "BodyTemperature");
					
					
					// create observation value 
					Resource value = this.kb.createIndividual(
							OWLKoalaNameSpace.KOALA.getNs() + "BodyTemperatureObservationValue");
				
					// assert data property
					this.kb.assertDataProperty(
							value.getURI(), 
							OWLKoalaNameSpace.KOALA.getNs() + "hasBodyTemperatureValue",
							(Double) observationValue);
					
					// assert property
					this.kb.assertProperty(
							output.getURI(), 
							OWLKoalaNameSpace.SSN.getNs() + "hasValue", 
							value.getURI());
				}
				break;
				
				
				case KOALA_BODY_WEIGHT : 
				{
					// assert observation property
					this.kb.assertProperty(
							observation.getURI(), 
							OWLKoalaNameSpace.SSN.getNs() + "observedProperty", 
							OWLKoalaNameSpace.KOALA.getNs() + "BodyWeight");
					
					
					// create observation value 
					Resource value = this.kb.createIndividual(
							OWLKoalaNameSpace.KOALA.getNs() + "BodyWeightObservationValue");
				
					// assert data property
					this.kb.assertDataProperty(
							value.getURI(), 
							OWLKoalaNameSpace.KOALA.getNs() + "hasBodyWeightValue",
							(Double) observationValue);
					
					// assert property
					this.kb.assertProperty(
							output.getURI(), 
							OWLKoalaNameSpace.SSN.getNs() + "hasValue", 
							value.getURI());
				}
				break;
				
				
				case KOALA_CONTACT : 
				{
					// assert observation property
					this.kb.assertProperty(
							observation.getURI(), 
							OWLKoalaNameSpace.SSN.getNs() + "observedProperty", 
							OWLKoalaNameSpace.KOALA.getNs() + "Contact");
					
					
					// create observation value 
					Resource value = this.kb.createIndividual(
							OWLKoalaNameSpace.KOALA.getNs() + "ContactObservationValue");
				
					// assert data property
					this.kb.assertDataProperty(
							value.getURI(), 
							OWLKoalaNameSpace.KOALA.getNs() + "hasContactValue",
							(Boolean) observationValue);
					
					// assert property
					this.kb.assertProperty(
							output.getURI(), 
							OWLKoalaNameSpace.SSN.getNs() + "hasValue", 
							value.getURI());
				}
				break;
				
				
				case KOALA_HEART_RATE : 
				{
					// assert observation property
					this.kb.assertProperty(
							observation.getURI(), 
							OWLKoalaNameSpace.SSN.getNs() + "observedProperty", 
							OWLKoalaNameSpace.KOALA.getNs() + "HeartRate");
					
					
					// create observation value 
					Resource value = this.kb.createIndividual(
							OWLKoalaNameSpace.KOALA.getNs() + "HeartRateObservationValue");
				
					// assert data property
					this.kb.assertDataProperty(
							value.getURI(), 
							OWLKoalaNameSpace.KOALA.getNs() + "hasHeartRateValue",
							(Double) observationValue);
					
					// assert property
					this.kb.assertProperty(
							output.getURI(), 
							OWLKoalaNameSpace.SSN.getNs() + "hasValue", 
							value.getURI());
				}
				break;
				
				
				case KOALA_OXIMETRY : 
				{
					// assert observation property
					this.kb.assertProperty(
							observation.getURI(), 
							OWLKoalaNameSpace.SSN.getNs() + "observedProperty", 
							OWLKoalaNameSpace.KOALA.getNs() + "Oximetry");
					
					
					// create observation value 
					Resource value = this.kb.createIndividual(
							OWLKoalaNameSpace.KOALA.getNs() + "OximetryObservationValue");
				
					// assert data property
					this.kb.assertDataProperty(
							value.getURI(), 
							OWLKoalaNameSpace.KOALA.getNs() + "hasOximetryValue",
							(Double) observationValue);
					
					// assert property
					this.kb.assertProperty(
							output.getURI(), 
							OWLKoalaNameSpace.SSN.getNs() + "hasValue", 
							value.getURI());
				}
				break;
				
				case KOALA_VOICE_COMMAND : 
				{
					// assert observation property
					this.kb.assertProperty(
							observation.getURI(), 
							OWLKoalaNameSpace.SSN.getNs() + "observedProperty", 
							OWLKoalaNameSpace.KOALA.getNs() + "VoiceCommand");
					
					
					// create observation value 
					Resource value = this.kb.createIndividual(
							OWLKoalaNameSpace.KOALA.getNs() + "VoiceCommandObservationValue");
				
					// assert data property
					this.kb.assertDataProperty(
							value.getURI(), 
							OWLKoalaNameSpace.KOALA.getNs() + "hasVoiceCommandValue",
							(String) observationValue);
					
					// assert property
					this.kb.assertProperty(
							output.getURI(), 
							OWLKoalaNameSpace.SSN.getNs() + "hasValue", 
							value.getURI());
				}
				break;
				
				
				default : 
					throw new RuntimeException("[ObservationReasoner] Unknown KOaLa property \"" + propertyUri + "\"");
			}
		}
		catch (Exception ex) {
			// forward exception
			throw new Exception(ex.getMessage());
		}
		finally {
			// compute inference time 
			long time = System.currentTimeMillis() - now;
			// update total inference time
			this.inferenceTime += time;
		}
	}

	/**
	 * 
	 */
	@Override
	protected List<ObservationReasonerUpdate> doPrepareObservationNotifications() 
	{
		// get current time
		long timestamp = System.currentTimeMillis();
		// list of observation to notify 
		List<ObservationReasonerUpdate> updates = new ArrayList<ObservationReasonerUpdate>();
		try
		{
			// get the list of inferred events
			List<Statement> list = this.kb.getStatements(
					OWLKoalaNameSpace.SSN.getNs() + "isProxyFor");
			
			// check inferred events
			for (Statement s : list) 
			{
				// check if already notified
				if (!this.cache.containsKey(s))
				{
					// increase inference counter
					this.inferenceCounter++;
					
					// get statement elements
					Resource subject = s.getSubject();
					// get statement's object
					Resource object = s.getResource();
					
					
					// get type property
					Property pType = this.kb.getProperty(OWLKoalaNameSpace.RDF.getNs() + "type");
					// get event property statement
					Statement pStatement = subject.getProperty(pType);
					// get inferred event type
					Resource eventType = pStatement.getResource();
					// get the observable feature associated
					Property concerns = this.kb.getProperty(OWLKoalaNameSpace.KOALA.getNs()+ "concerns");
					// get concerns property statement
					Statement cStatement = subject.getProperty(concerns);
					// get observable feature
					Resource concernedElement = cStatement.getResource();
					
					// check category
					if (object.getURI().equals(OWLKoalaNameSpace.KOALA.getNs() + "ObservedActivity")) 
					{
						// create activity update
						ObservationReasonerUpdate update = this.createActivityUpdate(
								subject.getId().getBlankNodeId().getLabelString(), 
								eventType.getURI(),
								concernedElement.getURI());
						
						// add update to the list 
						updates.add(update);
						// add event to the cache
						this.cache.put(s, update);
					}
					else 
					{
						// create event update
						ObservationReasonerUpdate update = this.createEventUpdate(
								subject.getId().getBlankNodeId().getLabelString(), 
								eventType.getURI(),
								concernedElement.getURI());
						
						// add update to the list
						updates.add(update);
						// add event to the cache
						this.cache.put(s, update);
					}
				}
			}
		}
		catch (Exception ex) {
			System.err.println("[ObservationReasoner] Error while checking inferred events/activities:\n"
					+ "\t- message= " + ex.getMessage() + "\n");
		}
		finally 
		{
			// write data
			if (this.writer != null) 
			{
				// print inferred event type and service type
				this.writer.print(timestamp + ";" + this.inferenceCounter + ";" + this.inferenceTime + "\n");
				// write record
				this.writer.flush();
			}
		}
		
		// get the list of updates
		return updates;
	}

	/**
	 * 
	 */
	@Override
	public void close() 
	{
		// close writer
		if (this.writer != null) {
			this.writer.close();
		}
	}
}