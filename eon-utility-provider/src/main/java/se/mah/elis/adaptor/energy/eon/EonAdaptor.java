package se.mah.elis.adaptor.energy.eon;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;

import se.mah.elis.adaptor.energy.eon.internal.user.EonUserProvider;
import se.mah.elis.services.users.factory.UserFactory;
import se.mah.elis.services.users.factory.UserProvider;

@Component(name = "Eon Adaptor", immediate = true)
@Service(value = EonAdaptor.class)
public class EonAdaptor {

	@Reference
	private UserFactory userFactoryService;
	private UserProvider eonProvider;
	
	public EonAdaptor() {
		eonProvider = new EonUserProvider();
	}
	
	protected void bindUserFactoryService(UserFactory uf) {
		this.userFactoryService = uf;
		userFactoryService.registerProvider(eonProvider);
	}
	
	protected void unbindUserFactoryService(UserFactory uf) {
		userFactoryService.unregisterProvider(eonProvider);
		this.userFactoryService = null;
	}
}
