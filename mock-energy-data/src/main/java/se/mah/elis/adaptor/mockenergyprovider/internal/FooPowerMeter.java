package se.mah.elis.adaptor.mockenergyprovider.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import se.mah.elis.adaptor.device.api.data.DeviceIdentifier;
import se.mah.elis.adaptor.device.api.entities.devices.DeviceSet;
import se.mah.elis.adaptor.device.api.entities.devices.ElectricitySampler;
import se.mah.elis.adaptor.device.api.entities.devices.Gateway;
import se.mah.elis.adaptor.device.api.exceptions.SensorFailedException;
import se.mah.elis.data.ElectricitySample;
import se.mah.elis.data.OrderedProperties;
import se.mah.elis.exceptions.StaticEntityException;

public class FooPowerMeter implements ElectricitySampler {

	private static final int VALUETYPE_POWER = 0;
	private static DateTimeFormatter fmt = DateTimeFormat
			.forPattern("yyyy-MM-dd");

	protected boolean isOnline;
	protected FooGateway gateway;
	protected DeviceIdentifier deviceId;
	protected String deviceName = "";
	protected String description = "";
	protected UUID dataid;
	protected UUID ownerid;
	protected DateTime created = DateTime.now();

	@Override
	public DeviceIdentifier getId() {
		return deviceId;
	}

	@Override
	public void setId(DeviceIdentifier id) throws StaticEntityException {
		deviceId = id;
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
	public void setGateway(Gateway gateway) throws StaticEntityException {
		if (!(gateway instanceof FooGateway))
			throw new StaticEntityException();
		this.gateway = (FooGateway) gateway;
	}

	@Override
	public DeviceSet[] getDeviceSets() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isOnline() {
		return isOnline;
	}

	@Override
	public ElectricitySample getSample() throws SensorFailedException {
		long seed = calculateSeed(DateTime.now());

		ElectricitySample electricitySample = new ElectricitySampleImpl(seed);

		return electricitySample;
	}

	@Override
	public List<ElectricitySample> getSamples(DateTime from, DateTime to)
			throws SensorFailedException {
		List<ElectricitySample> samples = new ArrayList<ElectricitySample>();

		long seed = 0;
		DateTime start = from;

		while (start.isBefore(to)) {
			seed = calculateSeed(start);
			samples.add(new ElectricitySampleImpl(seed));

			start = start.plusHours(1);

			if (start.equals(to)) {
				break;
			}
		}

		return samples;
	}

	@Override
	public ElectricitySample sample(int millis) throws SensorFailedException {
		// TODO Sample the current energy usage for a given amount of time.
		return null;
	}

	protected String getGatewayAddress() {
		return getGateway().getAddress().toString();
	}

	@Override
	public Properties getProperties() {
		OrderedProperties props = new OrderedProperties();
		props.put("dataid", dataid);
		props.put("ownerid", ownerid);
		props.put("created", created);
		props.put("identifier", deviceId);
		props.put("device_name", deviceName);
		props.put("description", description);
		return props;
	}

	@Override
	public OrderedProperties getPropertiesTemplate() {
		OrderedProperties props = new OrderedProperties();
		props.put("dataid", UUID.randomUUID());
		props.put("ownerid", UUID.randomUUID());
		props.put("created", created);
		props.put("identifier", new FooDeviceIdentifier("a"));
		props.put("device_name", "64");
		props.put("description", "256");
		return props;
	}

	@Override
	public void populate(Properties props) {
		this.dataid = (UUID) props.get("dataid");
		this.ownerid = (UUID) props.get("ownerid");
		this.created = (DateTime) props.get("created");
		this.deviceId = new FooDeviceIdentifier("");
		this.deviceName = (String) props.get("deviceName");
		this.description = (String) props.getProperty("description");
		this.deviceId.populate(props);

		this.gateway = new FooGateway();
	}

	@Override
	public UUID getDataId() {
		return dataid;
	}

	@Override
	public void setDataId(UUID uuid) {
		dataid = uuid;
	}

	@Override
	public void setOwnerId(UUID userId) {
		ownerid = userId;
	}

	@Override
	public UUID getOwnerId() {
		return ownerid;
	}

	@Override
	public DateTime created() {
		return created;
	}

	private long calculateSeed(DateTime dt) {
		long userPart = ownerid.getLeastSignificantBits();
		long date = dt.getMillis();
		long seed = userPart/2 + date/2;
		
		return seed;
	}
}
