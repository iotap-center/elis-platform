package se.mah.elis.adaptor.water.mkb.data;

import java.io.FileNotFoundException;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;

@Component(name = "MkbWaterData", immediate = true)
@Service(value = WaterDataService.class)
public class WaterDataService {

	private WaterData waterData;
	
	public WaterDataService() {
		init();
	}

	private void init() {
		System.out.println("Loading MKB water data");
		try {
			waterData = WaterDataLoader.loadFromFile("/tmp/mkb-water-data/all.txt");
			System.out.println("Loaded MKB water data");
		} catch (FileNotFoundException e) {		
			e.printStackTrace();
		}
		//waterData = WaterDataLoader.loadFromCode();
	}
	
	public WaterData getInstance() {
		return waterData;
	}
}
