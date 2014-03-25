package se.mah.elis.adaptor.water.mkb.data;

import java.io.FileNotFoundException;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.joda.time.DateTime;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.log.LogService;

/**
 * 
 * OSGI service that provides a way for other bundles to query and retrieve 
 * water data points. 
 * 
 * The data is refreshed once every hour. 
 * 
 * @author Marcus Ljungblad
 * @since 1.0
 * @version 1.1
 *
 */
@Component(name = "MkbWaterData", immediate = true)
@Service(value = WaterDataService.class)
public class WaterDataService {
	
	@Reference
	private LogService log;
	
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

		private static final String DEFAULT_WATERDATA = "/tmp/mkb-water-data/all.txt";
		private static final long ONE_HOUR = 1000 * 60 * 60;
		
		@Override
		public void run() {
			while (true) {
				log("Reloading water data at " + DateTime.now());
				
				loadData(DEFAULT_WATERDATA);
				
				try {
					Thread.sleep(ONE_HOUR);
				} catch (InterruptedException e) {
					break;
				}
			}
		}
		
		private void loadData(String filename) {
			try {
				setInstance(WaterDataLoader.loadFromFile(filename));
			} catch (FileNotFoundException e) {		
				logWarning("Could load water data from file (FileNotFound): " + filename);
				try {
					log("Trying default location for water data: " + DEFAULT_WATERDATA);
					setInstance(WaterDataLoader.loadFromFile(DEFAULT_WATERDATA));
				} catch (FileNotFoundException e2) {
					logError("Could not find any water data. MkbWaterProvider not available.");
				}
			}
		}
	}

	private void log(String message) {
		log(LogService.LOG_INFO, message);
	}

	private void logError(String message) {
		log(LogService.LOG_ERROR, message);
	}
	
	private void logWarning(String message) {
		log(LogService.LOG_WARNING, message);
	}

	private void log(int level, String message) {
		if (log != null)
			log.log(level, message);
	}

	protected void bindLog(LogService ls) {
		log = ls;
	}

	protected void unbindLog(LogService ls) {
		log = null;
	}
	
}
