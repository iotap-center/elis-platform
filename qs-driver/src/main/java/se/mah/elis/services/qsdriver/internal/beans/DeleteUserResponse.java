package se.mah.elis.services.qsdriver.internal.beans;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class DeleteUserResponse {

	public String status;
	public int code;
	public User response;
	
	public DeleteUserResponse() {
		response = new User(); 
	}

}
