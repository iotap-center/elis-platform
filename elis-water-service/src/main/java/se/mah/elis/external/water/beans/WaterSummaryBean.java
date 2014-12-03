package se.mah.elis.external.water.beans;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * WaterSummaryBean holds a summarized value from a set of water data.
 * 
 * @author "Johan Holmberg, Malmö University"
 * @since 1.0
 */
@XmlRootElement
public class WaterSummaryBean {

	/**
	 * The total volume of the water data set.
	 * 
	 * @since 1.0
	 */
	@XmlElement
	public float totalVolume;
}
