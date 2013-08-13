package se.mah.elis.adaptor.utilityprovider.eon;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class Activator implements BundleActivator {
	
	private ServiceRegistration reg;
	private static BundleContext context;
	
	
	static BundleContext getContext() {
		return context;
	}
	
	public void start(BundleContext context) throws Exception {
		Activator.context =  context;
		
		
//        reg = context.registerService(ElectricityUseService.class.getName(),new ElectricityUseService(),null);
		System.out.println("Eletricity Use Service Started");
		
		// Username and Password needed for authentication
		EonUtilityProviderService electricityService = new EonUtilityProviderService("eon2hem@gmail.com", "02DCBD");
		
		long panelId = electricityService.getPanels();
		String deviceId = electricityService.getDevices(panelId);

		String kwh = electricityService.getDeviceStatus(panelId, deviceId);
		System.out.println("Current Kwh is: "+kwh);
			

	}

	public void stop(BundleContext context) throws Exception {
		reg.unregister();
		System.out.println("Eletricity Use Service Stopped");
	}
}
