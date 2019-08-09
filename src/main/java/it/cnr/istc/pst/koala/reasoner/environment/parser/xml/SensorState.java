package it.cnr.istc.pst.koala.reasoner.environment.parser.xml;

/**
 * 
 * @author anacleto
 *
 */
public enum SensorState 
{
	/**
	 * 
	 */
	OFF("0"),
	
	/**
	 * 
	 */
	ON("1"),
	
	/**
	 * 
	 */
	MAINENTANCE("2"),
	
	/**
	 * 
	 */
	FAILURE("3");
	
	private String code;
	
	/**
	 * 
	 * @param code
	 */
	private SensorState(String code) {
		this.code = code;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getCode() {
		return code;
	}
	
	/**
	 * 
	 * @param code
	 * @return
	 */
	public static SensorState getStateByCode(String code) 
	{
		// check code
		if (code.equals(OFF.code)) {
			return OFF;
		}
		else if (code.equals(ON.code)) {
			return ON;
		}
		else if (code.equals(MAINENTANCE.code)) {
			return MAINENTANCE;
		}
		else if (code.equals(FAILURE.code)) {
			return FAILURE;
		}
		else {
			throw new RuntimeException("Unknown sensor state code - \"" + code + "\"");
		}
	}
}
