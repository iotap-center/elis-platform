package se.mah.elis.adaptor.utilityprovider.eon;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.mah.elis.adaptor.device.api.providers.GatewayUserProvider;
import se.mah.elis.adaptor.utilityprovider.eon.internal.gateway.EonGatewayUserFactory;

public class EonAdaptor implements BundleActivator {

	private static final Logger logger = LoggerFactory.getLogger(EonAdaptor.class); 
	private GatewayUserProvider userFactory;
	ServiceRegistration sr;
	
	public EonAdaptor() {
		userFactory = new EonGatewayUserFactory();
	}
	
	public void start(BundleContext context) throws Exception {
		logger.info("The E.On bundle just started");
		
		sr = (ServiceRegistration) context.registerService(
				GatewayUserProvider.class.getName(), userFactory, null);
	}

	public void stop(BundleContext context) throws Exception {
		logger.info("The E.On bundle just stopped");
	}
}
