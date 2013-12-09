package se.mah.elis.adaptor.building.ninjablock;

import se.mah.elis.adaptor.building.api.data.DeviceIdentifier;
import se.mah.elis.adaptor.building.api.entities.NotificationConsumer;
import se.mah.elis.adaptor.building.api.entities.devices.DeviceSet;
import se.mah.elis.adaptor.building.api.entities.devices.Gateway;
import se.mah.elis.adaptor.building.api.entities.devices.Tripwire;
import se.mah.elis.adaptor.building.api.exceptions.StaticEntityException;
import se.mah.elis.auxiliaries.exceptions.UnsupportedFunctionalityException;

public class NinjablockTripwire implements Tripwire {

	public NinjablockTripwire() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void setNotifier(NotificationConsumer consumer)
			throws UnsupportedFunctionalityException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public DeviceIdentifier getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setId(DeviceIdentifier id) throws StaticEntityException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setName(String name) throws StaticEntityException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setDescription(String description) throws StaticEntityException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Gateway getGateway() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setGateway(Gateway gw) throws StaticEntityException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public DeviceSet[] getDeviceSets() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isOnline() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isTriggered() throws UnsupportedFunctionalityException {
		
		Thread thread = new Thread();

		
		return false;
	}

	@Override
	public void release() {
		// TODO Auto-generated method stub
		
	}

}
