package se.mah.elis.services.logservice;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.osgi.service.log.LogReaderService;

@Component(name = "ElisLogReaderService", immediate = true)
@Service(value = ElisLogReaderService.class)
public class ElisLogReaderService {

	@Reference
	private LogReaderService logReaderService;
	
	protected void bindLogReaderService(LogReaderService lrs) {
		logReaderService = lrs;
		lrs.addLogListener(new ElisLogWriter());
	}
	
	protected void unbindLogReaderService(LogReaderService lrs) {
		logReaderService = null;
	}
}
