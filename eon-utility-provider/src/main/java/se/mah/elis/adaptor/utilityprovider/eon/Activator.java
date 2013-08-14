package se.mah.elis.adaptor.utilityprovider.eon;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import se.mah.elis.adaptor.building.api.entities.devices.DeviceSet;
import se.mah.elis.adaptor.utilityprovider.api.UtilityProvider;

public class Activator implements BundleActivator {

	private ServiceRegistration eonUtilityProviderServiceRegistration;

	public void start(BundleContext context) throws Exception {
		EonUtilityProviderService eonService = new EonUtilityProviderService();
//		if (eonService.initialise("eon2hem@gmail.com", "02DCBD")) { 
//			eonUtilityProviderServiceRegistration = context.registerService(
//					UtilityProvider.class.getName(), eonService, null);
//		}
		
		DeviceSet set = eonService.getDeviceSet("", "apa");
		System.out.println(set.size());
	}

	public void stop(BundleContext context) throws Exception {
		eonUtilityProviderServiceRegistration.unregister();
	}
}
