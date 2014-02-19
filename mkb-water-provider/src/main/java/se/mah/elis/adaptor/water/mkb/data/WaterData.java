package se.mah.elis.adaptor.water.mkb.data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;

public class WaterData {
	
	private Map<String, List<WaterSample>> userDataMap;
	
	public WaterData() {
		userDataMap = new HashMap<>();
	}
	
	public void setWaterData(Map<String, List<WaterSample>> map) {
		userDataMap = map;
	}
	
	public List<WaterSample> getRange(String meterId, DateTime start, DateTime end) {
		if (userDataMap.containsKey(meterId))
			return getRangeInSamples(meterId, userDataMap.get(meterId), start, end);
		return null;
	}
	
	private List<WaterSample> getRangeInSamples(String meterId, List<WaterSample> list, 
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

	private int findStopIndex(List<WaterSample> list, DateTime end, int listsize,
			int startIndex) {
		int stopIndex = 0;
		
		for (int i = startIndex; i < listsize; i++) {
			DateTime sample = list.get(i).getSampleDateTime();
			if (sample.isBefore(end))
				stopIndex = i;
		}
		
		stopIndex += 1; // make inclusive
		return stopIndex;
	}

	private int findStartIndex(String meterId, List<WaterSample> list,
			DateTime start, int listsize) {
		int startIndex = 0;
		
		if (getEarliestDate(meterId).isBefore(start))
			startIndex = 0;
		else {
			for (int i = 0; i < listsize; i++) {
				DateTime sample = list.get(i).getSampleDateTime();
				if (sample.isBefore(start))
					startIndex = i;
				else
					break; // don't bother going through the rest
			}
		}
		return startIndex;
	}

	public List<WaterSample> getRange(String meterId, DateTime fromDate) {
		return getRange(meterId, fromDate, getLastDate(meterId));
	}
	
	public List<WaterSample> getAllValues(String meterId) {
		if (userDataMap.containsKey(meterId))
			return userDataMap.get(meterId);
		return null;
	}
	
	public DateTime getEarliestDate(String meterId) {
		if (userDataMap.containsKey(meterId))
			return userDataMap.get(meterId).get(0).getSampleDateTime();
		return null;
	}
	
	public DateTime getLastDate(String meterId) {
		return getLatestSample(meterId).getSampleDateTime();
	}
	
	public WaterSample getLatestSample(String meterId) {
		if (userDataMap.containsKey(meterId)) {
			List<WaterSample> samples = userDataMap.get(meterId);
			return samples.get(samples.size() - 1);
		}	
		return null;
	}
	
}
