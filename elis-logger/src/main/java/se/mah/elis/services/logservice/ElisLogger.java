package se.mah.elis.services.logservice;

import org.osgi.framework.BundleContext;
import org.osgi.service.log.LogService;
import org.osgi.util.tracker.ServiceTracker;


public class ElisLogger {

	private static LogService logService = null;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static LogService getLogger(BundleContext context) {
		if (logService == null) { 
			ServiceTracker logServiceTracker = new ServiceTracker(context, LogService.class.getName(), null);
			logServiceTracker.open();
			logService = (LogService) logServiceTracker.getService();
		}
		
		return logService;
	}
}
