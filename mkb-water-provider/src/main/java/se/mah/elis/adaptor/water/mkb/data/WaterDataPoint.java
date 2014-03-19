package se.mah.elis.adaptor.water.mkb.data;

import org.joda.time.DateTime;

/**
 * 
 * Describes raw data points as parsed from the text files provided from the Elvaco FTP. 
 * 
 * @author Marcus Ljungblad
 * @since 1.0
 * @version 1.0
 *
 */
public class WaterDataPoint implements Comparable<WaterDataPoint> {
	
	private DateTime recordedDateTime;
	private float measuredValue;

	public WaterDataPoint(DateTime time, float value) {
		recordedDateTime = time;
		measuredValue = value;
	}
	
	/**
	 * The date and time the data point was recorded. 
	 * 
	 * @return
	 */
	public DateTime getRecordedDateTime() {
		return recordedDateTime;
	}
	
	/**
	 * The measured value of the data point. 
	 * 
	 * @return
	 */
	public float getValue() {
		return measuredValue;
	}

	@Override
	public int compareTo(WaterDataPoint o) {
		return recordedDateTime.compareTo(o.recordedDateTime);
	}
	
}
