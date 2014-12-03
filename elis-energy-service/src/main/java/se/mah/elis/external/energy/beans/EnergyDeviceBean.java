package se.mah.elis.external.energy.beans;

import java.util.List;
import java.util.UUID;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class EnergyDeviceBean {

	@XmlElement
	public UUID deviceId;
	
	@XmlElement
	public String deviceName;
	
	@XmlElement
	public List<EnergyDataBean> data;
	
	public String toString() {
		String out = deviceId + ", " + deviceName;
		
		return out;
	}
}
