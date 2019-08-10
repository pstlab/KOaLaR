package it.cnr.istc.pst.koala.reasoner.observation;

import java.util.ArrayList;
import java.util.List;

import org.apache.jena.rdf.model.Model;

import it.cnr.istc.pst.koala.reasoner.environment.EnvironmentListener;
import it.cnr.istc.pst.koala.reasoner.environment.EnvironmentReasoner;
import it.cnr.istc.pst.koala.reasoner.goal.ObservationListener;

/**
 * 
 * @author anacleto
 *
 */
public abstract class ObservationReasoner implements EnvironmentListener
{
	private final List<ObservationListener> listeners;			// observation listeners
	protected EnvironmentReasoner environment;					// environment model
	
	/**
	 * 
	 */
	public ObservationReasoner() {
		this.listeners = new ArrayList<ObservationListener>();
	}
	
	/**
	 * 
	 * @param listener
	 */
	public void subscribe(ObservationListener listener) {
		synchronized (this.listeners) {
			// add subscriber
			this.listeners.add(listener);
		}
	}
	
	/**
	 * 
	 * @param env
	 */
	public void init(EnvironmentReasoner env) {
		// set environment
		this.environment = env;
		this.environment.subscribe(this);
		
		// clear list of subscribers
		synchronized (this.listeners) {
			this.listeners.clear();
		}
		
		// complete initialization
		this.doInitialize(this.environment);
	}
	
	/**
	 * 
	 */
	public void update() 
	{
		// TODO Auto-generated method stub
	}
	
	/**
	 * 
	 * @param env
	 */
	protected abstract void doInitialize(EnvironmentReasoner env);
	
	/**
	 * 
	 * @param sensorId
	 * @param value
	 * @param propertyUri
	 * @throws Exception
	 */
	public void observation(String sensorId, Object value, String propertyUri) 
			throws Exception
	{
		// do handle observation
		this.doHandleObservation(sensorId, value, propertyUri);
		
		// extract information about inferred events and/or activities
		List<ObservationReasonerUpdate> updates = this.doPrepareObservationNotifications();
		// check observed events
		if (!updates.isEmpty()) 
		{
			// notify observation listeners
			synchronized (this.listeners) {
				// send update signal
				for (ObservationListener listener : this.listeners) {
					// send update signal
					listener.update(updates);
				}
			}
		}
	}
	
	/**
	 * 
	 * @param sensorIds
	 * @param values
	 * @param propertyUris
	 * @throws Exception
	 */
	public void observation(String[] sensorIds, Object[] values, String[] propertyUris) 
			throws Exception 
	{
		// handle all observations
		for (int index = 0; index < sensorIds.length; index++) {
			// get observation information
			String sensorId = sensorIds[index];
			Object value = values[index];
			String propertyUri = propertyUris[index];
			
			// handle observation
			this.doHandleObservation(sensorId, value, propertyUri);
		}
		
		
		// extract information about inferred events and/or activities
		List<ObservationReasonerUpdate> updates = this.doPrepareObservationNotifications();
		// check observed events
		if (!updates.isEmpty()) 
		{
			// notify observation listeners
			synchronized (this.listeners) {
				// send update signal
				for (ObservationListener listener : this.listeners) {
					// send update signal
					listener.update(updates);
				}
			}
		}
	}
	
	/**
	 * 
	 * @param eventId
	 * @param eventType
	 * @return
	 */
	protected ObservationReasonerUpdate createEventUpdate(String eventId, String eventType) {
		// create update object
		return new ObservationReasonerUpdate(eventId, eventType, ObservationUpdateCategory.OBSERVED_EVENT);
		
	}
	
	/**
	 * 
	 * @param eventId
	 * @param eventType
	 * @return
	 */
	protected ObservationReasonerUpdate createActivityUpdate(String eventId, String eventType) {
		// create update object
		return new ObservationReasonerUpdate(eventId, eventType, ObservationUpdateCategory.OBSERVED_ACTIVITY);
		
	}
	
	
	/**
	 * 
	 * @param sensorId
	 * @param value
	 * @param propertyUri
	 * @throws Exception
	 */
	protected abstract void doHandleObservation(String sensorId, Object value, String propertyUri) 
			throws Exception;
	
	/**
	 * 
	 * @return
	 */
	protected abstract List<ObservationReasonerUpdate> doPrepareObservationNotifications();
	
	/**
	 * 
	 * @return
	 */
	public abstract Model getModel();
}
