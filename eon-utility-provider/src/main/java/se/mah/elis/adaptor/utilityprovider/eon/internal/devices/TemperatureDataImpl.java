package se.mah.elis.adaptor.utilityprovider.eon.internal.devices;

import java.util.Properties;
import java.util.UUID;

import se.mah.elis.data.OrderedProperties;
import se.mah.elis.data.TemperatureData;

public class TemperatureDataImpl implements TemperatureData {

	private static final long serialVersionUID = 512051193886837620L;
	private float celsius;
	private UUID uuid;

	public TemperatureDataImpl(float celsiusInput) {
		celsius = celsiusInput;
	}
	
	@Override
	public float getKelvin() {
		float kelvin = celsius+273.15f;
		return kelvin;
	}

	@Override
	public float getCelsius() {
		return celsius;
	}

	@Override
	public float getFahrenheit() {
		float farenheit = (celsius*1.8f)+32.0f;
		return farenheit;
	}

	@Override
	public float getRankine() {
		float rankine = (celsius*1.8000f)+491.67f;
		return rankine;
	}

	@Override
	public long getDataId() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public UUID getUUID() {
		return this.uuid;
	}

	@Override
	public void setUUID(UUID uuid) {
		this.uuid = uuid;
	}

	@Override
	public void setUniqueUserId(int userId) {
		
	}

	@Override
	public int getUniqueUserId() {
		return 0;
	}

	@Override
	public Properties getProperties() {
		return getPropertiesTemplate();
	}

	@Override
	public OrderedProperties getPropertiesTemplate() {	
		OrderedProperties props = new OrderedProperties();
		props.put("uuid", this.uuid);
		props.put("celsius", this.celsius);
		return null;
	}

	@Override
	public void populate(Properties props) {
		this.uuid = UUID.fromString((String) props.get("uuid"));
		this.celsius = (Float) props.get("celsius");
	}

}
