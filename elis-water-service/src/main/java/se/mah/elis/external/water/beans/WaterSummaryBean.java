package se.mah.elis.external.water.beans;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class WaterSummaryBean {

	@XmlElement
	public float totalVolume;
}
