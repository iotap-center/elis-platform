package se.mah.elis.adaptor.fooprovider;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.osgi.service.log.LogService;

import se.mah.elis.adaptor.fooprovider.internal.user.FooUserProvider;
import se.mah.elis.services.users.factory.UserFactory;
import se.mah.elis.services.users.factory.UserProvider;

@Component(name = "Mock Energy Adaptor", immediate = true)
@Service(value=MockAdaptor.class)
public class MockAdaptor {

	@Reference
	private LogService log;
	
	@Reference
	private UserFactory userFactoryService;
	
	private UserProvider userProvider;
	
	public MockAdaptor() {
		userProvider = new FooUserProvider();
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
