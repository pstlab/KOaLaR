package it.cnr.istc.pst.koala.reasoner.owl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;

import it.cnr.istc.pst.koala.lang.dictionary.KoalaPropertyDictionary;
import it.cnr.istc.pst.koala.reasoner.environment.EnvironmentReasoner;
import it.cnr.istc.pst.koala.reasoner.observation.ObservationReasoner;
import it.cnr.istc.pst.koala.reasoner.observation.ObservationReasonerUpdate;

/**
 * 
 * @author anacleto
 *
 */
public class OWLObservationReasoner extends ObservationReasoner
{
	private OWLModel kb;											// the knowledge-base
	private Map<Statement, ObservationReasonerUpdate> cache;			// cache of notified obaervations
	
	/**
	 * 
	 * @param ontologyFilePath
	 * @param ruleFilePath
	 */
	public OWLObservationReasoner(String ontologyFilePath, String ruleFilePath) {
		super();
		// create a knowledge-base instance
		this.kb = new OWLModel(ontologyFilePath, ruleFilePath);
		// setup cache
		this.cache = new HashMap<Statement, ObservationReasonerUpdate>();
	}

	/**
	 * 
	 */
	@Override
	protected void doInitialize(EnvironmentReasoner env) {
		// setup model
		this.kb.setup(env.getModel());
		// setup cache
		this.cache.clear();
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
		try
		{
			// retrieve sensor individual according to the ID
			Resource sensor = this.kb.getResourceById(new Long(sensorId));
			// create observation individual
			Resource observation = this.kb.createIndividual(OWLNameSpace.SSN.getNs() + "Observation");
			// create sensor output individual
			Resource output = this.kb.createIndividual(OWLNameSpace.SSN.getNs() + "SensorOutput");
			
			// assert properties
			this.kb.assertProperty(observation.getURI(), OWLNameSpace.SSN.getNs() + "hasOutput", output.getURI());
			this.kb.assertProperty(observation.getURI(), OWLNameSpace.SSN.getNs() + "observedBy", sensor.getURI());
			
			// check property in order to create observation value accordingly
			switch (KoalaPropertyDictionary.getPropertyByURI(propertyUri))
			{
				case KOALA_LUMINOSITY : 
				{	
					// assert observation property
					this.kb.assertProperty(
							observation.getURI(), 
							OWLNameSpace.SSN.getNs() + "observedProperty",
							OWLNameSpace.KOALA.getNs() + "Luminosity");
					
					
					// create observation value 
					Resource value = this.kb.createIndividual(
							OWLNameSpace.KOALA.getNs() + "LuminosityObservationValue");
				
					// assert data property
					this.kb.assertDataProperty(
							value.getURI(), 
							OWLNameSpace.KOALA.getNs() + "hasLuminosityValue",
							(Double) observationValue);
					
					// assert property
					this.kb.assertProperty(
							output.getURI(), 
							OWLNameSpace.SSN.getNs() + "hasValue", 
							value.getURI());
				}
				break;
	
				case KOALA_TEMPERATURE : 
				{
					// assert observation property
					this.kb.assertProperty(
							observation.getURI(), 
							OWLNameSpace.SSN.getNs() + "observedProperty", 
							OWLNameSpace.KOALA.getNs() + "Temperature");
					
					
					// create observation value 
					Resource value = this.kb.createIndividual(
							OWLNameSpace.KOALA.getNs() + "TemperatureObservationValue");
				
					// assert data property
					this.kb.assertDataProperty(
							value.getURI(), 
							OWLNameSpace.KOALA.getNs() + "hasTemperatureValue", 
							(Double) observationValue);
					
					// assert property
					this.kb.assertProperty(
							output.getURI(), 
							OWLNameSpace.SSN.getNs() + "hasValue", 
							value.getURI());
				}
				break;
				
				
				case KOALA_ENERGY : 
				{
					// assert observation property
					this.kb.assertProperty(
							observation.getURI(), 
							OWLNameSpace.SSN.getNs() + "observedProperty", 
							OWLNameSpace.KOALA.getNs() + "Energy");
					
					
					// create observation value 
					Resource value = this.kb.createIndividual(
							OWLNameSpace.KOALA.getNs() + "EnergyObservationValue");
				
					// assert data property
					this.kb.assertDataProperty(
							value.getURI(), 
							OWLNameSpace.KOALA.getNs() + "hasEnergyValue",
							(Double) observationValue);
					
					// assert property
					this.kb.assertProperty(
							output.getURI(), 
							OWLNameSpace.SSN.getNs() + "hasValue", 
							value.getURI());
				}
				break;
					
				case KOALA_PRESENCE : 
				{
					// assert observation property
					this.kb.assertProperty(
							observation.getURI(), 
							OWLNameSpace.SSN.getNs() + "observedProperty", 
							OWLNameSpace.KOALA.getNs() + "Presence");
					
					
					// create observation value 
					Resource value = this.kb.createIndividual(
							OWLNameSpace.KOALA.getNs() + "PresenceObservationValue");
				
					// assert data property
					this.kb.assertDataProperty(
							value.getURI(), 
							OWLNameSpace.KOALA.getNs() + "hasPresenceValue",
							(Boolean) observationValue);
					
					// assert property
					this.kb.assertProperty(
							output.getURI(), 
							OWLNameSpace.SSN.getNs() + "hasValue", 
							value.getURI());
					
				} 
				break;
				
				case KOALA_BATTERY_LEVEL: 
				{
					// assert observation property
					this.kb.assertProperty(
							observation.getURI(), 
							OWLNameSpace.SSN.getNs() + "observedProperty", 
							OWLNameSpace.KOALA.getNs() + "BatteryLevel");
					
					
					// create observation value 
					Resource value = this.kb.createIndividual(
							OWLNameSpace.KOALA.getNs() + "BatteryLevelObservationValue");
				
					// assert data property
					this.kb.assertDataProperty(
							value.getURI(), 
							OWLNameSpace.KOALA.getNs() + "hasBatteryLevelValue",
							(Double) observationValue);
					
					// assert property
					this.kb.assertProperty(
							output.getURI(), 
							OWLNameSpace.SSN.getNs() + "hasValue", 
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
							OWLNameSpace.SSN.getNs() + "observedProperty", 
							OWLNameSpace.KOALA.getNs() + "BloodPressure");
					
					
					// create observation value 
					Resource value = this.kb.createIndividual(
							OWLNameSpace.KOALA.getNs() + "BloodPressureObservationValue");
				
					// assert data property
					this.kb.assertDataProperty(
							value.getURI(), 
							OWLNameSpace.KOALA.getNs() + "hasMinBloodPressureValue",
							min);
					
					this.kb.assertDataProperty(
							value.getURI(), 
							OWLNameSpace.KOALA.getNs() + "hasMaxBloodPressureValue",
							max);
					
					// assert property
					this.kb.assertProperty(
							output.getURI(), 
							OWLNameSpace.SSN.getNs() + "hasValue", 
							value.getURI());
				}
				break;
				
				
				case KOALA_BLOOD_SUGAR : 
				{
					// assert observation property
					this.kb.assertProperty(
							observation.getURI(), 
							OWLNameSpace.SSN.getNs() + "observedProperty", 
							OWLNameSpace.KOALA.getNs() + "BloodSugar");
					
					
					// create observation value 
					Resource value = this.kb.createIndividual(
							OWLNameSpace.KOALA.getNs() + "BloodSugarObservationValue");
				
					// assert data property
					this.kb.assertDataProperty(
							value.getURI(), 
							OWLNameSpace.KOALA.getNs() + "hasBloodSugarValue",
							(Double) observationValue);
					
					// assert property
					this.kb.assertProperty(
							output.getURI(), 
							OWLNameSpace.SSN.getNs() + "hasValue", 
							value.getURI());
				}
				break;
				
				case KOALA_BODY_TEMPERATURE : 
				{
					// assert observation property
					this.kb.assertProperty(
							observation.getURI(), 
							OWLNameSpace.SSN.getNs() + "observedProperty", 
							OWLNameSpace.KOALA.getNs() + "BodyTemperature");
					
					
					// create observation value 
					Resource value = this.kb.createIndividual(
							OWLNameSpace.KOALA.getNs() + "BodyTemperatureObservationValue");
				
					// assert data property
					this.kb.assertDataProperty(
							value.getURI(), 
							OWLNameSpace.KOALA.getNs() + "hasBodyTemperatureValue",
							(Double) observationValue);
					
					// assert property
					this.kb.assertProperty(
							output.getURI(), 
							OWLNameSpace.SSN.getNs() + "hasValue", 
							value.getURI());
				}
				break;
				
				
				case KOALA_BODY_WEIGHT : 
				{
					// assert observation property
					this.kb.assertProperty(
							observation.getURI(), 
							OWLNameSpace.SSN.getNs() + "observedProperty", 
							OWLNameSpace.KOALA.getNs() + "BodyWeight");
					
					
					// create observation value 
					Resource value = this.kb.createIndividual(
							OWLNameSpace.KOALA.getNs() + "BodyWeightObservationValue");
				
					// assert data property
					this.kb.assertDataProperty(
							value.getURI(), 
							OWLNameSpace.KOALA.getNs() + "hasBodyWeightValue",
							(Double) observationValue);
					
					// assert property
					this.kb.assertProperty(
							output.getURI(), 
							OWLNameSpace.SSN.getNs() + "hasValue", 
							value.getURI());
				}
				break;
				
				
				case KOALA_CONTACT : 
				{
					// assert observation property
					this.kb.assertProperty(
							observation.getURI(), 
							OWLNameSpace.SSN.getNs() + "observedProperty", 
							OWLNameSpace.KOALA.getNs() + "Contact");
					
					
					// create observation value 
					Resource value = this.kb.createIndividual(
							OWLNameSpace.KOALA.getNs() + "ContactObservationValue");
				
					// assert data property
					this.kb.assertDataProperty(
							value.getURI(), 
							OWLNameSpace.KOALA.getNs() + "hasContactValue",
							(Boolean) observationValue);
					
					// assert property
					this.kb.assertProperty(
							output.getURI(), 
							OWLNameSpace.SSN.getNs() + "hasValue", 
							value.getURI());
				}
				break;
				
				
				case KOALA_HEART_RATE : 
				{
					// assert observation property
					this.kb.assertProperty(
							observation.getURI(), 
							OWLNameSpace.SSN.getNs() + "observedProperty", 
							OWLNameSpace.KOALA.getNs() + "HeartRate");
					
					
					// create observation value 
					Resource value = this.kb.createIndividual(
							OWLNameSpace.KOALA.getNs() + "HeartRateObservationValue");
				
					// assert data property
					this.kb.assertDataProperty(
							value.getURI(), 
							OWLNameSpace.KOALA.getNs() + "hasHeartRateValue",
							(Boolean) observationValue);
					
					// assert property
					this.kb.assertProperty(
							output.getURI(), 
							OWLNameSpace.SSN.getNs() + "hasValue", 
							value.getURI());
				}
				break;
				
				
				case KOALA_OXIMETRY : 
				{
					// assert observation property
					this.kb.assertProperty(
							observation.getURI(), 
							OWLNameSpace.SSN.getNs() + "observedProperty", 
							OWLNameSpace.KOALA.getNs() + "Oximetry");
					
					
					// create observation value 
					Resource value = this.kb.createIndividual(
							OWLNameSpace.KOALA.getNs() + "OximetryObservationValue");
				
					// assert data property
					this.kb.assertDataProperty(
							value.getURI(), 
							OWLNameSpace.KOALA.getNs() + "hasOximetryValue",
							(Boolean) observationValue);
					
					// assert property
					this.kb.assertProperty(
							output.getURI(), 
							OWLNameSpace.SSN.getNs() + "hasValue", 
							value.getURI());
				}
				break;
				
				case KOALA_VOICE_COMMAND : 
				{
					// assert observation property
					this.kb.assertProperty(
							observation.getURI(), 
							OWLNameSpace.SSN.getNs() + "observedProperty", 
							OWLNameSpace.KOALA.getNs() + "VoiceCommand");
					
					
					// create observation value 
					Resource value = this.kb.createIndividual(
							OWLNameSpace.KOALA.getNs() + "VoiceCommandObservationValue");
				
					// assert data property
					this.kb.assertDataProperty(
							value.getURI(), 
							OWLNameSpace.KOALA.getNs() + "hasVoiceCommandValue",
							(String) observationValue);
					
					// assert property
					this.kb.assertProperty(
							output.getURI(), 
							OWLNameSpace.SSN.getNs() + "hasValue", 
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
	}

	/**
	 * 
	 */
	@Override
	protected List<ObservationReasonerUpdate> doPrepareObservationNotifications() 
	{
		// list of observation to notify 
		List<ObservationReasonerUpdate> updates = new ArrayList<ObservationReasonerUpdate>();
		try
		{
			// get the list of inferred events
			List<Statement> list = this.kb.getStatements(
					OWLNameSpace.KOALA.getNs() + "hasEventCategory");
			
			// check inferred events
			for (Statement s : list) 
			{
				// check if already notified
				if (!this.cache.containsKey(s))
				{
					// get statement elements
					Resource subject = s.getSubject();
					// get statement's object
					Resource object = s.getResource();
					
					
					// get type property
					Property pType = this.kb.getProperty(OWLNameSpace.RDF.getNs() + "type");
					// get event property statement
					Statement pStatement = subject.getProperty(pType);
					// get last inferred type
					Resource eventType = pStatement.getResource();
					
					// check category
					if (object.getURI().equals(OWLNameSpace.KOALA.getNs() + "ObservedActivity")) 
					{
						// create activity update
						ObservationReasonerUpdate update = this.createActivityUpdate(
								subject.getId().getBlankNodeId().getLabelString(), 
								eventType.getURI());
						
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
								eventType.getURI());
						
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
		
		// get the list of updates
		return updates;
	}
}