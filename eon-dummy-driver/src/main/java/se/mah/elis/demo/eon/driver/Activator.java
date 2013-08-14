package se.mah.elis.demo.eon.driver;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import se.mah.elis.adaptor.utilityprovider.api.UtilityProvider;
import se.mah.elis.demo.eon.driver.internal.EonService;

public class Activator implements BundleActivator {

	private ServiceRegistration eonServiceRegistration;
	
	public void start(BundleContext ctx) throws Exception {
		//Dictionary<String, String> props = new Hashtable<String, String>();
		//props.put("provider", "eon");
		
		UtilityProvider eonService = new EonService();
		this.eonServiceRegistration = 
				ctx.registerService(UtilityProvider.class.getName(), 
						eonService, null);
	}

	public void stop(BundleContext ctx) throws Exception {
		this.eonServiceRegistration.unregister();
	}

}
