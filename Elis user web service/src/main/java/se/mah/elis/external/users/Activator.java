/**
 * 
 */
package se.mah.elis.external.users;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;

import se.mah.elis.services.users.UserService;

/**
 * Activates the User Web Service bundle.
 * 
 * @author "Johan Holmberg, Malmö University"
 * @since 1.0
 */
public class Activator implements BundleActivator, ServiceListener {
	
	private ServiceReference<UserService> usRef;
	private UserService us;
	private BundleContext ctx;
	private UserWebService uws;

	/* (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void start(BundleContext context) throws Exception {
		uws = new UserWebService();
		ctx = context;
		
		synchronized (this) {
			ctx.addServiceListener(this,
					"(&(objectClass=" + UserService.class.getName() + "))");
			ServiceReference[] usRefs =
					ctx.getServiceReferences(UserService.class.getName(),
							null);
			if (usRefs != null) {
				usRef = usRefs[0];
				us = (UserService) ctx.getService(usRef);
				uws.setUserService(us);
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
		ctx.ungetService(usRef);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void serviceChanged(ServiceEvent event) {
		String[] objectClass =
				(String[]) event.getServiceReference().getProperty("objectClass");
		
		if (event.getType() == ServiceEvent.REGISTERED) {
			if (objectClass.equals(UserService.class.getName())
					&& usRef == null) {
				usRef = (ServiceReference<UserService>) event.getServiceReference();
				us = (UserService) ctx.getService(usRef);
			}
		} else if (event.getType() == ServiceEvent.UNREGISTERING) {
			if (event.getServiceReference() == usRef) {
				ctx.ungetService(usRef);
				usRef = null;
				us = null;
				uws.setUserService(null);
				
				ServiceReference<UserService>[] usRefs = null;
				try {
					usRefs = (ServiceReference<UserService>[]) ctx.getServiceReferences(UserService.class.getName(), null);
				} catch (InvalidSyntaxException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if (usRefs != null) {
					usRef = usRefs[0];
					us = (UserService) ctx.getService(usRef);
					uws.setUserService(us);
				}
			}
		}
	}

}
