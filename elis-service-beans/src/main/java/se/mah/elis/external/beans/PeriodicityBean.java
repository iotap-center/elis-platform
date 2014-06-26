package se.mah.elis.external.beans;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.joda.time.DateTime;

import se.mah.elis.external.beans.helpers.DateTimeAdapter;

@XmlRootElement
public class PeriodicityBean {
	
	@XmlElement(required = true)
	public String periodicity;
	
	@XmlJavaTypeAdapter(DateTimeAdapter.class)
	@XmlElement
	public DateTime when;
	
	@XmlJavaTypeAdapter(DateTimeAdapter.class)
	@XmlElement
	public DateTime from;
	
	@XmlJavaTypeAdapter(DateTimeAdapter.class)
	@XmlElement
	public DateTime to;
	
	public PeriodicityBean() {
		when = from = to = null;
		periodicity = "";
	}
}
