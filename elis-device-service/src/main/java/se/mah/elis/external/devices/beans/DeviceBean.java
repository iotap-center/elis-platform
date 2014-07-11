package se.mah.elis.external.devices.beans;

import java.util.UUID;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class DeviceBean {

	@XmlElement
	public String name;
	
	@XmlElement
	public String description;
	
	@XmlElement
	public UUID id;
	
}
