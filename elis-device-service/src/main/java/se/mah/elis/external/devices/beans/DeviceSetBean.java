package se.mah.elis.external.devices.beans;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class DeviceSetBean {
	
	@XmlElement(required = true)
	public List<DeviceBean> devices;
}
