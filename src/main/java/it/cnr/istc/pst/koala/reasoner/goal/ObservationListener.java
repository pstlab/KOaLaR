package it.cnr.istc.pst.koala.reasoner.goal;

import java.util.List;

import it.cnr.istc.pst.koala.reasoner.observation.ObservationReasonerUpdate;

/**
 * 
 * @author alessandroumbrico
 *
 */
public interface ObservationListener 
{
	/**
	 * 
	 * @param notifications
	 */
	public void update(List<ObservationReasonerUpdate> notifications);
}
