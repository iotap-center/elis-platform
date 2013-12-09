package se.mah.elis.services.qsdriver.internal.beans;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class GatewayUserBean {
	
	@XmlElement
	public String id;
	
	@XmlElement(required = true)
	public String serviceName;
	
	@XmlElement(required = true)
	public String serviceUserName;
	
	@XmlElement
	public String servicePassword;
}
