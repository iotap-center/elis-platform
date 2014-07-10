package se.mah.elis.external.devices.beans;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class DeviceSetBean {
	
	@XmlElement
	public String user;
	
	@XmlElement
	public String device;
	
	@XmlElement
	public String deviceset;
	
	@XmlElement(required = true)
	public List<DeviceBean> devices;
}
