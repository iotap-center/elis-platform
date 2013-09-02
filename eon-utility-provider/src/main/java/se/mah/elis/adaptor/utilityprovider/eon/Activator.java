package se.mah.elis.adaptor.utilityprovider.eon;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import se.mah.elis.adaptor.building.api.providers.GatewayUserProvider;
import se.mah.elis.adaptor.utilityprovider.eon.internal.EonHttpBridge;
import se.mah.elis.adaptor.utilityprovider.eon.internal.gateway.EonGatewayUserFactory;
import se.mah.elis.services.users.UserService;

public class Activator implements BundleActivator {

	private GatewayUserProvider userFactory;
	ServiceRegistration sr;
	
	public Activator() {
		userFactory = new EonGatewayUserFactory(
				new EonHttpBridge("http://ewpapi2.dev.appex.no", 80,
						"/v0_2/api/"));
	}
	
	public void start(BundleContext context) throws Exception {
		System.out.println("Starting the E.On bundle.");
		
		sr = (ServiceRegistration) context.registerService(
				GatewayUserProvider.class.getName(), userFactory, null);
	}

	public void stop(BundleContext context) throws Exception {
		System.out.println("Stopping the E.On bundle.");
	}
}
