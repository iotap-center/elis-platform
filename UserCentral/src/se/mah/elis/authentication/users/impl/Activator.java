package se.mah.elis.authentication.users.impl;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import se.mah.elis.authentication.users.UserCentral;
import se.mah.elis.external.web.users.UserResource;

public class Activator implements BundleActivator {

	private ServiceRegistration userCentralService;
	private ServiceRegistration userHttpResourceService;
	
	@Override
	public void start(BundleContext context) throws Exception {
		System.out.println("UserCentral starting");
		
		this.userCentralService = context.registerService(UserCentral.class.getName(), new UserCentralImpl(), null);
		this.userHttpResourceService = context.registerService(UserResource.class.getName(), 
				new UserResource(context), null);
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		this.userCentralService.unregister();
		this.userHttpResourceService.unregister();
		System.out.println("UserCentral stopping");
	}

}
