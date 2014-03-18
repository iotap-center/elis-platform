package se.mah.elis.adaptor.utilityprovider.eon.internal.devices;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.simple.parser.ParseException;

import se.mah.elis.adaptor.device.api.data.DeviceIdentifier;
import se.mah.elis.adaptor.device.api.entities.devices.DeviceSet;
import se.mah.elis.adaptor.device.api.entities.devices.ElectricitySampler;
import se.mah.elis.adaptor.device.api.entities.devices.Gateway;
import se.mah.elis.adaptor.device.api.exceptions.SensorFailedException;
import se.mah.elis.adaptor.utilityprovider.eon.internal.gateway.EonGateway;
import se.mah.elis.data.ElectricitySample;
import se.mah.elis.data.OrderedProperties;
import se.mah.elis.exceptions.StaticEntityException;

/**
 * A virtual representation of the E.On power meter
 * 
 * @author Joakim Lithell
 * @author Marcus Ljungblad
 * @version 1.1.0
 * @since 1.0
 */

public class EonPowerMeter extends EonDevice implements ElectricitySampler {

	private static final int VALUETYPE_POWER = 0;
	private static DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm");
	
	protected boolean isOnline;
	protected EonGateway gateway;
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
		if (!(gateway instanceof EonGateway))
			throw new StaticEntityException();
		this.gateway = (EonGateway) gateway;
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
		double value;

		try {
			value = httpBridge.getPowerMeterKWh(
					this.gateway.getAuthenticationToken(), getGatewayAddress(),
					getId().toString());
		} catch (ParseException e) {
			throw new SensorFailedException();
		}

		ElectricitySample electricitySample = null;
		electricitySample = new ElectricitySampleImpl(value);

		return electricitySample;
	}

	public List<ElectricitySample> getSamples(DateTime from, DateTime to)
			throws SensorFailedException {
		List<ElectricitySample> samples = new ArrayList<ElectricitySample>();

		DateTime start = from;
		
		while (start.isBefore(to)) {
			try {
				List<Map<String, Object>> data = httpBridge.getStatData(
						this.gateway.getAuthenticationToken(), getGatewayAddress(),
						this.getId().toString(), formatDate(start), VALUETYPE_POWER);
				samples.addAll(convertToSamples(data, start));
			} catch (ParseException e) {
				throw new SensorFailedException();
			}
			
			start = start.plusDays(1);
			
			if (start.equals(to)) 
				break;
		}

		return samples;
	}

	private Collection<? extends ElectricitySample> convertToSamples(
			List<Map<String, Object>> data, DateTime from) {
		List<ElectricitySample> samples = new ArrayList<>();
		
		for (Map<String, Object> rawsample : data) {
			double value = number(rawsample.get("Value"));
			DateTime sampleTime = calculateSampleTime(from, (String) rawsample.get("Key"));
			ElectricitySample sample = new ElectricitySampleImpl(value, sampleTime);
			samples.add(sample);
		}
		
		return samples;
	}

	private DateTime calculateSampleTime(DateTime from, String offset) {
		int HOUR_START = 11;
		int HOUR_STOP = 13;
		int offsetInHours = Integer.parseInt(offset.substring(HOUR_START, HOUR_STOP));
		DateTime sampleTime = from.plusHours(offsetInHours);
		return sampleTime;
	}

	private double number(Object object) {
		double value = ((Number) object).doubleValue();
		return value;
	}

	public String formatDate(DateTime from) {
		return fmt.print(from);
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
		props.put("gateway", gateway.getDataId());
		return props;
	}

	@Override
	public OrderedProperties getPropertiesTemplate() {
		OrderedProperties props = new OrderedProperties();
		props.put("dataid", UUID.randomUUID());
		props.put("ownerid", UUID.randomUUID());
		props.put("created", created);
		props.put("identifier", new EonDeviceIdentifier("a"));
		props.put("device_name", "64");
		props.put("description", "256");
		props.put("gateway", UUID.randomUUID());
		return props;
	}

	@Override
	public void populate(Properties props) {
		this.dataid = (UUID) props.get("dataid");
		this.ownerid = (UUID) props.get("ownerid");
		this.created = (DateTime) props.get("created");
		this.deviceId = new EonDeviceIdentifier("");
		this.deviceName = (String) props.get("deviceName");
		this.description = (String) props.getProperty("description");
		this.deviceId.populate(props);

		// TODO Create gateway
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

}
