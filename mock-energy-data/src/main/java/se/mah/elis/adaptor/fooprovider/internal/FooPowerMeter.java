package se.mah.elis.adaptor.fooprovider.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.joda.time.DateTime;
import org.osgi.service.log.LogService;

import se.mah.elis.adaptor.device.api.data.DeviceIdentifier;
import se.mah.elis.adaptor.device.api.entities.devices.DeviceSet;
import se.mah.elis.adaptor.device.api.entities.devices.ElectricitySampler;
import se.mah.elis.adaptor.device.api.entities.devices.Gateway;
import se.mah.elis.adaptor.device.api.exceptions.SensorFailedException;
import se.mah.elis.data.ElectricitySample;
import se.mah.elis.data.OrderedProperties;
import se.mah.elis.exceptions.StaticEntityException;

@Service
public class FooPowerMeter implements ElectricitySampler {

	@Reference
	private LogService log;
	
	protected FooGateway gateway;
	protected String deviceName = "Foo Meter";
	protected String description = "A mock power meter";
	protected UUID dataid;
	protected UUID ownerid;
	protected DateTime created = DateTime.now();
	
	public FooPowerMeter(FooGateway gateway, UUID ownerid) {
		this.gateway = gateway;
	}

	@Override
	public DeviceIdentifier getId() {
		return new FooDeviceIdentifier("foo");
	}

	@Override
	public void setId(DeviceIdentifier id) throws StaticEntityException {
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
		return true;
	}

	@Override
	public ElectricitySample getSample() throws SensorFailedException {
		long seed = calculateSeed(DateTime.now());

		log(LogService.LOG_INFO, "Fetching sample");
		ElectricitySample electricitySample = new ElectricitySampleImpl(seed);

		return electricitySample;
	}

	@Override
	public List<ElectricitySample> getSamples(DateTime from, DateTime to)
			throws SensorFailedException {
		log(LogService.LOG_INFO, "Fetching samples");
		List<ElectricitySample> samples = new ArrayList<ElectricitySample>();

		long seed = 0;
		DateTime start = from;
		int i = 0;

		while (start.isBefore(to)) {
			++i;
			seed = calculateSeed(start);
			samples.add(new ElectricitySampleImpl(seed, start));

			start = start.plusHours(1);

			if (start.equals(to)) {
				break;
			}
		}
		log(LogService.LOG_INFO, "Fetched " + i + " samples");

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
		props.put("identifier", new FooDeviceIdentifier("foo"));
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
		props.put("identifier", new FooDeviceIdentifier("foo"));
		props.put("device_name", "64");
		props.put("description", "256");
		return props;
	}

	@Override
	public void populate(Properties props) {
		this.dataid = (UUID) props.get("dataid");
		this.ownerid = (UUID) props.get("ownerid");
		this.created = (DateTime) props.get("created");
		this.deviceName = (String) props.get("deviceName");
		this.description = (String) props.getProperty("description");

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
		long userPart = 4; //ownerid.getLeastSignificantBits();
		long date = dt.getMillis();
		long seed = userPart/2 + date/2;
		
		return seed;
	}

	private void log(String message) {
		log(LogService.LOG_INFO, message);
	}

	private void log(int level, String message) {
		if (log != null)
			log.log(level, message);
	}

	protected void bindLog(LogService ls) {
		log = ls;
	}

	protected void unbindLog(LogService ls) {
		log = null;
	}
}
