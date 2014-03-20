package se.mah.elis.services.demo.users;

import org.apache.felix.scr.annotations.Reference;
import org.osgi.service.log.LogService;

public class LoggingExample {

	@Reference
	private LogService log;
	
	public void callMeMaybe() {
		if (log != null)
			log.log(LogService.LOG_INFO, "Tada!");
	}
	
	protected void bindLog(LogService service) {
		log = service;
	}
	
	protected void unbindLog(LogService service) {
		log = null;
	}
}
