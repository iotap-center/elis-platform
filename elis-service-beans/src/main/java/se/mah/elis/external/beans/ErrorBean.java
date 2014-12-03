package se.mah.elis.external.beans;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * 
 * 
 * @author "Johan Holmberg, Malmö University"
 * @since 1.0
 */
@XmlRootElement
@XmlType(propOrder = { "status", "code", "errorType", "errorDetail"})
public class ErrorBean extends EnvelopeBean {
	
	/**
	 * A HTTP status code, as defined by the W3C.
	 * 
	 * @since 1.0
	 */
	@XmlElement(required = true)
	public String errorType;
	
	/**
	 * A message describing the error.
	 * 
	 * @since 1.0
	 */
	@XmlElement(required = true)
	public String errorDetail;
}
