package se.mah.elis.adaptor.building.ninjablock.beans;

public class NinjaEyeBean {
	
	private String DA;
	
	public NinjaEyeBean(String color) {
		DA = color;
	}

	public String returnValue () {
		return DA;
	}

}
