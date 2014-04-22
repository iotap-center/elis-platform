package se.mah.elis.adaptor.water.mkb.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Parser to read files synchronised from the Elvaco FTP server. 
 * 
 * @author Marcus Ljungblad
 * @since 1.0
 * @version 1.0
 *
 */
public class WaterDataLoader {

	private static DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");

	public static WaterData loadFromFile(String path) throws FileNotFoundException { 
		WaterData data = new WaterData();
		Map<String, List<WaterDataPoint>> samples = getSamplesFromFile(path);
		data.setWaterData(samples);
		return data;
	}

	/**
	 * Read data points from a text file. 
	 * 
	 * @param path 
	 * @return
	 * @throws FileNotFoundException
	 */
	private static Map<String, List<WaterDataPoint>> getSamplesFromFile(String path) throws FileNotFoundException {
		Map<String, List<WaterDataPoint>> samples = new HashMap<>();
		BufferedReader bufferedReader = null;
		
		try {
			FileReader in = new FileReader(new File(path));
			bufferedReader = new BufferedReader(in);
			
			for (String line; (line = bufferedReader.readLine()) != null;) {
				parseLine(samples, line);
			}
		} catch (FileNotFoundException e) {
			throw e;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
				try {
					bufferedReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		
		return samples;
	}
	
	/**
	 * Used for test purposes to generate a small sample of data points. 
	 * 
	 * @return
	 */
	public static WaterData loadFromCode() {
		WaterData data = new WaterData();
		data.setWaterData(createFromCode());
		return data;
	}

	private static Map<String, List<WaterDataPoint>> createFromCode() {
		String[] testLines = testData().split("\n");
		Map<String, List<WaterDataPoint>> samples = parseLines(testLines);
		return samples;
	}

	public static Map<String, List<WaterDataPoint>> parseLines(String[] testLines) {
		Map<String, List<WaterDataPoint>> samples = new HashMap<>();
		for (String line : testLines) {
			parseLine(samples, line);
		}
		return samples;
	}

	private static void parseLine(Map<String, List<WaterDataPoint>> samples,
			String line) {
		WaterDataPoint sample = null;
		
		try {
			
			String[] parts = line.split(";");
			
			if (parts.length < 3) // we can survive w/o meter value, just say 0
				throw new Exception("Insufficient arguments online");
			
			DateTime registered = fmt.parseDateTime(parts[0]);
			String meterId = parts[1];
			
			String measuredValue = null;
			if (parts.length == 4)
				measuredValue = parts[3];
			
			if (measuredValue == null)
				measuredValue = "0";
			
			float value = Float.parseFloat(measuredValue.replace(',', '.'));
			sample = new WaterDataPoint(registered, value);
		
			if (samples.containsKey(meterId))
				samples.get(meterId).add(sample);
			else {
				samples.put(meterId, new ArrayList<WaterDataPoint>());
				samples.get(meterId).add(sample);
			}
			
		} catch (Exception e) {
			// skip this line
		}
	}

	public static String testData() {
		return  "2014-02-18 00:00:00;63408104;735999114001608663;18,515\n" +
				"2014-02-18 01:00:00;63408104;735999114001608663;18,516\n" +
				"2014-02-18 02:00:00;63408104;735999114001608663;18,516\n" +
				"2014-02-18 03:00:00;63408104;735999114001608663;18,516\n" +
				"2014-02-18 04:00:00;63408104;735999114001608663;18,518\n" +
				"2014-02-18 05:00:00;63408104;735999114001608663;18,519\n" +
				"2014-02-18 06:00:00;63408104;735999114001608663;18,519\n" +
				"2014-02-18 07:00:00;63408104;735999114001608663;18,519\n" +
				"2014-02-18 00:00:00;63408102;735999114001815665;29,200\n" +
				"2014-02-18 01:00:00;63408102;735999114001815665;29,200\n" +
				"2014-02-18 02:00:00;63408102;735999114001815665;29,200\n" +
				"2014-02-18 03:00:00;63408102;735999114001815665;29,200\n" +
				"2014-02-18 04:00:00;63408102;735999114001815665;29,200\n" +
				"2014-02-18 05:00:00;63408102;735999114001815665;29,200\n" +
				"2014-02-18 06:00:00;63408102;735999114001815665;29,200\n" +
				"2014-02-18 07:00:00;63408102;735999114001815665;29,200\n" +
				"2014-02-18 00:00:00;63408103;735999114002080222;33,763\n" +
				"2014-02-19 00:00:00;63408103;735999114002080222;33,763\n" +
				"2014-02-20 00:00:00;63408103;735999114002080222;33,763\n" +
				"2014-02-21 00:00:00;63408103;735999114002080222;33,764\n" +
				"2014-02-22 00:00:00;63408103;735999114002080222;33,764\n" +
				"2014-02-23 00:00:00;63408103;735999114002080222;33,764\n" +
				"2014-02-24 00:00:00;63408103;735999114002080222;33,764\n" +
				"2014-02-25 00:00:00;63408103;735999114002080222;33,764";
	}
	
}
