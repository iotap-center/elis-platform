package se.mah.elis.external.energy.beans;

import java.util.List;
import java.util.UUID;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import se.mah.elis.external.beans.PeriodicityBean;

@XmlRootElement
public class EnergyBean {

	@XmlElement
	public UUID user;

	@XmlElement
	public UUID device;

	@XmlElement
	public UUID deviceset;
	
	@XmlElement
	public PeriodicityBean period;
	
	@XmlElement
	public List<EnergyDataBean> samples;
	
	@XmlElement
	public EnergySummaryBean summary;
	
	public EnergyBean() {
		period = new PeriodicityBean();
	}
	
	public String toString() {
		String uuid = "";
		if (user != null) {
			uuid = user.toString();
		} else if (device != null) {
			uuid = device.toString();
		} else if (deviceset != null) {
			uuid = deviceset.toString();
		}
		
		String out = uuid + ", " + period + ", [";
		boolean more = false;
		
		for (EnergyDataBean bean : samples) {
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
