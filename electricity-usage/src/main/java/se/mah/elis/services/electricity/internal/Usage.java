package se.mah.elis.services.electricity.internal;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name="usage")
public class Usage {
	
	@XmlElement(name="timestamp")
	private String timestamp;
	
	@XmlElement(name="deviceSetUsage")
	private List<DeviceSetResponse> deviceSetUsage;
	
	public Usage() {
		deviceSetUsage = new ArrayList<DeviceSetResponse>();
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public List<DeviceSetResponse> getDeviceSetUsage() {
		return deviceSetUsage;
	}

	public void setDeviceSetUsage(List<DeviceSetResponse> deviceSetUsage) {
		this.deviceSetUsage = deviceSetUsage;
	}
	
	public void addDeviceSetResponse(DeviceSetResponse response) {
		this.deviceSetUsage.add(response);
	}
	
}
