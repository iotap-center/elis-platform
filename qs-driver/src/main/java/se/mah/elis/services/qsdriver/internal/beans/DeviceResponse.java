package se.mah.elis.services.qsdriver.internal.beans;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class DeviceResponse {

	public String status;
	public int code;
	public Device response;
	
	public DeviceResponse() {
		response = new Device();
	}

}
