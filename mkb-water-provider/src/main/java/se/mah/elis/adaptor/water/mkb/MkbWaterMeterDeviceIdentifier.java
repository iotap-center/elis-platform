package se.mah.elis.adaptor.water.mkb;

import java.util.Properties;

import se.mah.elis.adaptor.device.api.data.DeviceIdentifier;
import se.mah.elis.data.OrderedProperties;

public class MkbWaterMeterDeviceIdentifier implements DeviceIdentifier {

	private String meterId;

	public MkbWaterMeterDeviceIdentifier(String id) {
		this.meterId = id;
	}
	
	public String getMeterId() {
		return meterId;
	}
	
	@Override 
	public String toString() {
		return meterId;
	}
	
	@Override
	public OrderedProperties getProperties() {
		OrderedProperties props = new OrderedProperties();
		props.put("meterId", this.meterId);
		return props;
	}

	@Override
	public OrderedProperties getPropertiesTemplate() {
		OrderedProperties props = new OrderedProperties();
		props.put("meterId", "32");
		return props;
	}

	@Override
	public void populate(Properties props) {
		this.meterId = (String) props.getProperty("meterId");
	}

}
