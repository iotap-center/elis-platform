package se.mah.elis.data.test.mocks;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import se.mah.elis.data.ElectricitySample;

public class ElectricitySampleMock implements ElectricitySample {

	private double testValue;
	
	public ElectricitySampleMock(double value) {
		testValue = value;
	}
	
	public ElectricitySampleMock() {
		testValue = 1;
	}

	public int getSampleLength() {
		return 100;
	}

	public Date getSampleTimestamp() {
		return new Date();
	}

	public List<String> getTraversableMethods() {
		ArrayList<String> list = new ArrayList<String>();
		
		list.add("getCurrentCurrent");
		list.add("getCurrentVoltage");
		list.add("getCurrentPower");
		list.add("getMaxCurrent");
		list.add("getMaxVoltage");
		list.add("getMaxPower");
		list.add("getMinCurrent");
		list.add("getMinVoltage");
		list.add("getMinPower");
		list.add("getMeanCurrent");
		list.add("getMeanVoltage");
		list.add("getMeanPower");
		list.add("getTotalEnergyUsageInJoules");
		list.add("getTotalEnergyUsageInWh");
		
		return list;
	}

	public double getCurrentCurrent() {
		return testValue;
	}

	public double getCurrentVoltage() {
		return testValue;
	}

	public double getCurrentPower() {
		return testValue;
	}

	public double getMaxCurrent() {
		return testValue;
	}

	public double getMaxVoltage() {
		return testValue;
	}

	public double getMaxPower() {
		return testValue * testValue;
	}

	public double getMinCurrent() {
		return testValue;
	}

	public double getMinVoltage() {
		return testValue;
	}

	public double getMinPower() {
		return testValue * testValue;
	}

	public double getMeanCurrent() {
		return testValue;
	}

	public double getMeanVoltage() {
		return testValue;
	}

	public double getMeanPower() {
		return testValue * testValue;
	}

	public double getTotalEnergyUsageInJoules() {
		return testValue * testValue * 10;
	}

	public double getTotalEnergyUsageInWh() {
		return testValue * testValue * 10;
	}

}
