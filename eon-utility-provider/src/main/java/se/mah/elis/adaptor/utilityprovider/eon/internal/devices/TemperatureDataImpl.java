package se.mah.elis.adaptor.utilityprovider.eon.internal.devices;

import java.util.Properties;
import java.util.UUID;

import se.mah.elis.data.OrderedProperties;
import se.mah.elis.data.TemperatureData;

public class TemperatureDataImpl implements TemperatureData {

	private float celsius;

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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setUUID(UUID uuid) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setUniqueUserId(int userId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getUniqueUserId() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Properties getProperties() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OrderedProperties getPropertiesTemplate() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void populate(Properties props) {
		// TODO Auto-generated method stub
		
	}

}
