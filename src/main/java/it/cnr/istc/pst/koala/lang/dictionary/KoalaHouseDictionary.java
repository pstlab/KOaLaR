package it.cnr.istc.pst.koala.lang.dictionary;

/**
 * 
 * @author alessandroumbrico
 *
 */
public enum KoalaHouseDictionary 
{
	KOALA_KITCHEN("http://pst.istc.cnr.it/ontologies/2017/1/koala#Kitchen"),
	
	KOALA_BEDROOM("http://pst.istc.cnr.it/ontologies/2017/1/koala#BedRoom"),
	
	KOALA_BATHROOM("http://pst.istc.cnr.it/ontologies/2017/1/koala#BathRoom"),
	
	KOALA_LIVINGROOM("http://pst.istc.cnr.it/ontologies/2017/1/koala#LivingRoom"),
	
	KOALA_CORRIDOR("http://pst.istc.cnr.it/ontologies/2017/1/koala#Corridor"),
	
	KOALA_CHAIR("http://pst.istc.cnr.it/ontologies/2017/1/koala#Chair"),
	
	KOALA_TV("http://pst.istc.cnr.it/ontologies/2017/1/koala#TV"),
	
	KOALA_BED("http://pst.istc.cnr.it/ontologies/2017/1/koala#Bed"),
	
	KOALA_SOFA("http://pst.istc.cnr.it/ontologies/2017/1/koala#Sofa"),
	
	KOALA_WINDOW("http://pst.istc.cnr.it/ontologies/2017/1/koala#Window"),
	
	KOALA_DOOR("http://pst.istc.cnr.it/ontologies/2017/1/koala#Door"),
	
	KOALA_PATIENT("http://pst.istc.cnr.it/ontologies/2017/1/koala#Patient"),
	
	KOALA_ASSISTIVE_ROBOT("http://pst.istc.cnr.it/ontologies/2017/1/koala#AssistiveRobot"),
	
	KOALA_HOUSE("http://pst.istc.cnr.it/ontologies/2017/1/koala#House");
	
	private String uri;
	
	/**
	 * 
	 * @param uri
	 */
	private KoalaHouseDictionary(String uri) {
		this.uri = uri;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getUri() {
		return uri;
	}
}
