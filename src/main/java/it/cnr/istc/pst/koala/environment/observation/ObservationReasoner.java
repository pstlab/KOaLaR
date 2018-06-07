package it.cnr.istc.pst.koala.environment.observation;

import it.cnr.istc.pst.koala.environment.configuration.reasoner.EnvironmentConfigurationListener;
import it.cnr.istc.pst.koala.environment.configuration.reasoner.EnvironmentConfigurationReasoner;

/**
 * 
 * @author anacleto
 *
 */
public abstract class ObservationReasoner implements EnvironmentConfigurationListener
{
	protected EnvironmentConfigurationReasoner environment;
	
	/**
	 * 
	 * @param environment
	 */
	public ObservationReasoner(EnvironmentConfigurationReasoner environment) { 
		this.environment = environment;
		// subscribe to environment updates
		this.environment.subscribe(this);
	}
	
	/**
	 * 
	 */
	public void update() {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * 
	 * @param sensorId
	 * @param observationValue
	 * @param property
	 * @throws Exception
	 */
	public abstract void observation(String sensorId, String observationValue, ObservationProperty property) 
			throws Exception;
}
