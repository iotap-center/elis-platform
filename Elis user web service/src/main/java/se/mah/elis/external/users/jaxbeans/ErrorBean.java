package se.mah.elis.external.users.jaxbeans;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement
@XmlType(propOrder = { "status", "code", "errorType", "errorDetail"})
public class ErrorBean extends EnvelopeBean {
	
	@XmlElement(required = true)
	public String errorType;
	
	@XmlElement(required = true)
	public String errorDetail;
}
