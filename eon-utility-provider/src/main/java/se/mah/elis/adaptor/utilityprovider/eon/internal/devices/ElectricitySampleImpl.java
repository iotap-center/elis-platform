package se.mah.elis.adaptor.utilityprovider.eon.internal.devices;

import java.util.List;
import java.util.Properties;
import java.util.UUID;

import org.joda.time.DateTime;

import se.mah.elis.data.ElectricitySample;
import se.mah.elis.data.OrderedProperties;

public class ElectricitySampleImpl implements ElectricitySample {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1127379585566280397L;
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
	public DateTime getSampleTimestamp() {
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
