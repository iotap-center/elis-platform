package se.mah.elis.services.qsdriver.internal;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import se.mah.elis.external.web.qsdriver.QuantifiedSelfDeviceResource;
import se.mah.elis.external.web.qsdriver.QuantifiedSelfDevicesetResource;
import se.mah.elis.external.web.qsdriver.QuantifiedSelfUsageResource;
import se.mah.elis.external.web.qsdriver.QuantifiedSelfUserResource;


public class Activator implements BundleActivator {
	
	private ServiceRegistration qsDriverServiceRegistration;
	private BundleContext bc;  

	public void start(BundleContext context) throws Exception {
		// Device
		this.qsDriverServiceRegistration = context.registerService(
				QuantifiedSelfDeviceResource.class.getName(), new QuantifiedSelfDeviceResource(),null);
		// User
		this.qsDriverServiceRegistration = context.registerService(
				QuantifiedSelfUserResource.class.getName(), new QuantifiedSelfUserResource(),null);
		// Deviceset
		this.qsDriverServiceRegistration = context.registerService(
				QuantifiedSelfDevicesetResource.class.getName(), new QuantifiedSelfDevicesetResource(),null);
		// Usage
		this.qsDriverServiceRegistration = context.registerService(
				QuantifiedSelfUsageResource.class.getName(), new QuantifiedSelfUsageResource(),null);
		
	}


	public void stop(BundleContext context) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
