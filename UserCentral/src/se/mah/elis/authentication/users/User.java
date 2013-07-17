package se.mah.elis.authentication.users;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class User {
	public String userName;
	public String firstName;
	public String lastName;
	
	@Override
	public boolean equals(Object otherUser) {
		if (!(otherUser instanceof User))
			return false;
		return this.userName.equalsIgnoreCase(((User)otherUser).userName);
	}
}
