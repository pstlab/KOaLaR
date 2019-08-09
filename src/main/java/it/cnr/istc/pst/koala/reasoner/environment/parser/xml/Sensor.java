package it.cnr.istc.pst.koala.reasoner.environment.parser.xml;

/**
 * 
 * @author alessandroumbrico
 *
 */
public class Sensor 
{
	private String id;
	private String name;
	private String type;
	private SensorState state;
	private Element owner;
	private Element target;
	
	/**
	 * 
	 * @param id
	 * @param name
	 * @param type
	 * @param state
	 * @param owner
	 * @param target
	 */
	protected Sensor(String id, String name, String type, SensorState state, Element owner, Element target) {
		this.id = id;
		this.name = name;
		this.type = type;
		this.state = state;
		this.owner = owner;
		this.target = target;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getType() {
		return type;
	}
	
	/**
	 * 
	 * @return
	 */
	public Element getOwner() {
		return owner;
	}
	
	/**
	 * 
	 * @return
	 */
	public SensorState getState() {
		return state;
	}
	
	/**
	 * 
	 * @return
	 */
	public Element getTarget() {
		return target;
	}
	
	
	
	/**
	 * 
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		Sensor other = (Sensor) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	/**
	 * 
	 */
	@Override
	public String toString() {
		return "[Sensor id=" + this.id + ", name= " +  this.name + ", type= \"" + this.type + "\"]";
	}
}
