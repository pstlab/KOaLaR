package it.cnr.istc.pst.koala.reasoner.owl;

import org.apache.jena.rdf.model.Resource;

import it.cnr.istc.pst.koala.lang.dictionary.KoalaPropertyDictionary;
import it.cnr.istc.pst.koala.reasoner.environment.EnvironmentReasoner;
import it.cnr.istc.pst.koala.reasoner.observation.ObservationReasoner;

/**
 * 
 * @author anacleto
 *
 */
public class OWLObservationReasoner extends ObservationReasoner
{
	private OWLModel kb;											// the knowledge-base
	
	/**
	 * 
	 * @param ontologyFilePath
	 * @param ruleFilePath
	 */
	public OWLObservationReasoner(String ontologyFilePath, String ruleFilePath) {
		super();
		// create a knowledge-base instance
		this.kb = new OWLModel(ontologyFilePath, ruleFilePath);
	}

	/**
	 * 
	 */
	@Override
	protected void doInitialize() {
		// setup model
		this.kb.setup(this.environment.getModel());
	}
	
	/**
	 * 
	 * @param sensorId
	 * @param observationValue
	 * @param propertyUri
	 * @throws Exception
	 */
	@Override
	public void observation(String sensorId, Object observationValue, String propertyUri) 
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
					// retrieve observed property according to type
					Resource observedProperty = this.kb.getObservedPropertyByType(
							sensor.getURI(), 
							OWLNameSpace.KOALA.getNs() + "Luminosity");
					
					// assert observation property
					this.kb.assertProperty(
							observation.getURI(), 
							OWLNameSpace.SSN.getNs() + "observedProperty", 
							observedProperty.getURI());
					
					
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
					// retrieve observed property according to type
					Resource observedProperty = this.kb.getObservedPropertyByType(
							sensor.getURI(), 
							OWLNameSpace.KOALA.getNs() + "Temperature");
					
					// assert observation property
					this.kb.assertProperty(
							observation.getURI(), 
							OWLNameSpace.SSN.getNs() + "observedProperty", 
							observedProperty.getURI());
					
					
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
					// retrieve observed property according to type
					Resource observedProperty = this.kb.getObservedPropertyByType(
							sensor.getURI(), 
							OWLNameSpace.KOALA.getNs() + "Energy");
					
					// assert observation property
					this.kb.assertProperty(
							observation.getURI(), 
							OWLNameSpace.SSN.getNs() + "observedProperty", 
							observedProperty.getURI());
					
					
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
					// retrieve observed property according to type
					Resource observedProperty = this.kb.getObservedPropertyByType(
							sensor.getURI(), 
							OWLNameSpace.KOALA.getNs() + "Presence");
					
					// assert observation property
					this.kb.assertProperty(
							observation.getURI(), 
							OWLNameSpace.SSN.getNs() + "observedProperty", 
							observedProperty.getURI());
					
					
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
					// retrieve observed property according to type
					Resource observedProperty = this.kb.getObservedPropertyByType(
							sensor.getURI(), 
							OWLNameSpace.KOALA.getNs() + "BatteryLevel");
					
					// assert observation property
					this.kb.assertProperty(
							observation.getURI(), 
							OWLNameSpace.SSN.getNs() + "observedProperty", 
							observedProperty.getURI());
					
					
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
					
					// retrieve observed property according to type
					Resource observedProperty = this.kb.getObservedPropertyByType(
							sensor.getURI(), 
							OWLNameSpace.KOALA.getNs() + "BloodPressure");
					
					// assert observation property
					this.kb.assertProperty(
							observation.getURI(), 
							OWLNameSpace.SSN.getNs() + "observedProperty", 
							observedProperty.getURI());
					
					
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
					// retrieve observed property according to type
					Resource observedProperty = this.kb.getObservedPropertyByType(
							sensor.getURI(), 
							OWLNameSpace.KOALA.getNs() + "BloodSugar");
					
					// assert observation property
					this.kb.assertProperty(
							observation.getURI(), 
							OWLNameSpace.SSN.getNs() + "observedProperty", 
							observedProperty.getURI());
					
					
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
					// retrieve observed property according to type
					Resource observedProperty = this.kb.getObservedPropertyByType(
							sensor.getURI(), 
							OWLNameSpace.KOALA.getNs() + "BodyTemperature");
					
					// assert observation property
					this.kb.assertProperty(
							observation.getURI(), 
							OWLNameSpace.SSN.getNs() + "observedProperty", 
							observedProperty.getURI());
					
					
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
					// retrieve observed property according to type
					Resource observedProperty = this.kb.getObservedPropertyByType(
							sensor.getURI(), 
							OWLNameSpace.KOALA.getNs() + "BodyWeight");
					
					// assert observation property
					this.kb.assertProperty(
							observation.getURI(), 
							OWLNameSpace.SSN.getNs() + "observedProperty", 
							observedProperty.getURI());
					
					
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
					// retrieve observed property according to type
					Resource observedProperty = this.kb.getObservedPropertyByType(
							sensor.getURI(), 
							OWLNameSpace.KOALA.getNs() + "Contact");
					
					// assert observation property
					this.kb.assertProperty(
							observation.getURI(), 
							OWLNameSpace.SSN.getNs() + "observedProperty", 
							observedProperty.getURI());
					
					
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
					// retrieve observed property according to type
					Resource observedProperty = this.kb.getObservedPropertyByType(
							sensor.getURI(), 
							OWLNameSpace.KOALA.getNs() + "HeartRate");
					
					// assert observation property
					this.kb.assertProperty(
							observation.getURI(), 
							OWLNameSpace.SSN.getNs() + "observedProperty", 
							observedProperty.getURI());
					
					
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
					// retrieve observed property according to type
					Resource observedProperty = this.kb.getObservedPropertyByType(
							sensor.getURI(), 
							OWLNameSpace.KOALA.getNs() + "Oximetry");
					
					// assert observation property
					this.kb.assertProperty(
							observation.getURI(), 
							OWLNameSpace.SSN.getNs() + "observedProperty", 
							observedProperty.getURI());
					
					
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
					// retrieve observed property according to type
					Resource observedProperty = this.kb.getObservedPropertyByType(
							sensor.getURI(), 
							OWLNameSpace.KOALA.getNs() + "VoiceCommand");
					
					// assert observation property
					this.kb.assertProperty(
							observation.getURI(), 
							OWLNameSpace.SSN.getNs() + "observedProperty", 
							observedProperty.getURI());
					
					
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

	/*
	 * 
	 */
	public static void main(String[] args)
	{
		try
		{
			// create environment reasoner
			EnvironmentReasoner env = new OWLEnvironmentReasoner(
					"etc/ontology/koala_v1.0.owl",
					"etc/ontology/feature_extraction_v1.0.rules");
			
			// initialize reasoner
			env.init("etc/environment/house_config.xml");
			
			
			// get observation reasoner
			ObservationReasoner reasoner = new OWLObservationReasoner(
					"etc/ontology/koala_v1.0.owl",
					"etc/ontology/situation_detection_v1.0.rules");
			
			// initialize reasoner over the environment
			reasoner.init(env);
			
			
			// add observation
			reasoner.observation(
					"25", 
					"11", 
					KoalaPropertyDictionary.KOALA_LUMINOSITY.getUri());
			
			Thread.sleep(1000);
			
			reasoner.observation(
					"25", 
					"55", 
					KoalaPropertyDictionary.KOALA_LUMINOSITY.getUri());
			
			
			Thread.sleep(1000);
			
			reasoner.observation(
					"25", 
					"21", 
					KoalaPropertyDictionary.KOALA_TEMPERATURE.getUri());
			
			Thread.sleep(1000);
			
			reasoner.observation(
					"25", 
					"10", 
					KoalaPropertyDictionary.KOALA_TEMPERATURE.getUri());
		}
		catch (Exception ex) {
			System.err.println(ex.getMessage());
		}
		
	}
}
