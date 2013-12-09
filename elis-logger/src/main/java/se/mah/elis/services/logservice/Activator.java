package se.mah.elis.services.logservice;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.log.LogReaderService;
import org.osgi.service.log.LogService;

public class Activator implements BundleActivator {

	private BundleContext context;

	public void start(BundleContext context) throws Exception {
		this.context = context;
		createLogListener();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void createLogListener() {
		ServiceReference ref = context
				.getServiceReference(LogReaderService.class.getName());
		if (ref != null) {
			LogReaderService reader = (LogReaderService) context.getService(ref);
			reader.addLogListener(new ElisLogListener());
		}
	}

	public void stop(BundleContext context) throws Exception {

	}
}
