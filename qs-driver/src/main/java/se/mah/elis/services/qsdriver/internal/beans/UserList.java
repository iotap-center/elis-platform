package se.mah.elis.services.qsdriver.internal.beans;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class UserList {
	public List<User> users;
}
