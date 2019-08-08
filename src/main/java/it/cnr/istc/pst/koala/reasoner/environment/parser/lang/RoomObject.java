package it.cnr.istc.pst.koala.reasoner.environment.parser.lang;

/**
 * 
 * @author alessandro
 *
 */
public class RoomObject 
{
	private String id;
	private String type;

	/**
	 * 
	 * @param id
	 * @param type
	 */
	public RoomObject(String id, String type) {
		this.id = id;
		this.type = type;
	}
	
	public String getId() {
		return id;
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
