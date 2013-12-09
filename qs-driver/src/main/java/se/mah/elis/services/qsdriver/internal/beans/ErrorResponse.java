package se.mah.elis.services.qsdriver.internal.beans;

import javax.ws.rs.core.Response;

public class ErrorResponse {

	public String errorType = "Conflict";
	public String errorDetail = "The proposed URL already exists on this server.";
	public String status = "Error";
	public int code = 409;
	public User response;
	
	public ErrorResponse() {
		response = new User();
	}
	
}
