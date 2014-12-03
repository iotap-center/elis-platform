package se.mah.elis.external.control.beans;

import java.util.UUID;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class PowerstateBean {
	
	@XmlElement
	public UUID device;
	
	@XmlElement
	public UUID deviceset;

	@XmlElement
	public UUID user;

	@XmlElement
	public boolean powerstate;
}
