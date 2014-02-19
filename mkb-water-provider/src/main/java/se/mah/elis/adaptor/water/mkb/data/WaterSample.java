package se.mah.elis.adaptor.water.mkb.data;

import org.joda.time.DateTime;

public class WaterSample implements Comparable<WaterSample> {
	
	private DateTime registeredDateTime;
	private float measuredValue;

	public WaterSample(DateTime time, float value) {
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
	public int compareTo(WaterSample o) {
		return registeredDateTime.compareTo(o.registeredDateTime);
	}
	
}
