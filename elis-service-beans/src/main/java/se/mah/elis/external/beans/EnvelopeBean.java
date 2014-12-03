package se.mah.elis.external.beans;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * EnvelopeBean describes a wrapper for the responses produced by the Elis
 * platform.
 * 
 * @author "Johan Holmberg, Malmö University"
 * @since 1.0
 */
@XmlRootElement
public class EnvelopeBean {
	
	/**
	 * A HTTP status message, as defined by the W3C.
	 * 
	 * @since 1.0
	 */
	@XmlElement(required = true)
	public String status;
	
	/**
	 * A HTTP status code, as defined by the W3C.
	 * 
	 * @since 1.0
	 */
	@XmlElement(required = true)
	public int code;
	
	/**
	 * Any object that is to be sent to the requesting client. This should
	 * ideally be another bean, as it will be translated into JSON.
	 * 
	 * @since 1.0
	 */
	@XmlElement(required = true)
	public Object response;
	
	/**
	 * Initializes an EnvelopeBean object.
	 * 
	 * @since 1.0
	 */
	public EnvelopeBean () {
		response = new Object();
	}
}
