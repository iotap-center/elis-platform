package se.mah.elis.services.qsdriver.internal;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class User {

	public String id;
	public String username;
	public String firstname;
	public String lastname;
	public String email;
	public List<GatewayUsers> gatewayUsers;
}
