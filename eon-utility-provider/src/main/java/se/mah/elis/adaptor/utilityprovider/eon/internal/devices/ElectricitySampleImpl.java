package se.mah.elis.adaptor.utilityprovider.eon.internal.devices;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.json.simple.parser.ParseException;

import se.mah.elis.adaptor.utilityprovider.eon.internal.EonParser;
import se.mah.elis.auxiliaries.data.ElectricitySample;

public class ElectricitySampleImpl implements ElectricitySample{
	
	private double currentKwh;
	
	public ElectricitySampleImpl(double sample){
		currentKwh = sample;
	}

	@Override
	public int getSampleLength() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Date getSampleTimestamp() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getTraversableMethods() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double getCurrentCurrent() {
		return 0;
	}

	@Override
	public double getCurrentVoltage() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getCurrentPower() {
		return getPowerInWatts();
	}

	@Override
	public double getMaxCurrent() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getMaxVoltage() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getMaxPower() {
		return getPowerInWatts();
	}

	@Override
	public double getMinCurrent() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getMinVoltage() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getMinPower() {
		return getPowerInWatts();
	}


	@Override
	public double getMeanCurrent() {
		return 0;
	}

	@Override
	public double getMeanVoltage() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getMeanPower() {
		return getPowerInWatts();
	}

	@Override
	public double getTotalEnergyUsageInJoules() {
		double joules = currentKwh*3600000;
		return joules;
	}

	@Override
	public double getTotalEnergyUsageInWh() {
		double wh = currentKwh*1000;
		return wh;
	}

	private double getPowerInWatts() {
		int timeInHours = 1;
		double watts = 1000*currentKwh/timeInHours;
		return watts;
	}
}
