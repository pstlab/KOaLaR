package it.cnr.istc.pst.koala.reasoner.owl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;

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
	private OWLModel kb;								// knowledge base model
	private Set<Statement> cache;						// cache of inferred statements about assistive tasks
	private PrintWriter writer;
	
	private long inferenceTime;							// count time spent doing inference
	private int inferenceCounter;						// count number of inferred goals
	
	/**
	 * 
	 * @param ontologyFilePath
	 * @param ruleFilePath
	 */
	public OWLGoalReasoner(String ontologyFilePath, String ruleFilePath) {
		super();
		// create a knowledge-base instance
		this.kb = new OWLModel(ontologyFilePath, ruleFilePath);
		// initialize cache
		this.cache = new HashSet<Statement>();
		this.inferenceTime = 0;
		this.inferenceCounter = 0;
	}

	/**
	 * 
	 */
	@Override
	protected void doInitialize(ObservationReasoner env) 
	{
		// set parameters
		this.inferenceTime = 0;
		this.inferenceCounter = 0;
		
		// current time 
		long now = System.currentTimeMillis();
		// setup model
		this.kb.setup(env.getModel());
		// get inference time
		long time = System.currentTimeMillis() - now;
		// update counter
		this.inferenceTime += time;
		
		// clear cache
		this.cache.clear();
		try
		{
			// create data file
			File dataFile = new File("data/goal_reasoner_" + System.currentTimeMillis() + ".csv");
			// create file and write the header of the CSV
			this.writer = new PrintWriter(new BufferedWriter(new FileWriter(dataFile)));
			// write header
			this.writer.print("timestamp;number of inferred goals;total inference time\n");
			this.writer.flush();
		}
		catch (IOException ex) {
			// set to null writer
			this.writer = null;
			// print error message 
			System.err.println("[GoalReasoner] Error opening writer buffer for data\n- message: " + ex.getMessage());
		}
	}
	
	/**
	 * 
	 */
	@Override
	protected void doGoalTriggering(List<ObservationReasonerUpdate> notifications) 
	{
		// get current time
		long timestamp = System.currentTimeMillis();
		// handle notifications
		for (ObservationReasonerUpdate notification : notifications) 
		{
			// current time 
			long now = System.currentTimeMillis();
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
			finally {
				// compute inference time
				long time = System.currentTimeMillis() - now;
				// update counter
				this.inferenceTime += time;
			}
		}
		
		try
		{
			// get the list of inferred events
			List<Statement> list = this.kb.getStatements(
					OWLNameSpace.SSN.getNs() + "isProxyFor");
			
			// check inferred events
			for (Statement s : list) 
			{
				// check if already notified
				if (!this.cache.contains(s))
				{
					// update inference counter
					this.inferenceCounter++;
					
					// get statement elements
					Resource subject = s.getSubject();
					
					
					// get type property
					Property pType = this.kb.getProperty(OWLNameSpace.RDF.getNs() + "type");
					// get event property statement
					Statement pStatement = subject.getProperty(pType);
					// get inferred event type
					Resource taskType = pStatement.getResource();
					
					// get the observable feature associated
					Property supports = this.kb.getProperty(OWLNameSpace.KOALA.getNs()+ "supports");
					// get concerns property statement
					Statement sStatement = subject.getProperty(supports);
					// get supported service
					Resource serviceType = sStatement.getResource();
					
					// get "explanation"
					Property precProperty = this.kb.getProperty(OWLNameSpace.KOALA.getNs() + "hasPrecondition");
					// get precondition
					Resource precondition = subject.getProperty(precProperty).getResource();
					
					// events triggering goal
					List<Resource> events = new ArrayList<Resource>();
					// get events enabling assistive task
					List<Statement> eStatements = this.kb.getStatements(
							precondition.getLocalName(), 
							OWLNameSpace.DUL.getNs()+ "includesEvent", 
							null);
					
					for (Statement es : eStatements) {
						// add resource
						events.add(es.getResource());
					}
					
					// print some data
					System.out.println("[GoalReasoner] Assistive task detected:\n"
							+ "- task: " + taskType + "\n"
							+ "- supported-service: " + serviceType + "\n"
							+ "- triggering-event(s): " + events + "\n");
					
					// add statement to the cache
					this.cache.add(s);
				}
			}
		}
		catch (Exception ex) {
			System.err.println("[GoalReasoner] Error while checking inferred assistive tasks\n- message= " + ex.getMessage());
		}
		finally 
		{
			// write data
			if (this.writer != null) 
			{
				// print inferred task type and service type
				this.writer.print(timestamp + ";" + this.inferenceCounter + ";" + this.inferenceTime + "\n");
				// write record
				this.writer.flush();
			}
		}
	}
	
	
	/**
	 * 
	 */
	@Override
	protected void doClose() 
	{
		// check if writer has been opened
		if (this.writer != null) {
			// close writer
			writer.close();
		}
		
	}
}
