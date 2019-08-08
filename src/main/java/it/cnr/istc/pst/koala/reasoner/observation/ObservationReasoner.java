package it.cnr.istc.pst.koala.reasoner.observation;

import it.cnr.istc.pst.koala.reasoner.environment.EnvironmentListener;
import it.cnr.istc.pst.koala.reasoner.environment.EnvironmentReasoner;

/**
 * 
 * @author anacleto
 *
 */
public abstract class ObservationReasoner implements EnvironmentListener
{
	protected EnvironmentReasoner environment;
	
	/**
	 * 
	 * @param env
	 */
	public void init(EnvironmentReasoner env) {
		this.environment = env;
		this.environment.subscribe(this);
		
		// complete initialization
		this.doInitialize();
	}
	
	/**
	 * 
	 */
	public void update() {
		// TODO Auto-generated method stub
	}
	
	/**
	 * 
	 */
	protected abstract void doInitialize();
	
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
