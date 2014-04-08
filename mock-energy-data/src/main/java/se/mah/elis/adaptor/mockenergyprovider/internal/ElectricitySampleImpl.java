package se.mah.elis.adaptor.mockenergyprovider.internal;

import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.UUID;

import org.joda.time.DateTime;

import se.mah.elis.data.ElectricitySample;
import se.mah.elis.data.OrderedProperties;

public class ElectricitySampleImpl implements ElectricitySample {
	private UUID dataid;
	private UUID ownerid;
	private long seed = 0;
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
	
	public ElectricitySampleImpl(long seed){
		sampled = created;
		this.seed = seed;
	}

	public ElectricitySampleImpl(long seed, DateTime sampleTime) {
		sampled = sampleTime;
		created = sampleTime;
		this.seed = seed;
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
		double joules = calcMockValue()*3600000;
		return joules;
	}

	@Override
	public double getTotalEnergyUsageInWh() {
		double wh = calcMockValue()*1000;
		return wh;
	}

	private double getPowerInWatts() {
		double watts = 1000*calcMockValue();
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
		props.put("seed", seed);
		
		return props;
	}

	@Override
	public OrderedProperties getPropertiesTemplate() {
		OrderedProperties props = new OrderedProperties();
		
		props.put("dataid", UUID.randomUUID());
		props.put("ownerid", UUID.randomUUID());
		props.put("created", created);
		props.put("sampled", sampled);
		props.put("seed", seed);
		
		return props;
	}

	@Override
	public void populate(Properties props) {
		dataid = (UUID) props.get("dataid");
		ownerid = (UUID) props.get("ownerid");
		created = (DateTime) props.get("created");
		sampled = (DateTime) props.get("sampled");
		seed = (long) props.get("seed");
	}

	@Override
	public DateTime created() {
		return created;
	}
	
	private double calcMockValue() {
		Random random = new Random(seed);
		int sample = random.nextInt();
		
		if (sample < 0) {
			sample = -sample;
		}
		if (sample > 1000000000) {
			sample = sample / 1000;
		}
		if (sample > 1000000) {
			sample = sample / 1000;
		}
		
		
		return sample / 1000;
	}
}
