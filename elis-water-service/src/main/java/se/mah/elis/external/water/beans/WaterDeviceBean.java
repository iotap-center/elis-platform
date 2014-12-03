package se.mah.elis.external.water.beans;

import java.util.List;
import java.util.UUID;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * WaterDeviceBean holds a set of water data points collected by a water
 * device.
 * 
 * @author "Johan Holmberg, Malmö University"
 * @since 1.0
 */
@XmlRootElement
public class WaterDeviceBean {

	/**
	 * The owner of the water metering device.
	 * 
	 * @since 1.0
	 */
	@XmlElement
	public UUID deviceId;
	
	/**
	 * The data point set.
	 * 
	 * @since 1.0
	 */
	@XmlElement
	public List<WaterDataPointBean> data;
}
