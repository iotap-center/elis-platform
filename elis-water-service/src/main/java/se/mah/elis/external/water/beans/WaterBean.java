package se.mah.elis.external.water.beans;

import java.util.List;
import java.util.UUID;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import se.mah.elis.external.beans.PeriodicityBean;

@XmlRootElement
public class WaterBean {

	@XmlElement
	public UUID user;

	@XmlElement
	public UUID device;

	@XmlElement
	public UUID deviceset;
	
	@XmlElement
	public PeriodicityBean period;
	
	@XmlElement
	public List<WaterDataPointBean> samples;
	
	@XmlElement
	public WaterSummaryBean summary;
		
}
