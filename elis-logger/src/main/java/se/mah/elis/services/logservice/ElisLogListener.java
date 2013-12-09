package se.mah.elis.services.logservice;

import org.osgi.service.log.LogEntry;
import org.osgi.service.log.LogListener;

public class ElisLogListener implements LogListener {

	public void logged(LogEntry entry) {
		String logMessage = String.format("[%s][%d][%d] %s", 
				entry.getBundle().getSymbolicName(), 
				entry.getBundle().getBundleId(),
				entry.getLevel(),
				entry.getMessage());
		
		System.out.println(logMessage);
	}

}
