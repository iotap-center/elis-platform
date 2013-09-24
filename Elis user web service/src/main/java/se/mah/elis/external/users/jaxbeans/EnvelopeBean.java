package se.mah.elis.external.users.jaxbeans;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class EnvelopeBean {
	
	@XmlElement(required = true)
	public String status;
	
	@XmlElement(required = true)
	public int code;
	
	@XmlElement(required = true)
	public Object response;
	
	public EnvelopeBean () {
		response = new Object();
	}
}
