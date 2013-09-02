/**
 * 
 */
package se.mah.elis.demo;

import javax.naming.AuthenticationException;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;

import se.mah.elis.adaptor.building.api.entities.GatewayUser;
import se.mah.elis.adaptor.building.api.exceptions.MethodNotSupportedException;
import se.mah.elis.adaptor.building.api.providers.GatewayUserProvider;
import se.mah.elis.services.users.PlatformUser;
import se.mah.elis.services.users.UserService;
import se.mah.elis.services.users.exceptions.NoSuchUserException;

/**
 * Activates the user service.
 * 
 * @author "Johan Holmberg, Malmö University"
 * @since 1.0
 */
public class Activator implements BundleActivator, ServiceListener {

	private UserService us;
	private GatewayUserProvider gup;
	private BundleContext ctx;
	private ServiceReference<UserService> usRef;
	private ServiceReference<GatewayUserProvider> gupRef;
	
	private PlatformUser pu;
	private GatewayUser gu;
	
	private String eonUsername = "marcus.ljungblad@mah.se";
	private String eonPassword = "medeamah2012";
	private String platformUsername = "ernst";
	private String platformPassword = "handyman";
	
	/* (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void start(BundleContext context) throws Exception {
		System.out.println("Starting the demo");
		
		ctx = context;
		
		synchronized (this) {
			ctx.addServiceListener(this,
					"(&(objectClass=" + UserService.class.getName() + "))");
			ctx.addServiceListener(this,
					"(&(objectClass=" + GatewayUserProvider.class.getName() + "))");

			ServiceReference[] usRefs = ctx.getServiceReferences(UserService.class.getName(), null);
			if (usRefs != null) {
				usRef = usRefs[0];
				us = (UserService) ctx.getService(usRef);
				populate();
			}
			
			ServiceReference[] gupRefs = ctx.getServiceReferences(GatewayUserProvider.class.getName(), null);
			if (gupRefs != null) {
				gupRef = gupRefs[0];
				gup = (GatewayUserProvider) ctx.getService(gupRef);
				populate();
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void stop(BundleContext context) throws Exception {
		System.out.println("Stopping the demo");
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
				populate();
			}
			if (objectClass.equals(GatewayUserProvider.class.getName())
					&& gupRef == null) {
				gupRef = (ServiceReference<GatewayUserProvider>) event.getServiceReference();
				gup = (GatewayUserProvider) ctx.getService(gupRef);
				populate();
			}
		} else if (event.getType() == ServiceEvent.UNREGISTERING) {
			if (event.getServiceReference() == usRef) {
				ctx.ungetService(usRef);
				usRef = null;
				us = null;
				
				ServiceReference[] usRefs = null;
				try {
					usRefs = ctx.getServiceReferences(UserService.class.getName(), null);
				} catch (InvalidSyntaxException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if (usRefs != null) {
					usRef = usRefs[0];
					us = (UserService) ctx.getService(usRef);
					populate();
				}
			}
			if (event.getServiceReference() == gupRef) {
				ctx.ungetService(gupRef);
				gupRef = null;
				gup = null;
				
				ServiceReference[] gupRefs = null;
				try {
					gupRefs = ctx.getServiceReferences(GatewayUserProvider.class.getName(), null);
				} catch (InvalidSyntaxException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if (gupRefs != null) {
					gupRef = gupRefs[0];
					gup = (GatewayUserProvider) ctx.getService(usRef);
					populate();
				}
			}
		}
	}

	private void populate() {
		if (us != null) {
			if (pu == null) {
				pu = us.createPlatformUser(platformUsername, platformPassword);
			}
		}
		if (gup != null) {
			if (gu == null) {
				try {
					gu = gup.getUser(eonUsername, eonPassword);
				} catch (AuthenticationException | MethodNotSupportedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		if (us != null && pu != null && gu != null) {
			try {
				us.registerUserToPlatformUser(gu, pu);
			} catch (NoSuchUserException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
