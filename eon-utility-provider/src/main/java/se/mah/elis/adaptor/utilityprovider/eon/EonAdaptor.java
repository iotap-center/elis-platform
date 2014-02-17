package se.mah.elis.adaptor.utilityprovider.eon;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;

import se.mah.elis.adaptor.utilityprovider.eon.internal.user.EonUserProvider;
import se.mah.elis.services.users.factory.UserFactory;
import se.mah.elis.services.users.factory.UserProvider;

@Component(name = "Eon Adaptor", immediate = true)
@Service(value = EonAdaptor.class)
public class EonAdaptor {

	@Reference
	private UserFactory userFactoryService;
	private UserProvider eonProvider;
	
	public EonAdaptor() {
		System.out.println("E.On adaptor created");
		eonProvider = new EonUserProvider();
	}
	
	protected void bindUserFactoryService(UserFactory uf) {
		System.out.println("E.On adaptor bind UserFactory");
		this.userFactoryService = uf;
		userFactoryService.registerProvider(eonProvider);
	}
	
	protected void unbindUserFactoryService(UserFactory uf) {
		System.out.println("E.On adaptor unbind UserFactory");
		userFactoryService.unregisterProvider(eonProvider);
		this.userFactoryService = null;
	}
}
