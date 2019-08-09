package it.cnr.istc.pst.koala.reasoner.environment.parser.xml;

/**
 * 
 * @author alessandroumbrico
 *
 */
public class Element 
{
	private String id;
	private String name;
	private String type;
	private Element partOf;
	
	/**
	 * 
	 * @param id
	 * @param name
	 * @param type
	 */
	protected Element(String id, String name, String type) {
		this.id = id;
		this.name = name;
		this.type = type;
		this.partOf = null;
	}
	
	/**
	 * 
	 * @param id
	 * @param name
	 * @param type
	 * @param element
	 */
	protected Element(String id, String name, String type, Element element) {
		this(id, name, type);
		this.partOf = element;
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
	public Element getPartOf() {
		return partOf;
	}
	
	/**
	 * 
	 * @param partOf
	 */
	public void setPartOf(Element partOf) {
		this.partOf = partOf;
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
		Element other = (Element) obj;
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
		return "[Element id=" + this.id + ", name= " +  this.name + ", type= \"" + this.type + "\"]";
	}
}
