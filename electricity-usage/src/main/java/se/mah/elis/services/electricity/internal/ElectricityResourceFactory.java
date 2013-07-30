package se.mah.elis.services.electricity.internal;

import org.osgi.framework.Bundle;
import org.osgi.framework.ServiceFactory;
import org.osgi.framework.ServiceRegistration;
import org.osgi.util.tracker.ServiceTracker;

import se.mah.elis.demo.eon.driver.BMSProviderService;
import se.mah.elis.external.web.usage.electricity.ElectricityResource;

public class ElectricityResourceFactory implements ServiceFactory {
	
	public Object getService(Bundle bundle, ServiceRegistration registration) {
		return null;
	}

	public void ungetService(Bundle arg0, ServiceRegistration arg1, Object arg2) {
		// TODO Auto-generated method stub
		
	}

}
