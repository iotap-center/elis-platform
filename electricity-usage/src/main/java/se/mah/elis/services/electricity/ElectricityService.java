package se.mah.elis.services.electricity;

import java.util.List;

import se.mah.elis.services.electricity.internal.DeviceSetRequest;
import se.mah.elis.services.electricity.internal.Usage;
import se.mah.elis.services.electricity.internal.UsageRequest;

public interface ElectricityService {
	public Usage getDeviceSetUsage(UsageRequest usageRequest) throws InterruptedException;
}
