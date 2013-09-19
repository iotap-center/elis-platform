package se.mah.elis.external.web.oauth;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class AccessTokenBean {
	
	@XmlElement(name="access_token")
	public String accessToken;
}
