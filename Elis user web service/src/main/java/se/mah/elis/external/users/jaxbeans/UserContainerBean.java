package se.mah.elis.external.users.jaxbeans;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class UserContainerBean {
	
	@XmlElement
	public PlatformUserBean[] userList;
	
	@XmlElement
	public PlatformUserBean user;

}
