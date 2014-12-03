package se.mah.elis.external.control.beans;

import java.util.UUID;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class DimlevelBean {

	@XmlElement
	public UUID device;
	
	@XmlElement
	public UUID deviceset;

	@XmlElement
	public UUID user;

	@XmlElement
	@Min(0)
	@Max(100)
	public int dimlevel;
	
}
