package it.cnr.istc.pst.koala.reasoner.owl;

import java.util.List;

import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;

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
		for (ObservationReasonerUpdate notification : notifications) 
		{
			try
			{
				// event type
				String typeUri = notification.getEventType();
				// propagate information into the knowledge base
				Resource event = this.kb.createIndividual(typeUri);
				// get associated observable feature
				Resource observableFeature = this.kb.getResource(notification.getConcernedFeatureId());
				// get concerns property
				Property concerns = this.kb.getProperty(OWLNameSpace.KOALA.getNs() + "concerns");
				// assert property
				this.kb.assertProperty(
						event.getURI(), 
						concerns.getURI(), 
						observableFeature.getURI());

				System.out.println("[GoalReasoner] Update received from observation reasoner\n"
						+ "\t- " + event + "\n"
						+ "\t- asserting property " + event.getURI() + " " + concerns.getURI() + " " + observableFeature.getURI());
			}
			catch (Exception ex) {
				System.err.println("[GoalReasoner] Error while handling notification\n"
						+ "- notification: " + notification + "\n"
						+ "- message: " + ex.getMessage());
			}
		}
		
		
		/*
		 * TODO - retrieve (new) inferred assistive tasks 
		 */
		
		
	}
	
}
