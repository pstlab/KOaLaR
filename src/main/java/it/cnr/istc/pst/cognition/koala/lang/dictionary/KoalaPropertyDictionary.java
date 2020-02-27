package it.cnr.istc.pst.cognition.koala.lang.dictionary;

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
	
	KOALA_BATTERY_LEVEL("http://pst.istc.cnr.it/ontologies/2017/1/koala#BatteryLevel");
	
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
	
	/**
	 * 
	 * @param propertyUri
	 * @return
	 */
	public static KoalaPropertyDictionary getPropertyByURI(String propertyUri)
	{
		if (propertyUri.equals(KOALA_TEMPERATURE.uri)) {
			return KOALA_TEMPERATURE;
		}
		else if (propertyUri.equals(KOALA_PRESENCE.uri)) {
			return KOALA_PRESENCE;
		}
		else if (propertyUri.equals(KOALA_LUMINOSITY.uri)) {
			return KOALA_LUMINOSITY;
		}
		else if (propertyUri.equals(KOALA_CONTACT.uri)) {
			return KOALA_CONTACT;
		}
		else if (propertyUri.equals(KOALA_ENERGY.uri)) {
			return KOALA_ENERGY;
		}
		else if (propertyUri.equals(KOALA_BODY_TEMPERATURE.uri)) {
			return KOALA_BODY_TEMPERATURE;
		}
		else if (propertyUri.equals(KOALA_BODY_WEIGHT.uri)) {
			return KOALA_BODY_WEIGHT;
		}
		else if (propertyUri.equals(KOALA_BLOOD_SUGAR.uri)) {
			return KOALA_BLOOD_SUGAR;
		}
		else if (propertyUri.equals(KOALA_BLOOD_PRESSURE.uri)) {
			return KOALA_BLOOD_PRESSURE;
		}
		else if (propertyUri.equals(KOALA_HEART_RATE.uri)) {
			return KOALA_HEART_RATE;
		}
		else if (propertyUri.equals(KOALA_OXIMETRY.uri)) {
			return KOALA_OXIMETRY;
		}
		else if (propertyUri.equals(KOALA_VOICE_COMMAND.uri)) {
			return KOALA_VOICE_COMMAND;
		}
		else if (propertyUri.equals(KOALA_BATTERY_LEVEL.uri)) {
			return KOALA_BATTERY_LEVEL;
		}
		else {
			// unknown 
			throw new RuntimeException("Unknown property URI " + propertyUri + "\n");
		}
	}
}
