package se.mah.elis.external.water.beans;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 
 * 
 * @author "Johan Holmberg, Malmö University"
 * @since 1.0
 */
@XmlRootElement
public class WaterDataPointBean {

	/**
	 * A timestamp, denoting when the data point was sampled.
	 * 
	 * @since 1.0
	 */
	@XmlElement
	public long timestamp;
	
	/**
	 * The timestamp again, but in a human-readable version.
	 * 
	 * @since 1.0
	 */
	@XmlElement
	public String humanReadableTimestamp;
	
	/**
	 * The measured volume of water.
	 * 
	 * @since 1.0
	 */
	@XmlElement
	public float volume;
}
