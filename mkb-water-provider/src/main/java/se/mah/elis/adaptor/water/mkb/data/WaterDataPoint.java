package se.mah.elis.adaptor.water.mkb.data;

import org.joda.time.DateTime;

public class WaterDataPoint implements Comparable<WaterDataPoint> {
	
	private DateTime registeredDateTime;
	private float measuredValue;

	public WaterDataPoint(DateTime time, float value) {
		registeredDateTime = time;
		measuredValue = value;
	}
	
	public DateTime getSampleDateTime() {
		return registeredDateTime;
	}
	
	public float getValue() {
		return measuredValue;
	}

	@Override
	public int compareTo(WaterDataPoint o) {
		return registeredDateTime.compareTo(o.registeredDateTime);
	}
	
}
