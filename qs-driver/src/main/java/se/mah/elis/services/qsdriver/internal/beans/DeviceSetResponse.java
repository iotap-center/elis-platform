package se.mah.elis.services.qsdriver.internal.beans;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class DeviceSetResponse {

	public String status;
	public int code;
	public DeviceSet response;
	
	public DeviceSetResponse() {
		response = new DeviceSet();
	}

}
