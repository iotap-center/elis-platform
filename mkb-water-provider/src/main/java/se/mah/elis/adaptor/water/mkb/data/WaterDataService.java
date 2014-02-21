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
	
	public WaterDataService(WaterData data) {
		waterData = data;
	}

	private void init() {
		try {
			waterData = WaterDataLoader.loadFromFile("/tmp/mkb-water-data/all.txt");
		} catch (FileNotFoundException e) {		
			e.printStackTrace();
		}
	}
	
	public WaterData getInstance() {
		return waterData;
	}
}
