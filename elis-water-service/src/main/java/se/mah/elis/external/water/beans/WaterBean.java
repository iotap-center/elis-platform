package se.mah.elis.external.water.beans;

import java.util.List;
import java.util.UUID;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import se.mah.elis.external.beans.PeriodicityBean;

/**
 * WaterBean is a container for water-related data results.
 * 
 * @author "Johan Holmberg, Malmö University"
 * @since 1.0
 */
@XmlRootElement
public class WaterBean {

	/**
	 * The user that owns this data set.
	 * 
	 * @since 1.0
	 */
	@XmlElement
	public UUID user;

	/**
	 * The device that collected this data set.
	 * 
	 * @since 1.0
	 */
	@XmlElement
	public UUID device;

	/**
	 * The device set that produced this data set.
	 * 
	 * @since 1.0
	 */
	@XmlElement
	public UUID deviceset;
	
	/**
	 * The periodicity bean associated with this data.
	 * 
	 * @since 1.0
	 */
	@XmlElement
	public PeriodicityBean period;
	
	/**
	 * The data samples that make up this data set.
	 * 
	 * @since 1.0
	 */
	@XmlElement
	public List<WaterDataPointBean> samples;
	
	/**
	 * The summarization of this data set.
	 * 
	 * @since 1.0
	 */
	@XmlElement
	public WaterSummaryBean summary;
		
}
