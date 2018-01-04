package it.cnr.istc.pst.koala.environment.observation.owl;

import it.cnr.istc.pst.koala.environment.configuration.reasoner.EnvironmentConfigurationReasoner;
import it.cnr.istc.pst.koala.environment.configuration.reasoner.owl.OWLEnvironmentConfigurationReasoner;
import it.cnr.istc.pst.koala.environment.observation.ObservationProperty;
import it.cnr.istc.pst.koala.environment.observation.ObservationReasoner;
import it.cnr.istc.pst.koala.model.owl.OWLModel;

/**
 * 
 * @author anacleto
 *
 */
public class OWLObservationReasoner extends ObservationReasoner
{
	private static final String RULE_SET_VERSION = "1.0";
	private static final String SITUATION_DETECTION_RULE_SET_FILE = "etc/ontology/environment_situation_detection_v" + RULE_SET_VERSION + ".rules";
	
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
	 * @param value
	 * @param property
	 */
	public void observation(String sensorId, String value, ObservationProperty property)
	{
		/*
		 * TODO
		 */
	}

	
	/*
	 * 
	 */
	public static void main(String[] args)
	{
		// create environment reasoner
		EnvironmentConfigurationReasoner env = new OWLEnvironmentConfigurationReasoner();
		// get observation reasoner
		OWLObservationReasoner reasoner = new OWLObservationReasoner(env);
		
		reasoner.kb.listStatements();
	}
}
