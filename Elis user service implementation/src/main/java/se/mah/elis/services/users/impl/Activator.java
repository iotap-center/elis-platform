/**
 * 
 */
package se.mah.elis.services.users.impl;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;

import se.mah.elis.services.users.UserService;

/**
 * Activates the user service.
 * 
 * @author "Johan Holmberg, Malmö University"
 * @since 1.0
 */
public class Activator implements BundleActivator {

	UserService us;
	ServiceRegistration<UserService> sr;
	
	/**
	 * 
	 */
	public Activator() {
		us = new UserServiceImpl();
	}

	/* (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void start(BundleContext context) throws Exception {
		System.out.println("Starting the user service");
		
		sr = (ServiceRegistration<UserService>) context.registerService(UserService.class.getName(), us, null);
	}

	/* (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void stop(BundleContext context) throws Exception {
		System.out.println("Stopping the user service");
		
		context.ungetService((ServiceReference<UserService>) sr);
	}

}
