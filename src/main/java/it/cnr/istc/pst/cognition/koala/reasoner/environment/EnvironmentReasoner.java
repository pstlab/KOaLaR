package it.cnr.istc.pst.cognition.koala.reasoner.environment;

import java.util.ArrayList;
import java.util.List;

import org.apache.jena.rdf.model.Model;

/**
 * 
 * @author alessandro
 *
 */
public abstract class EnvironmentReasoner 
{
	private final List<EnvironmentListener> listeners;
	
	/**
	 * 
	 */
	public EnvironmentReasoner() {
		this.listeners = new ArrayList<EnvironmentListener>();
	}
	
	/**
	 * 
	 * @param listener
	 */
	public void subscribe(EnvironmentListener listener) {
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
			for (EnvironmentListener listener : this.listeners) {
				// update listener
				listener.update();
			}
		}
	}
	
	/**
	 * 
	 * @param envFilePath
	 */
	public abstract void init(String envFilePath);
	
	/**
	 * 
	 * @return
	 */
	public abstract Model getModel();
}
