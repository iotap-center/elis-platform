package se.mah.elis.services.qsdriver.internal.beans;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class User {
	@XmlElement(required = true)
	public String userId;
	
	@XmlElement(required = true)
	public String username;
	
	@XmlElement(required = true)
	public String password;
	
	@XmlElement
	public String firstName;
	
	@XmlElement
	public String lastName;
	
	@XmlElement
	public String email;
	
	@XmlElement
	public GatewayUserBean[] gatewayUsers;
	
	@XmlElement
	public GatewayUserBean gatewayUser;
}
