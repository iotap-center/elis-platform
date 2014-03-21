package se.mah.elis.external.energy.beans;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class EnergyDataBean {

	@XmlElement
	public String timestamp;
	
	@XmlElement
	public double kwh;

	@XmlElement
	public double watts;
	
}
