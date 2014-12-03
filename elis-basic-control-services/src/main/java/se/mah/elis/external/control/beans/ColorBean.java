package se.mah.elis.external.control.beans;

import java.util.UUID;

import javax.validation.constraints.Pattern;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ColorBean {

	@XmlElement
	public UUID device;
	
	@XmlElement
	public UUID deviceset;

	@XmlElement
	public UUID user;

	@XmlElement
	@Pattern(regexp="^#([A-Fa-f0-9]{6})$")
	public String color;
	
}
