package it.cnr.istc.pst.koala.lang.dictionary;

/**
 * 
 * @author alessandroumbrico
 *
 */
public enum KoalaSensorDictionary 
{
	KOALA_PIR("http://pst.istc.cnr.it/ontologies/2017/1/koala#Pir"),
	
	KOALA_GAP("http://pst.istc.cnr.it/ontologies/2017/1/koala#Gap"),
	
	KOALA_SWITCH("http://pst.istc.cnr.it/ontologies/2017/1/koala#Switch"),
	
	KOALA_BATTERY("http://pst.istc.cnr.it/ontologies/2017/1/koala#BatterySensor"),
	
	KOALA_VOICE_COMMAND("http://pst.istc.cnr.it/ontologies/2017/1/koala#VoiceCommandListener"),
	
	KOALA_PHYSIOLOGICAL("http://pst.istc.cnr.it/ontologies/2017/1/koala#Physiological");
	
	private String uri;
	
	/**
	 * 
	 * @param uri
	 */
	private KoalaSensorDictionary(String uri) {
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
