package se.mah.elis.external.water.beans;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class WaterDataPointBean {

	@XmlElement
	public String timestamp;
	
	@XmlElement
	public String humanReadableTimestamp;
	
	@XmlElement
	public float volume;
}
