package se.mah.elis.adaptor.water.mkb;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

import org.joda.time.DateTime;
import org.joda.time.Period;

import se.mah.elis.adaptor.water.mkb.data.WaterDataPoint;
import se.mah.elis.data.OrderedProperties;
import se.mah.elis.data.WaterSample;

public class MkbWaterSample implements WaterSample {

	private static final long serialVersionUID = 7438609549499091999L;
	private float volume;
	private UUID uuid;
	private DateTime sampleTimestamp;
	private int sampleLength;

	public MkbWaterSample(WaterDataPoint point) {
		this.volume = point.getValue();
		this.sampleTimestamp = point.getSampleDateTime();
		this.sampleLength = 0;
	}

	public MkbWaterSample(List<WaterDataPoint> points) {
		WaterDataPoint firstPoint = points.get(0);
		WaterDataPoint lastPoint = points.get(points.size() - 1);
		
		this.sampleLength = calculateLength(firstPoint, lastPoint);
		this.volume = calculateTotalVolume(firstPoint, lastPoint);
		this.sampleTimestamp = lastPoint.getSampleDateTime();
	}

	private float calculateTotalVolume(WaterDataPoint firstPoint,
			WaterDataPoint lastPoint) {
		return lastPoint.getValue() - firstPoint.getValue();
	}

	private int calculateLength(WaterDataPoint firstPoint,
			WaterDataPoint lastPoint) {
		Period period = new Period(firstPoint.getSampleDateTime(), 
				lastPoint.getSampleDateTime());
		return period.getMillis();
	}

	@Override
	public int getSampleLength() {
		return sampleLength;
	}

	@Override
	public DateTime getSampleTimestamp() {
		return sampleTimestamp;
	}

	@Override
	public List<String> getTraversableMethods() {
		List<String> methods = new ArrayList<>();
		methods.add("getVolume");
		return methods;
	}

	@Override
	public long getDataId() {
		// not used
		return 0;
	}

	@Override
	public UUID getUUID() {
		return uuid;
	}

	@Override
	public void setUUID(UUID uuid) {
		this.uuid = uuid;
	}

	@Override
	public void setUniqueUserId(int userId) {
		// not used
	}

	@Override
	public int getUniqueUserId() {
		// not used
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

	@Override
	public float getVolume() {
		return volume;
	}

}
