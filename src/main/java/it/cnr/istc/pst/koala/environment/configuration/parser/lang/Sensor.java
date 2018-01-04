package it.cnr.istc.pst.koala.environment.configuration.parser.lang;

/**
 * 
 * @author alessandro
 *
 */
public class Sensor 
{	
	private String id;
	private String type;
	private SensorState state;
	
	/**
	 * 
	 * @param id
	 * @param type
	 * @param state
	 */
	public Sensor(String id, String type, SensorState state) {
		this.id = id;
		this.type = type;
		this.state = state;
	}
	
	public String getId() {
		return id;
	}
	
	public String getType() {
		return type;
	}
	
	public SensorState getState() {
		return state;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Sensor other = (Sensor) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	/**
	 * 
	 */
	@Override
	public String toString() {
		return "[Sensor id= " + this.id + " type= " + this.type + "]";
	}
}
