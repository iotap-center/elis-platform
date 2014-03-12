package se.mah.elis.external.energy.beans;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class EnergyDeviceBean {

	@XmlElement
	public String deviceId;
	
	@XmlElement
	public String deviceName;
	
	@XmlElement
	public List<EnergyDataBean> data;
	
}
