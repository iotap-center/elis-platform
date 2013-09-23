package se.mah.elis.adaptor.utilityprovider.eon.devices.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import se.mah.elis.adaptor.utilityprovider.eon.internal.devices.TemperatureDataImpl;

public class TemperatureDataImplTest {
	
	float celsius = 25.0f;
	float fahrenheit = 77f;
	
	@Before
	public void setUp() { }
	
	@Test
	public void testGetCelsius(){
		TemperatureDataImpl tempDataImpl = new TemperatureDataImpl(celsius);
		assertEquals(celsius, tempDataImpl.getCelsius(), 0.01);
	}
	
	@Test
	public void testGetFahrenheit() {
		TemperatureDataImpl tempDataImpl = new TemperatureDataImpl(celsius);
		assertEquals(fahrenheit, tempDataImpl.getFahrenheit(), 0.01);
	}

	// TODO Make test for getKelvin()
	// TODO Make test for getRankine()
}
