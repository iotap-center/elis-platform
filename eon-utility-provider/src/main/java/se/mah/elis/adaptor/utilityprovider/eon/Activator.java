package se.mah.elis.adaptor.utilityprovider.eon;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import se.mah.elis.adaptor.utilityprovider.api.UtilityProvider;

public class Activator implements BundleActivator {

	private ServiceRegistration eonUtilityProviderServiceRegistration;

	public void start(BundleContext context) throws Exception {
		UtilityProvider provider = new EonUtilityProvider();
		
		eonUtilityProviderServiceRegistration = context.registerService(
				UtilityProvider.class.getName(), provider, null);
	}

	public void stop(BundleContext context) throws Exception {
		eonUtilityProviderServiceRegistration.unregister();
	}
}
