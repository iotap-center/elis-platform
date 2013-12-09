package se.mah.elis.services.qsdriver.internal.beans;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Device {
	public String id;
	public String name; 
	public List<String> actions;
	public Location location;
	public List<DeviceSet> devicesets;
}