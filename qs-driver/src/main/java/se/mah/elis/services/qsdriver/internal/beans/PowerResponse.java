package se.mah.elis.services.qsdriver.internal.beans;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class PowerResponse {
	
	public String status;
	public int code;
	public Power response;
	
	public PowerResponse() {
		response = new Power();
	}

}
