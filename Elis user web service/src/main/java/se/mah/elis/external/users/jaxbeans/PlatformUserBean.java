package se.mah.elis.external.users.jaxbeans;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Encapsulates a JSON query as a JAX bean.
 * 
 * @author "Johan Holmberg, Malmö University"
 * @since 1.0
 */
@XmlRootElement
public class PlatformUserBean {
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
