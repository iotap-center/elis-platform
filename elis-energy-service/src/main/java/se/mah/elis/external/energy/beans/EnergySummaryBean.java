package se.mah.elis.external.energy.beans;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class EnergySummaryBean {
	
	@XmlElement
	public double kwh;
	
	public String toString() {
		return kwh + "";
	}
}
