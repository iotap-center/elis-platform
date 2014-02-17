package se.mah.elis.services.rest.demo;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/test")
public class Demo {
	
	@GET
	public String helloWorld() {
		return "Hello Elis!";
	}
}
