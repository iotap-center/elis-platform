package se.mah.elis.adaptor.water.mkb;

import java.util.Properties;
import java.util.UUID;

import org.apache.felix.scr.annotations.Reference;

import se.mah.elis.adaptor.device.api.data.DeviceIdentifier;
import se.mah.elis.adaptor.device.api.entities.devices.DeviceSet;
import se.mah.elis.adaptor.device.api.entities.devices.Gateway;
import se.mah.elis.adaptor.device.api.entities.devices.WaterMeter;
import se.mah.elis.adaptor.water.mkb.data.WaterDataService;
import se.mah.elis.data.OrderedProperties;
import se.mah.elis.exceptions.StaticEntityException;

public class MkbWaterMeter implements WaterMeter {

	private static final long serialVersionUID = 1483471192257524353L;
	private DeviceIdentifier deviceId;
	private String deviceName;
	private String description;
	private Gateway gateway;
	private UUID uuid;
	private boolean isOnline;
	
	@Reference
	private WaterDataService waterDataSource;
	
	protected void bindWaterDataSource(WaterDataService source) {
		if (source.getInstance() != null) {
			waterDataSource = source;
			isOnline = true;
		}
	}
	
	protected void unbindWaterDataSource(WaterDataService source) {
		isOnline = false;
		waterDataSource = null;
	}

	@Override
	public DeviceIdentifier getId() {
		return deviceId;
	}

	@Override
	public void setId(DeviceIdentifier id) throws StaticEntityException {
		this.deviceId = id;
	}

	@Override
	public String getName() {
		return deviceName;
	}

	@Override
	public void setName(String name) throws StaticEntityException {
		deviceName = name;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public void setDescription(String description) throws StaticEntityException {
		this.description = description;
	}

	@Override
	public Gateway getGateway() {
		return gateway;
	}

	@Override
	public void setGateway(Gateway gw) throws StaticEntityException {
		gateway = gw;
	}

	@Override
	public DeviceSet[] getDeviceSets() {
		return null;
	}

	@Override
	public boolean isOnline() {
		return isOnline;
	}

	@Override
	public long getDataId() {
		// not used
		return 0;
	}

	@Override
	public UUID getUUID() {
		return uuid;
	}

	@Override
	public void setUUID(UUID uuid) {
		this.uuid = uuid;
	}

	@Override
	public void setUniqueUserId(int userId) {
		// not used
	}

	@Override
	public int getUniqueUserId() {
		// not used
		return 0;
	}

	@Override
	public Properties getProperties() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OrderedProperties getPropertiesTemplate() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void populate(Properties props) {
		// TODO Auto-generated method stub

	}

	@Override
	public float getWaterConsumption() {
		if (isOnline)
			waterDataSource.getInstance().getLatestSample(getName());
		return 0;
	}

}
