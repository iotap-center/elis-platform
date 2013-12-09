package se.mah.elis.adaptor.utilityprovider.eon.devices.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import se.mah.elis.adaptor.utilityprovider.eon.internal.devices.TemperatureDataImpl;

public class TemperatureDataImplTest {
	
	TemperatureDataImpl tempDataImpl;
	
	float celsius = 25.0f;
	float fahrenheit = 77f;
	float kelvin = 298.15f;
	float rankine = 536.67f;
	
	@Before
	public void setUp() { 
		tempDataImpl = new TemperatureDataImpl(celsius);
	}
	
	@Test
	public void testGetCelsius(){
		assertEquals(celsius, tempDataImpl.getCelsius(), 0.01);
	}
	
	@Test
	public void testGetFahrenheit() {
		assertEquals(fahrenheit, tempDataImpl.getFahrenheit(), 0.01);
	}

	@Test
	public void testGetKelvin() {
		assertEquals(kelvin, tempDataImpl.getKelvin(), 0.01);
	}
	
	@Test
	public void testGetRankine() {
		assertEquals(rankine, tempDataImpl.getRankine(), 0.01);
	}
}
