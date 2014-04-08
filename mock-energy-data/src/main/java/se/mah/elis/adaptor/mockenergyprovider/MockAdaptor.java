package se.mah.elis.adaptor.mockenergyprovider;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.log.LogService;

import se.mah.elis.services.users.factory.UserFactory;
import se.mah.elis.services.users.factory.UserProvider;

@Component(name = "Mock Energy Adaptor", immediate = true)
@Service
public class MockAdaptor {

	@Reference
	private LogService log;
	
	@Reference
	private UserFactory userFactoryService;
	
	@Reference
	private ConfigurationAdmin configAdmin;
	
	private UserProvider userProvider;
	
	public MockAdaptor() {
//		eonProvider = new EonUserProvider();
	}
	
	protected void bindUserFactoryService(UserFactory uf) {
		this.userFactoryService = uf;
		userFactoryService.registerProvider(userProvider);
	}
	
	protected void unbindUserFactoryService(UserFactory uf) {
		userFactoryService.unregisterProvider(userProvider);
		this.userFactoryService = null;
	}
	
	protected void bindLog(LogService ls) {
		log = ls;
	}
	
	protected void unbindLog(LogService ls) {
		log = null;
	}

}
