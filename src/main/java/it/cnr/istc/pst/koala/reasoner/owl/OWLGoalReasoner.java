package it.cnr.istc.pst.koala.reasoner.owl;

import java.util.List;

import it.cnr.istc.pst.koala.reasoner.goal.GoalReasoner;
import it.cnr.istc.pst.koala.reasoner.observation.ObservationReasoner;
import it.cnr.istc.pst.koala.reasoner.observation.ObservationReasonerUpdate;

/**
 * 
 * @author anacleto
 *
 */
public class OWLGoalReasoner extends GoalReasoner
{
	private OWLModel kb;
	
	/**
	 * 
	 * @param ontologyFilePath
	 * @param ruleFilePath
	 */
	public OWLGoalReasoner(String ontologyFilePath, String ruleFilePath) {
		super();
		// create a knowledge-base instance
		this.kb = new OWLModel(ontologyFilePath, ruleFilePath);
	}

	/**
	 * 
	 */
	@Override
	protected void doInitialize(ObservationReasoner env) {
		// setup model
		this.kb.setup(env.getModel());
	}
	
	/**
	 * 
	 */
	@Override
	protected void doGoalTriggering(List<ObservationReasonerUpdate> notifications) 
	{
		// handle notifications
		for (ObservationReasonerUpdate n : notifications) {
			System.err.println("[GoalReasoner] Update received from observation reasoner\n"
					+ "\t- " + n);
		}
	}
	
}
