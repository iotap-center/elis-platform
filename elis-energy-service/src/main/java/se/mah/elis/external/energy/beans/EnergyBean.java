package se.mah.elis.external.energy.beans;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import se.mah.elis.adaptor.device.api.entities.devices.Device;

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
	
	public String toString() {
		String out = puid + ", " + period + ", [";
		boolean more = false;
		
		for (EnergyDeviceBean bean : devices) {
			if (more) {
				out += ", ";
			} else {
				more = false;
			}
			out += "[" + bean.toString() + "]";
		}
		
		out += "]";
		
		if (summary != null) {
			out += ", " + summary.toString();
		}
		
		return out;
	}
}
