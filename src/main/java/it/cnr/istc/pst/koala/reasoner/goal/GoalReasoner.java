package it.cnr.istc.pst.koala.reasoner.goal;

import java.util.ArrayList;
import java.util.List;

import it.cnr.istc.pst.koala.reasoner.observation.ObservationReasoner;
import it.cnr.istc.pst.koala.reasoner.observation.ObservationReasonerUpdate;

/**
 * 
 * @author anacleto
 *
 */
public abstract class GoalReasoner implements ObservationListener
{
	protected ObservationReasoner environment;					// environment model at observation level
	
	private final List<ObservationReasonerUpdate> updates;		// list of cached notifications from observation reasoner
	protected Thread process;									// goal triggering background process
	
	/**
	 * 
	 */
	public GoalReasoner() {
		// set updates 
		this.updates = new ArrayList<ObservationReasonerUpdate>();
	}

	/**
	 * 
	 * @param env
	 * @throws Exception
	 */
	public void init(ObservationReasoner env) 
			throws Exception 
	{
		// set environment
		this.environment = env;
		this.environment.subscribe(this);
		
		// set wait flag
		synchronized (this.updates) {
			this.updates.clear();
			this.updates.notifyAll();
		}
		
		// check already running process
		if (this.process != null && this.process.isAlive()) {
			// stop process
			this.process.interrupt();
			this.process.wait();
		}
		
		// create background thread
		this.process = new Thread(new Runnable() {
			
			/**
			 * 
			 */
			public void run() 
			{
				boolean run = true;
				while (run)
				{
					try
					{
						// list of updates to handle
						List<ObservationReasonerUpdate> list = new ArrayList<ObservationReasonerUpdate>();
						// wait a signal
						synchronized (updates) {
							// check signal flag
							while (updates.isEmpty()) {
								// wait signal
								updates.wait();
							}

							// get updates
							list.addAll(updates);
							// clear 
							updates.clear();
							// release lock
							updates.notifyAll();
						}

						
						// do goal triggering if necessary
						doGoalTriggering(list);
					}
					catch (InterruptedException ex) {
						// stop goal triggering
						run = false;
					}
				}
			}
		});
		
		// complete initialization
		this.doInitialize(this.environment);
	}
	
	/*
	 * 
	 */
	public void start() {
		// check if initialize
		if (this.process != null && !this.process.isAlive()) {
			// start background process
			this.process.start();
		}
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	public void stop() 
			throws Exception 
	{
		// check if process is running
		if (this.process != null && this.process.isAlive()) {
			// interrupt process
			this.process.interrupt();
			this.process.join();
		}
	}
	
	/**
	 * 
	 */
	public void update(List<ObservationReasonerUpdate> notifications) 
	{
		// notify internal goal triggering process
		synchronized (this.updates) {
			// add all updates
			this.updates.addAll(notifications);
			// release lock
			this.updates.notifyAll();
		}
	}
	
	/**
	 * 
	 */
	protected abstract void doInitialize(ObservationReasoner env);
	
	/**
	 * 
	 */
	protected abstract void doGoalTriggering(List<ObservationReasonerUpdate> notifications);
}
