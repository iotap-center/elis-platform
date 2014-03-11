package se.mah.elis.external.water.beans;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class WaterDeviceBean {

	@XmlElement
	public String deviceId;
	
	@XmlElement
	public List<WaterDataPointBean> data;
}
