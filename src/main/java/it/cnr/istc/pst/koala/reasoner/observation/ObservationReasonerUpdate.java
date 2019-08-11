package it.cnr.istc.pst.koala.reasoner.observation;

/**
 * 
 * @author alessandroumbrico
 *
 */
public class ObservationReasonerUpdate 
{
	private String eventId;
	private String eventType;
	private ObservationUpdateCategory category;
	private String concernedFeatureId;
	
	/**
	 * 
	 * @param eventId
	 * @param eventType
	 * @param concernedFeatureId
	 * @param category
	 */
	protected ObservationReasonerUpdate(String eventId, String eventType, String concernedFeatureId, ObservationUpdateCategory category) {
		this.eventId = eventId;
		this.eventType = eventType;
		this.category = category;
		this.concernedFeatureId = concernedFeatureId;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getEventId() {
		return eventId;
	}
	
	/**
	 * 
	 * @return
	 */
	public ObservationUpdateCategory getCategory() {
		return category;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getEventType() {
		return eventType;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getConcernedFeatureId() {
		return concernedFeatureId;
	}

	/**
	 * 
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((eventId == null) ? 0 : eventId.hashCode());
		return result;
	}

	/**
	 * 
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ObservationReasonerUpdate other = (ObservationReasonerUpdate) obj;
		if (eventId == null) {
			if (other.eventId != null)
				return false;
		} else if (!eventId.equals(other.eventId))
			return false;
		return true;
	}
	
	/**
	 * 
	 */
	@Override
	public String toString() {
		return "[Update eventId= \"" + this.eventId + "\", type=\"" + this.eventType + "\", category= \"" + this.category + "\"]";
	}
}
