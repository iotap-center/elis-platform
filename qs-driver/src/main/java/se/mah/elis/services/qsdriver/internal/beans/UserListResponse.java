package se.mah.elis.services.qsdriver.internal.beans;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class UserListResponse {

	public String status;
	public int code;
	public UserList response;
	
	public UserListResponse() {
		response = new UserList();
	}

}
