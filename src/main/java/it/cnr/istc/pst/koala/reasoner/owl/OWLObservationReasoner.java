package it.cnr.istc.pst.koala.reasoner.owl;

import org.apache.jena.rdf.model.Resource;

import it.cnr.istc.pst.koala.reasoner.environment.EnvironmentReasoner;
import it.cnr.istc.pst.koala.reasoner.observation.ObservationProperty;
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
	 * @param property
	 * @throws Exception
	 */
	public void observation(String sensorId, String observationValue, ObservationProperty property) 
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
			switch (property)
			{
				case LUMINOSITY : 
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
							new Long(observationValue).longValue());
					
					// assert property
					this.kb.assertProperty(
							output.getURI(), 
							OWLNameSpace.SSN.getNs() + "hasValue", 
							value.getURI());
				}
				break;
	
				case PRESENCE : {
					
					/*
					 * TODO: implement presence observation and missing cases
					 */
				} 
				break;
				
				case TEMPEREATURE : 
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
							new Long(observationValue).longValue());
					
					// assert property
					this.kb.assertProperty(
							output.getURI(), 
							OWLNameSpace.SSN.getNs() + "hasValue", 
							value.getURI());
				}
				break;
				
				
				case ENERGY:
					break;
					
					
				case POWER:
					break;
					
					
				case VOLTAGE:
					break;
					
					
				default:
					break;
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
			reasoner.observation("4", "11", ObservationProperty.LUMINOSITY);
			
			Thread.sleep(1000);
			
			reasoner.observation("4", "55", ObservationProperty.LUMINOSITY);
			
			
			Thread.sleep(1000);
			
			reasoner.observation("4", "21", ObservationProperty.TEMPEREATURE);
			
			Thread.sleep(1000);
			
			reasoner.observation("4", "10", ObservationProperty.TEMPEREATURE);
			
//			System.out.println("\n\n------- Knowledge-base Statements ------\n\n");
//			reasoner.kb.listStatements();
//			System.out.println("\n\n---------------------------------------\n\n");
		}
		catch (Exception ex) {
			System.err.println(ex.getMessage());
		}
		
	}
}
