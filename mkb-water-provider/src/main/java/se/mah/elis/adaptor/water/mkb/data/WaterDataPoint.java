package se.mah.elis.adaptor.water.mkb.data;

import org.joda.time.DateTime;

public class WaterDataPoint implements Comparable<WaterDataPoint> {
	
	private DateTime recordedDateTime;
	private float measuredValue;

	public WaterDataPoint(DateTime time, float value) {
		recordedDateTime = time;
		measuredValue = value;
	}
	
	public DateTime getRecordedDateTime() {
		return recordedDateTime;
	}
	
	public float getValue() {
		return measuredValue;
	}

	@Override
	public int compareTo(WaterDataPoint o) {
		return recordedDateTime.compareTo(o.recordedDateTime);
	}
	
}
