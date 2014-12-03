package se.mah.elis.external.devices.beans;

import java.util.List;
import java.util.UUID;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class DeviceSetBean {
	
	@XmlElement
	public UUID user;
	
	@XmlElement
	public UUID device;
	
	@XmlElement
	public UUID deviceset;
	
	@XmlElement(required = true)
	public List<DeviceBean> devices;
}
