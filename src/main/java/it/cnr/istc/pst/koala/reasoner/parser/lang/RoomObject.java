package it.cnr.istc.pst.koala.reasoner.parser.lang;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author alessandro
 *
 */
public class RoomObject 
{
	private String id;
	private String type;
	private List<Sensor> sensors;

	/**
	 * 
	 * @param id
	 * @param type
	 */
	public RoomObject(String id, String type) {
		this.id = id;
		this.type = type;
		this.sensors = new ArrayList<Sensor>();
	}
	
	public String getId() {
		return id;
	}
	
	public List<Sensor> getSensors() {
		return new ArrayList<Sensor>(sensors);
	}
	
	public String getType() {
		return type;
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
		RoomObject other = (RoomObject) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "[RoomObject id= " + this.id + " type= " + this.type + "]";
	}
}
