package se.mah.elis.adaptor.utilityprovider.eon;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.mah.elis.adaptor.utilityprovider.eon.internal.user.EonUserProvider;
import se.mah.elis.services.users.factory.UserFactory;

@Component(name = "Eon Adapter")
@Service(value = EonAdaptor.class)
public class EonAdaptor {

	private static final Logger logger = LoggerFactory.getLogger(EonAdaptor.class);
	
	@Reference
	private UserFactory userFactoryService;
	
	public EonAdaptor() {
		userFactoryService.registerProvider(new EonUserProvider());
		logger.info("E.On adapter initiated");
	}

}
