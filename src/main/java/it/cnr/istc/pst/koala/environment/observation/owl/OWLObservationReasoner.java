package it.cnr.istc.pst.koala.environment.observation.owl;

import org.apache.jena.rdf.model.Resource;

import it.cnr.istc.pst.koala.environment.configuration.reasoner.EnvironmentConfigurationReasoner;
import it.cnr.istc.pst.koala.environment.configuration.reasoner.owl.OWLEnvironmentConfigurationReasoner;
import it.cnr.istc.pst.koala.environment.observation.ObservationProperty;
import it.cnr.istc.pst.koala.environment.observation.ObservationReasoner;
import it.cnr.istc.pst.koala.model.owl.OWLModel;
import it.cnr.istc.pst.koala.model.owl.OWLNameSpace;

/**
 * 
 * @author anacleto
 *
 */
public class OWLObservationReasoner extends ObservationReasoner
{
	private static final String RULE_SET_VERSION = "1.0";
	private static final String SITUATION_DETECTION_RULE_SET_FILE = "etc/ontology/situation_detection_v" + RULE_SET_VERSION + ".rules";
	
	private OWLModel kb;											// the knowledge-base
	
	/**
	 * 
	 * @param environment
	 */
	public OWLObservationReasoner(EnvironmentConfigurationReasoner environment) {
		super(environment);
		// create a knowledge-base instance
		this.kb = new OWLModel(SITUATION_DETECTION_RULE_SET_FILE);
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
			EnvironmentConfigurationReasoner env = new OWLEnvironmentConfigurationReasoner();
			// get observation reasoner
			OWLObservationReasoner reasoner = new OWLObservationReasoner(env);
			
			
			// add observation
			reasoner.observation("7", "11", ObservationProperty.LUMINOSITY);
			
			Thread.sleep(1000);
			
			reasoner.observation("7", "23", ObservationProperty.LUMINOSITY);
			
			reasoner.kb.listStatements();
		}
		catch (Exception ex) {
			System.err.println(ex.getMessage());
		}
		
	}
}
