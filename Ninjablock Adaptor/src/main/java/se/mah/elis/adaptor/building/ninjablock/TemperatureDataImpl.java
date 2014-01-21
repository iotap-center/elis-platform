package se.mah.elis.adaptor.building.ninjablock;

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

}
