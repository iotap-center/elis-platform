package se.mah.elis.adaptor.water.mkb.data;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Dictionary;
import java.util.Hashtable;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.felix.service.command.CommandProcessor;
import org.apache.felix.service.command.Descriptor;
import org.joda.time.DateTime;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
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
@Properties({
		@Property(name = CommandProcessor.COMMAND_SCOPE, value = "elis"),
		@Property(name = CommandProcessor.COMMAND_FUNCTION, value = { "reloadwater" }) })
public class WaterDataService implements ManagedService {

	private static final String SERVICE_PID = "se.mah.elis.adaptor.water.mkb";
	private static final String DATA_SOURCE = "se.mah.elis.adaptor.water.mkb.source";
	public static Dictionary<String, ?> properties;

	@Reference
	private LogService log;

	@Reference
	ConfigurationAdmin configAdmin;

	private WaterUpdater waterUpdater;
	private Thread updaterThread;
	private WaterData waterData;
	
	private boolean isInitialised = false;

	public WaterDataService() { }
	
	private void init() {
		waterUpdater = new WaterUpdater();
		updaterThread = new Thread(waterUpdater);
		updaterThread.start();
	}

	protected void deactivate(ComponentContext ctx) {
		updaterThread.interrupt();
	}

	public WaterDataService(WaterData data) {
		waterData = data;
		isInitialised = true;
	}

	public WaterData getInstance() {
		return waterData;
	}

	private void setInstance(WaterData data) {
		waterData = data;
	}

	@Descriptor("Trigger reload of water data from source")
	public void reloadwater() {
		waterUpdater.loadData((String) WaterDataService.properties
				.get(DATA_SOURCE));
	}

	private class WaterUpdater implements Runnable {

		public static final String DEFAULT_WATERDATA = "/tmp/mkb-water-data/all.txt";
		private static final long ONE_HOUR = 1000 * 60 * 60;

		@Override
		public void run() {
			while (true) {
				String source = (String) WaterDataService.properties.get(DATA_SOURCE);
				loadData(source);

				try {
					Thread.sleep(ONE_HOUR);
				} catch (InterruptedException e) {
					break;
				}
			}
		}

		public synchronized void loadData(String filename) {
			try {
				log("Reloading water data at " + DateTime.now()
						+ " from file: " + filename);
				setInstance(WaterDataLoader.loadFromFile(filename));
			} catch (FileNotFoundException e) {
				logWarning("Could load water data from file (FileNotFound): "
						+ filename);
				try {
					log("Trying default location for water data: "
							+ DEFAULT_WATERDATA);
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

	protected void bindConfigAdmin(ConfigurationAdmin ca) {
		configAdmin = ca;
		setConfig();
		if (!isInitialised)
			init();
	}

	protected void unbindConfigAdmin(ConfigurationAdmin ca) {
		configAdmin = null;
	}

	@Override
	public void updated(Dictionary<String, ?> props)
			throws ConfigurationException {
		if (props != null) {
			properties = props;
			log("Updated configuration: " + properties.toString());
		}
	}

	private void setConfig() {
		try {
			Configuration config = configAdmin.getConfiguration(SERVICE_PID);
			properties = config.getProperties();
			setDefaultConfiguration();
			config.update(properties);
		} catch (IOException e) {
			logError("Failed to get configuration from Configuration Admin service.");
			setDefaultConfiguration();
		} finally {
			log("Installed configuration: " + properties.toString());
		}
	}

	private void setDefaultConfiguration() {
		if (properties == null) {
			properties = getDefaultConfiguration();
			log("Using default configuration.");
		}
	}

	private static Dictionary<String, ?> getDefaultConfiguration() {
		Dictionary<String, Object> props = new Hashtable<>();
		props.put(DATA_SOURCE, WaterUpdater.DEFAULT_WATERDATA);
		return props;
	}

}
