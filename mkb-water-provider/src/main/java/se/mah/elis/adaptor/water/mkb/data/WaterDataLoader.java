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

public class WaterDataLoader {

	private static DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");

	public static WaterData loadFromFile(String path) throws FileNotFoundException { 
		WaterData data = new WaterData();
		Map<String, List<WaterSample>> samples = getSamplesFromFile(path);
		data.setWaterData(samples);
		return data;
	}

	private static Map<String, List<WaterSample>> getSamplesFromFile(String path) throws FileNotFoundException {
		Map<String, List<WaterSample>> samples = new HashMap<>();
		BufferedReader bufferedReader = null;
		
		try {
			FileReader in = new FileReader(new File(path));
			bufferedReader = new BufferedReader(in);
			
			for (String line; (line = bufferedReader.readLine()) != null;) {
				parseLine(samples, line);
			}
			
			bufferedReader.close();
		} catch (FileNotFoundException e) {
			throw e;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return samples;
	}
	
	public static WaterData loadFromCode() {
		WaterData data = new WaterData();
		data.setWaterData(createFromCode());
		return data;
	}

	private static Map<String, List<WaterSample>> createFromCode() {
		String[] testLines = testData().split("\n");
		Map<String, List<WaterSample>> samples = parseLines(testLines);
		return samples;
	}

	private static Map<String, List<WaterSample>> parseLines(String[] testLines) {
		Map<String, List<WaterSample>> samples = new HashMap<>();
		for (String line : testLines) {
			parseLine(samples, line);
		}
		return samples;
	}

	private static void parseLine(Map<String, List<WaterSample>> samples,
			String line) {
		WaterSample sample = null;
		
		try {
			
			String[] parts = line.split(";");
			DateTime registered = fmt.parseDateTime(parts[0]);
			String meterId = parts[1];
			float value = Float.parseFloat(parts[3].replace(',', '.'));
			sample = new WaterSample(registered, value);
		
			if (samples.containsKey(meterId))
				samples.get(meterId).add(sample);
			else {
				samples.put(meterId, new ArrayList<WaterSample>());
				samples.get(meterId).add(sample);
			}
			
		} catch (Exception e) {
			// skip this line
		}
	}

	public static String testData() {
		return 	"2014-02-18 00:00:00;63408104;735999114001608663;18,515\n" +
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
				"2014-02-18 01:00:00;63408103;735999114002080222;33,763\n" +
				"2014-02-18 02:00:00;63408103;735999114002080222;33,763\n" +
				"2014-02-18 03:00:00;63408103;735999114002080222;33,764\n" +
				"2014-02-18 04:00:00;63408103;735999114002080222;33,764\n" +
				"2014-02-18 05:00:00;63408103;735999114002080222;33,764\n" +
				"2014-02-18 06:00:00;63408103;735999114002080222;33,764\n" +
				"2014-02-18 07:00:00;63408103;735999114002080222;33,764";
	}
	
}
