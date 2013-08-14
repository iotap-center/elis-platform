package se.mah.elis.services.electricity.internal;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.util.tracker.ServiceTracker;

import se.mah.elis.adaptor.utilityprovider.api.UtilityProvider;
import se.mah.elis.external.web.usage.electricity.ElectricityResource;
import se.mah.elis.services.electricity.ElectricityService;

public class Activator implements BundleActivator {

	private ServiceRegistration electricityServiceResourceRegistration;
	private ServiceRegistration electricityServiceRegistration;
	private ServiceTracker usageTracker;
	private ServiceTracker providerTracker;

	public void start(BundleContext context) throws Exception {
		createTrackers(context);

		ElectricityService electricityService = new ElectricityServiceImpl(
				providerTracker);
		this.electricityServiceRegistration = context.registerService(
				ElectricityService.class.getName(), electricityService,
				null);

		ElectricityResource httpResource = new ElectricityResource(usageTracker);
		this.electricityServiceResourceRegistration = context.registerService(
				ElectricityResource.class.getName(), httpResource, null);
	}

	private void createTrackers(BundleContext context) {
		this.usageTracker = new ServiceTracker(context,
				ElectricityService.class.getName(), null);
		this.providerTracker = new ServiceTracker(context,
				UtilityProvider.class.getName(), null);

		this.usageTracker.open();
		this.providerTracker.open();
	}

	public void stop(BundleContext ctx) throws Exception {
		this.electricityServiceResourceRegistration.unregister();
		this.electricityServiceRegistration.unregister();
		this.providerTracker.close();
		this.usageTracker.close();
	}

}
