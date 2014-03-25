package se.mah.elis.adaptor.water.mkb.data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;

/**
 * 
 * Implementation to query water data as provided by the {@link WaterDataLoader} 
 * and reached via the {@link WaterDataService}. 
 * 
 * @author Marcus Ljungblad
 * @since 1.0
 * @version 1.0
 *
 */
public class WaterData {
	
	private Map<String, List<WaterDataPoint>> userDataMap;
	
	public WaterData() {
		userDataMap = new HashMap<>();
	}
	
	/**
	 * Sets the water data measurement points. 
	 *  
	 * @param map
	 */
	public void setWaterData(Map<String, List<WaterDataPoint>> map) {
		userDataMap = map;
	}
	
	/**
	 * Retrieve a range of data points for a specific meter. 
	 * 
	 * @param meterId
	 * @param start
	 * @param end
	 * @return
	 */
	public List<WaterDataPoint> getRange(String meterId, DateTime start, DateTime end) {
		if (userDataMap.containsKey(meterId))
			return getRangeInSamples(meterId, userDataMap.get(meterId), start, end);
		return null;
	}
	
	private List<WaterDataPoint> getRangeInSamples(String meterId, List<WaterDataPoint> list, 
			DateTime start, DateTime end) {
		int listsize = list.size();
		int startIndex = 0;
		int stopIndex = listsize;
		
		startIndex = findStartIndex(meterId, list, start, listsize);
		stopIndex = findStopIndex(list, end, listsize, startIndex);
		
		if (getLastDate(meterId).equals(end))
			stopIndex += 1; // include last element
		
		return list.subList(startIndex, stopIndex);
		
	}

	private int findStopIndex(List<WaterDataPoint> list, DateTime end, int listsize,
			int startIndex) {
		int stopIndex = 0;
		
		for (int i = startIndex + 1; i < listsize; i++) {
			DateTime sample = list.get(i).getRecordedDateTime();
			if (sample.isBefore(end) || sample.equals(end))
				stopIndex = i;
		}
		
		return stopIndex;
	}

	private int findStartIndex(String meterId, List<WaterDataPoint> list,
			DateTime start, int listsize) {
		int startIndex = 0;
		
		if (start.isBefore(getEarliestDate(meterId))) {
			startIndex = 0;
		} else {
			for (int i = 0; i < listsize; i++) {
				DateTime sample = list.get(i).getRecordedDateTime();
				if (sample.isBefore(start) || sample.equals(start))
					startIndex = i;
				else {
					break; // don't bother going through the rest
				}
			}
		}

		return startIndex;
	}

	/**
	 * Retrieve a range of water meter data points from a specific date up 
	 * until the last recorded data point. 
	 * 
	 * @param meterId
	 * @param fromDate
	 * @return
	 */
	public List<WaterDataPoint> getRange(String meterId, DateTime fromDate) {
		return getRange(meterId, fromDate, getLastDate(meterId));
	}
	
	/**
	 * Retrieve all values attached to a specific meter. 
	 * 
	 * @param meterId
	 * @return
	 */
	public List<WaterDataPoint> getAllValues(String meterId) {
		if (userDataMap.containsKey(meterId))
			return userDataMap.get(meterId);
		return null;
	}
	
	/**
	 * Get the {@link DateTime} for the first registered water meter data point. 
	 * 
	 * @param meterId
	 * @return
	 */
	public DateTime getEarliestDate(String meterId) {
		if (userDataMap.containsKey(meterId))
			return userDataMap.get(meterId).get(0).getRecordedDateTime();
		return null;
	}
	
	/**
	 * Get the {@link DateTime} for the last registered water meter data point.  
	 * @param meterId
	 * @return
	 */
	public DateTime getLastDate(String meterId) {
		return getLatestSample(meterId).getRecordedDateTime();
	}
	
	/**
	 * Get the last available water meter data point for a specific meter. 
	 * 
	 * @param meterId
	 * @return
	 */
	public WaterDataPoint getLatestSample(String meterId) {
		if (userDataMap.containsKey(meterId)) {
			List<WaterDataPoint> samples = userDataMap.get(meterId);
			return samples.get(samples.size() - 1);
		}	
		return null;
	}
	
	/**
	 * Get the total number of samples 
	 * 
	 * @return number of samples
	 */
	protected int size() {
		return userDataMap.size();
	}
	
}
