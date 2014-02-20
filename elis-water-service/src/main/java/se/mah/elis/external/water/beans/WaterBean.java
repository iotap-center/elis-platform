package se.mah.elis.external.water.beans;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class WaterBean {

	@XmlElement
	public String meterId;
	
	@XmlElement
	public String period;
	
	@XmlElement
	public List<WaterDataPointBean> data;
		
}
