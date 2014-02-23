package se.mah.elis.adaptor.utilityprovider.eon.internal.devices;

import java.util.Properties;
import java.util.UUID;

import org.joda.time.DateTime;

import se.mah.elis.data.OrderedProperties;
import se.mah.elis.data.TemperatureData;

public class TemperatureDataImpl implements TemperatureData {

	private float celsius = 0;
	private UUID dataid;
	private UUID ownerid;
	private DateTime created = DateTime.now();

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
	public UUID getDataId() {
		return dataid;
	}

	@Override
	public void setDataId(UUID uuid) {
		dataid = uuid;
	}

	@Override
	public void setOwnerId(UUID userId) {
		ownerid = userId;
	}

	@Override
	public UUID getOwnerId() {
		return ownerid;
	}

	@Override
	public Properties getProperties() {
		OrderedProperties props = new OrderedProperties();
		
		props.put("dataid", dataid);
		props.put("ownerid", ownerid);
		props.put("created", created);
		props.put("celsius", celsius);
		
		return props;
	}

	@Override
	public OrderedProperties getPropertiesTemplate() {
		OrderedProperties props = new OrderedProperties();
		
		props.put("dataid", UUID.randomUUID());
		props.put("ownerid", UUID.randomUUID());
		props.put("created", created);
		props.put("celsius", celsius);
		
		return props;
	}

	@Override
	public void populate(Properties props) {
		dataid = (UUID) props.get("dataid");
		ownerid = (UUID) props.get("ownerid");
		created = (DateTime) props.get("created");
		celsius = (float) props.get("celsius");
	}

	@Override
	public DateTime created() {
		return created;
	}

}
