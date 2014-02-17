package se.mah.elis.services.rest.demo;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class Activator implements BundleActivator {

	private ServiceRegistration<Demo> registration;

	@Override
	public void start(BundleContext context) throws Exception {
		registration = context.registerService(Demo.class, new Demo(), null);
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		registration.unregister();
	}

}
