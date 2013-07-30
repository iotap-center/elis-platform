package se.mah.elis.services.electricity.internal;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class DeviceSetResponse {
	
	@XmlElement(name="devices")
	private List<DeviceResponse> devices;
	
	@XmlElement(name="totalUsage")
	private double totalUsage;
	
	@XmlElement(name="deviceSetId")
	private String deviceSetId;
	
	public String getDeviceSetId() {
		return deviceSetId;
	}

	public void setDeviceSetId(String deviceId) {
		this.deviceSetId = deviceId;
	}

	public double getTotalUsage() {
		return totalUsage;
	}

	public void setTotalUsage(double totalUsage) {
		this.totalUsage = totalUsage;
	}

	public void addDevice(DeviceResponse device) {
		devices.add(device);
	}

	public void setDeviceSetResponses(List<DeviceResponse> deviceResponses) {
		this.devices = deviceResponses;
	}
}
