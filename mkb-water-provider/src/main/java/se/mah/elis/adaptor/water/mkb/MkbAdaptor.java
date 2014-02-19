package se.mah.elis.adaptor.water.mkb;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;

import se.mah.elis.services.users.factory.UserFactory;

@Component(name = "MKB Water adaptor", immediate = true)
@Service(value = MkbAdaptor.class)
public class MkbAdaptor {

	@Reference
	private UserFactory userFactoryService;
	
	private MkbProvider mkbProvider;
	
	public MkbAdaptor() {
		mkbProvider = new MkbProvider();
	}
	
	protected void bindUserFactoryService(UserFactory uf) {
		userFactoryService = uf;
		userFactoryService.registerProvider(mkbProvider);
	}
	
	protected void unbindUserFactoryService(UserFactory uf) {
		userFactoryService.unregisterProvider(mkbProvider);
		userFactoryService = null;
	}
	
	
	
}
