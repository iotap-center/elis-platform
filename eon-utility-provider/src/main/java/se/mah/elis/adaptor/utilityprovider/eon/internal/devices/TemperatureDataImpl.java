package se.mah.elis.adaptor.utilityprovider.eon.internal.devices;

import se.mah.elis.auxiliaries.data.TemperatureData;

public class TemperatureDataImpl implements TemperatureData {
	
	private float celsius;

	public TemperatureDataImpl(float celsiusInput) {
		celsius = celsiusInput;
	}
	
	@Override
	public float getKelvin() {
		// TODO make conversion to Kelvin.
		return 0;
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
		// TODO make conversion to Rankine.
		return 0;
	}

}
