package se.mah.elis.external.water.beans;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import se.mah.elis.external.beans.PeriodicityBean;

@XmlRootElement
public class WaterBean {

	@XmlElement
	public String user;

	@XmlElement
	public String device;

	@XmlElement
	public String deviceset;
	
	@XmlElement
	public PeriodicityBean period;
	
	@XmlElement
	public List<WaterDataPointBean> samples;
	
	@XmlElement
	public WaterSummaryBean summary;
		
}
