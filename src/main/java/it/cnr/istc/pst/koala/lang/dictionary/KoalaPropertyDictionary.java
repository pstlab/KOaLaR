package it.cnr.istc.pst.koala.lang.dictionary;

/**
 * 
 * @author alessandroumbrico
 *
 */
public enum KoalaPropertyDictionary 
{
	KOALA_TEMPERATURE("http://pst.istc.cnr.it/ontologies/2017/1/koala#Temperature"),
	
	KOALA_PRESENCE("http://pst.istc.cnr.it/ontologies/2017/1/koala#Presence"),
	
	KOALA_LUMINOSITY("http://pst.istc.cnr.it/ontologies/2017/1/koala#Luminosity"),
	
	KOALA_CONTACT("http://pst.istc.cnr.it/ontologies/2017/1/koala#Contact"),
	
	KOALA_ENERGY("http://pst.istc.cnr.it/ontologies/2017/1/koala#Energy"),
	
	KOALA_BODY_TEMPERATURE("http://pst.istc.cnr.it/ontologies/2017/1/koala#BodyTemperature"),
	
	KOALA_BODY_WEIGHT("http://pst.istc.cnr.it/ontologies/2017/1/koala#BodyWeight"),
	
	KOALA_BLOOD_SUGAR("http://pst.istc.cnr.it/ontologies/2017/1/koala#BloodSugar"),
	
	KOALA_BLOOD_PRESSURE("http://pst.istc.cnr.it/ontologies/2017/1/koala#BloodPressure"),
	
	KOALA_HEART_RATE("http://pst.istc.cnr.it/ontologies/2017/1/koala#HeartRate"),
	
	KOALA_OXIMETRY("http://pst.istc.cnr.it/ontologies/2017/1/koala#Oximetry"),
	
	KOALA_VOICE_COMMAND("http://pst.istc.cnr.it/ontologies/2017/1/koala#VoiceCommand"),
	
	KOALA_BATTERY("http://pst.istc.cnr.it/ontologies/2017/1/koala#Battery");
	
	private String uri;
	
	/**
	 * 
	 * @param uri
	 */
	private KoalaPropertyDictionary(String uri) {
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
