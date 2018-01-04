package it.cnr.istc.pst.koala.environment.configuration.reasoner;

import java.util.ArrayList;
import java.util.List;

import org.apache.jena.rdf.model.Model;

/**
 * 
 * @author alessandro
 *
 */
public abstract class EnvironmentConfigurationReasoner 
{
	private final List<EnvironmentConfigurationListener> listeners;
	
	/**
	 * 
	 */
	public EnvironmentConfigurationReasoner() {
		this.listeners = new ArrayList<EnvironmentConfigurationListener>();
	}
	
	/**
	 * 
	 * @param listener
	 */
	public void subscribe(EnvironmentConfigurationListener listener) {
		synchronized (this.listeners) {
			this.listeners.add(listener);
		}
	}
	
	/**
	 * 
	 */
	protected void publish() {
		// check subscribers
		synchronized (this.listeners) {
			for (EnvironmentConfigurationListener listener : this.listeners) {
				// update listener
				listener.update();
			}
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public abstract Model getModel();
}
