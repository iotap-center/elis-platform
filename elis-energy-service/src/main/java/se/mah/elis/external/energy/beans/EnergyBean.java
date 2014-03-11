package se.mah.elis.external.energy.beans;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class EnergyBean {

	@XmlElement
	public String puid;
	
	@XmlElement
	public String period;
	
	@XmlElement
	public List<EnergyDeviceBean> devices;
	
	@XmlElement
	public EnergySummaryBean summary;
}
