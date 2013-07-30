package se.mah.elis.services.electricity.internal;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class UsageRequest {

	@XmlElement(name="deviceSets")
	public List<DeviceSetRequest> deviceSets;
	
	public UsageRequest() { 
		deviceSets = new ArrayList<DeviceSetRequest>();
	}
	
}
