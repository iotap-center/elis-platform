package se.mah.declarative.service.bundle;

import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Deactivate;
import aQute.bnd.annotation.component.Reference;

// Java Annotations are used to generate the component XML declarations at build time.


@Component // Indicates that this class defines a component
public class UserComponent{

	private int id;
	
	@Activate
	public void start() {
		setId(5);
		System.out.println("id is "+getId());
	}
	
	@Deactivate
	public void stop() {
		System.out.println("bye");
	}
	
	@Reference
	public int getId() {
		return id;
	}

	@Reference
	public void setId(int id) {
		this.id = id;
		
	}
}
