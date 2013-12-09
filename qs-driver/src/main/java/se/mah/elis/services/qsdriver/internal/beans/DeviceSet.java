package se.mah.elis.services.qsdriver.internal.beans;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class DeviceSet {
	public String id;
	public ArrayList<Device> devices; 
}
