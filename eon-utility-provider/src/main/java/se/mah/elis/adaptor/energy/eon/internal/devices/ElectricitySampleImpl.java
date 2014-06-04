package se.mah.elis.adaptor.energy.eon.internal.devices;

import java.util.List;
import java.util.Properties;
import java.util.UUID;

import org.joda.time.DateTime;
import se.mah.elis.data.ElectricitySample;
import se.mah.elis.data.OrderedProperties;

/**
 * 
 * An implementation of an electricity sample fitted for the E.On API. 
 * 
 * @author Marcus Ljungblad
 * @since 1.0
 * @version 1.0
 *
 */
public class ElectricitySampleImpl implements ElectricitySample {
	
	private static final long serialVersionUID = 9035743168231203310L;
	private UUID dataid;
	private UUID ownerid;
	private double measuredValue = 0;
	private double maxCurrent = 0;
	private double minCurrent = 0;
	private double meanCurrent = 0;
	private double currentCurrent = 0;
	private double maxVoltage = 0;
	private double minVoltage = 0;
	private double meanVoltage = 0;
	private double currentVoltage = 0;
	private double maxPower = 0;
	private double minPower = 0;
	private double meanPower = 0;
	private double currentPower = 0;
	private long sampleLength = 0;
	private DateTime sampled;
	private DateTime created = DateTime.now();
	
	public ElectricitySampleImpl(double sample){
		sampled = created;
		measuredValue = sample;
	}

	public ElectricitySampleImpl(double value, DateTime sampleTime) {
		sampled = sampleTime;
		created = sampleTime;
		measuredValue = value;
	}

	@Override
	public long getSampleLength() {
		return sampleLength;
	}

	/**
	 * Returns the DateTime of when the sample was created. For historic 
	 * samples this may be well in the past. 
	 * 
	 * @since 1.0
	 */
	@Override
	public DateTime getSampleTimestamp() {
		return sampled;
	}

	@Override
	public List<String> getTraversableMethods() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double getCurrentCurrent() {
		return currentCurrent;
	}

	@Override
	public double getCurrentVoltage() {
		return currentVoltage;
	}

	@Override
	public double getCurrentPower() {
		return getPowerInWatts();
	}

	@Override
	public double getMaxCurrent() {
		return maxCurrent;
	}

	@Override
	public double getMaxVoltage() {
		return maxVoltage;
	}

	@Override
	public double getMaxPower() {
		return getPowerInWatts();
	}

	@Override
	public double getMinCurrent() {
		return minCurrent;
	}

	@Override
	public double getMinVoltage() {
		return minVoltage;
	}

	@Override
	public double getMinPower() {
		return getPowerInWatts();
	}


	@Override
	public double getMeanCurrent() {
		return meanCurrent;
	}

	@Override
	public double getMeanVoltage() {
		return meanVoltage;
	}

	@Override
	public double getMeanPower() {
		return getPowerInWatts();
	}

	@Override
	public double getTotalEnergyUsageInJoules() {
		double joules = measuredValue*3600000;
		return joules;
	}

	@Override
	public double getTotalEnergyUsageInWh() {
		double wh = measuredValue*1000;
		return wh;
	}

	private double getPowerInWatts() {
		double watts = 1000*measuredValue;
		return watts;
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
		
		if (dataid != null)
			props.put("dataid", UUID.randomUUID());
		if (ownerid != null)
			props.put("ownerid", UUID.randomUUID());
		props.put("created", created);
		props.put("sampled", sampled);
		props.put("sample_length", sampleLength);
		props.put("currentKwh", measuredValue);
		props.put("max_current", maxCurrent);
		props.put("min_current", minCurrent);
		props.put("mean_current", meanCurrent);
		props.put("current_current", currentCurrent);
		props.put("max_voltage", maxVoltage);
		props.put("min_voltage", minVoltage);
		props.put("mean_voltage", meanVoltage);
		props.put("current_voltage", currentVoltage);
		props.put("max_power", maxPower);
		props.put("min_power", minPower);
		props.put("mean_power", meanPower);
		props.put("currentPower", currentPower);
		
		return props;
	}

	@Override
	public OrderedProperties getPropertiesTemplate() {
		OrderedProperties props = new OrderedProperties();
		
		props.put("dataid", UUID.randomUUID());
		props.put("ownerid", UUID.randomUUID());
		props.put("created", created);
		props.put("sampled", sampled);
		props.put("sample_length", sampleLength);
		props.put("currentKwh", measuredValue);
		props.put("max_current", maxCurrent);
		props.put("min_current", minCurrent);
		props.put("mean_current", meanCurrent);
		props.put("current_current", currentCurrent);
		props.put("max_voltage", maxVoltage);
		props.put("min_voltage", minVoltage);
		props.put("mean_voltage", meanVoltage);
		props.put("current_voltage", currentVoltage);
		props.put("max_power", maxPower);
		props.put("min_power", minPower);
		props.put("mean_power", meanPower);
		props.put("currentPower", currentPower);
		
		return props;
	}

	@Override
	public void populate(Properties props) {
		dataid = (UUID) props.get("dataid");
		ownerid = (UUID) props.get("ownerid");
		created = (DateTime) props.get("created");
		sampled = (DateTime) props.get("sampled");
		sampleLength = (long) props.get("sample_length");
		measuredValue = (double) props.get("currentKwh");
		maxCurrent = (double) props.get("max_current");
		minCurrent = (double) props.get("min_current");
		meanCurrent = (double) props.get("mean_current");
		currentCurrent = (double) props.get("current_current");
		maxVoltage = (double) props.get("max_voltage");
		minVoltage = (double) props.get("min_voltage");
		meanVoltage = (double) props.get("mean_voltage");
		currentVoltage = (double) props.get("current_voltage");
		maxPower = (double) props.get("max_power");
		minPower = (double) props.get("min_power");
		meanPower = (double) props.get("mean_power");
		currentPower = (double) props.get("current_power");
	}

	@Override
	public DateTime created() {
		return created;
	}
	
	public void setSampleLength(long millis) {
		if (millis >= 0) {
			sampleLength = millis;
		}
	}
	
	public void setSamplingTime(DateTime instance) {
		sampled = instance;
	}
	
	@Override
	public String toString() {
		String stringed = null;
		
		if (dataid != null) {
			stringed = dataid.toString() + ": " + measuredValue;
		} else {
			stringed = "<no id>: " + measuredValue;
		}
		
		return stringed;
	}
}
