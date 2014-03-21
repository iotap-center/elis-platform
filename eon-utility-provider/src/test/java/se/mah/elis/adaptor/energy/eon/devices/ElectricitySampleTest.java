package se.mah.elis.adaptor.energy.eon.devices;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import se.mah.elis.adaptor.energy.eon.internal.devices.ElectricitySampleImpl;

public class ElectricitySampleTest {
	
	double CURRENT_KWH = 24.0;
	double WATT_HOUR = 24000;
	double WATTS= 24000;
	double JOULES = 86400000;
	double VOLTS;
	double AMPERE; 
	
	@Before
	public void setUp() { }
	
	@Test
	public void testGetCurrentPower(){
		ElectricitySampleImpl electricitySample = new ElectricitySampleImpl(CURRENT_KWH);
		assertEquals(WATTS, electricitySample.getCurrentPower(), 0.01);
	}
	
	@Test
	public void testGetTotalEnergyUsageInJoules(){
		ElectricitySampleImpl electricitySample = new ElectricitySampleImpl(CURRENT_KWH);
		assertEquals(JOULES, electricitySample.getTotalEnergyUsageInJoules(), 0.01);
	}
	
	@Test
	public void testGetTotalEnergyUsageInWh(){
		ElectricitySampleImpl electricitySample = new ElectricitySampleImpl(CURRENT_KWH);
		assertEquals(WATT_HOUR, electricitySample.getTotalEnergyUsageInWh(), 0.01);
	}
	
	@Test
	public void testGetMeanPower(){
		ElectricitySampleImpl electricitySample = new ElectricitySampleImpl(CURRENT_KWH);
		assertEquals(WATTS, electricitySample.getMeanPower(), 0.01);
	}
	
	@Test
	public void testGetMinPower(){
		ElectricitySampleImpl electricitySample = new ElectricitySampleImpl(CURRENT_KWH);
		assertEquals(WATTS, electricitySample.getMinPower(), 0.01);
	}
	
	@Test
	public void testGetMaxPower(){
		ElectricitySampleImpl electricitySample = new ElectricitySampleImpl(CURRENT_KWH);
		assertEquals(WATTS, electricitySample.getMaxPower(), 0.01);
	}
	
	


}
