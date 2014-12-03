package se.mah.elis.external.beans;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.joda.time.DateTime;

import se.mah.elis.external.beans.helpers.DateTimeAdapter;

/**
 * PeriodicityBean is used to describe data related to periodicity, timestamps,
 * and durations.
 * 
 * @author "Johan Holmberg, Malmö University"
 * @since 1.0
 */
@XmlRootElement
public class PeriodicityBean {
	
	/**
	 * A string describing the periodicity, which with the data is gathered.
	 * Examples include "daily", "hourly", and "bi-weekly".
	 * 
	 * @since 1.0
	 */
	@XmlElement(required = true)
	public String periodicity;
	
	/**
	 * Tells when the data associated with this bean was sampled. This field is
	 * usually not used together with the from and to fields.
	 * 
	 * @since 1.0
	 */
	@XmlJavaTypeAdapter(DateTimeAdapter.class)
	@XmlElement
	public DateTime when;
	
	/**
	 * Tells when data sampling associated with this bean was started. Usually
	 * not used together with the when field.
	 * 
	 * @since 1.0
	 */
	@XmlJavaTypeAdapter(DateTimeAdapter.class)
	@XmlElement
	public DateTime from;
	
	/**
	 * Tells when data sampling associated with this bean was stopped. Usually
	 * not used together with the when field.
	 * 
	 * @since 1.0
	 */
	@XmlJavaTypeAdapter(DateTimeAdapter.class)
	@XmlElement
	public DateTime to;
	
	/**
	 * Creates an instance of PeriodicityBean.
	 * 
	 * @since 1.0
	 */
	public PeriodicityBean() {
		when = from = to = null;
		periodicity = "";
	}
}
