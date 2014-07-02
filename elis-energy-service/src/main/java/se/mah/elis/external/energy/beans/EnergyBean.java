package se.mah.elis.external.energy.beans;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import se.mah.elis.external.beans.PeriodicityBean;

@XmlRootElement
public class EnergyBean {

	@XmlElement
	public String user;

	@XmlElement
	public String device;

	@XmlElement
	public String deviceset;
	
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
		String uuid = user + device + deviceset;
		
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
