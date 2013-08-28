package se.mah.elis.services.qsdriver.internal;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class DeviceSet {
	public String id;
	public List<String> devices; 

}
