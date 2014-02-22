package se.mah.elis.adaptor.water.mkb.data;

import java.io.FileNotFoundException;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.joda.time.DateTime;
import org.osgi.service.component.ComponentContext;

@Component(name = "MkbWaterData", immediate = true)
@Service(value = WaterDataService.class)
public class WaterDataService {
	
	private Thread updaterThread;
	private WaterData waterData;
	
	public WaterDataService() {
		updaterThread = new Thread(new UpdaterThread());
		updaterThread.start();
	}
	
	protected void deactivate(ComponentContext ctx) {
		updaterThread.interrupt();
	}
	
	public WaterDataService(WaterData data) {
		waterData = data;
	}
	
	public WaterData getInstance() {
		return waterData;
	}
	
	private void setInstance(WaterData data) {
		waterData = data;
	}
	
	private class UpdaterThread implements Runnable {

		private static final long ONE_HOUR = 1000 * 60 * 60;
		
		@Override
		public void run() {
			while (true) {
				System.out.println("Reloading water data at " + DateTime.now());
				
				loadData();
				
				try {
					Thread.sleep(ONE_HOUR);
				} catch (InterruptedException e) {
					break;
				}
			}
		}
		
		private void loadData() {
			try {
				setInstance(WaterDataLoader.loadFromFile("/tmp/mkb-water-data/all.txt"));
			} catch (FileNotFoundException e) {		
				e.printStackTrace();
			}
		}
	}
}
