package se.mah.elis.external.energy.beans;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class EnergySummaryBean {

	@XmlElement
	public String deviceId;
	
	@XmlElement
	public float kwh;
}
