package se.mah.elis.adaptor.water.mkb;

import java.util.List;
import java.util.Properties;
import java.util.UUID;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.ReferenceCardinality;
import org.apache.felix.scr.annotations.ReferencePolicy;
import org.joda.time.DateTime;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.log.LogService;

import se.mah.elis.adaptor.device.api.data.DeviceIdentifier;
import se.mah.elis.adaptor.device.api.entities.devices.DeviceSet;
import se.mah.elis.adaptor.device.api.entities.devices.Gateway;
import se.mah.elis.adaptor.device.api.entities.devices.WaterMeterSampler;
import se.mah.elis.adaptor.device.api.exceptions.SensorFailedException;
import se.mah.elis.adaptor.water.mkb.data.WaterData;
import se.mah.elis.adaptor.water.mkb.data.WaterDataPoint;
import se.mah.elis.adaptor.water.mkb.data.WaterDataService;
import se.mah.elis.data.OrderedProperties;
import se.mah.elis.data.WaterSample;
import se.mah.elis.exceptions.StaticEntityException;

/**
 * 
 * MKB Water Meter is a virtual representation of the water meter located in
 * users' apartments. It is primarily used to retrieve {@link MkbWaterSample}s.
 * 
 * @author Marcus Ljungblad
 * @since 1.0
 * @version 1.0
 *
 */
@Component
public class MkbWaterMeter implements WaterMeterSampler {

	private static final long serialVersionUID = 1483471192257524353L;
	private DeviceIdentifier deviceId;
	private String deviceName;
	private String description;
	private Gateway gateway;
	private UUID uuid;
	private boolean isOnline;

	@Reference(policy = ReferencePolicy.DYNAMIC, 
			cardinality = ReferenceCardinality.OPTIONAL_UNARY)
	private WaterDataService waterDataSource;
	
	@Reference
	private LogService log; 
	
	private UUID userOwner;
	private static ComponentContext ctx;

	public MkbWaterMeter() { } 
	
	protected MkbWaterMeter(WaterDataService wds) {
		waterDataSource = wds;
		isOnline = true;
	}
	
	protected void bindWaterDataSource(ServiceReference source) { }

	protected void unbindWaterDataSource(WaterDataService source) {	}
	
	@Activate
	public void activate(ComponentContext context) {
		ctx = context;
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
	public WaterSample getSample() throws SensorFailedException {
		setWaterSource();
		if (!isOnline)
			throw new SensorFailedException();
		return getLatestSample();
	}

	private WaterSample getLatestSample() throws SensorFailedException {
		WaterSample sample = null;
		WaterData service = waterDataSource.getInstance();
		
		if (service != null) {
			WaterDataPoint latestPoint = service.getLatestSample(getName());
			sample = new MkbWaterSample(latestPoint);
		} else {
			log(LogService.LOG_ERROR, "No WaterDataService available despite setting source.");
			throw new SensorFailedException();
		}
		
		return sample;
	}

	@Override
	public WaterSample getSample(DateTime from, DateTime to)
			throws SensorFailedException {
		setWaterSource();
		if (!isOnline)
			throw new SensorFailedException();
		return getRangeSample(from, to);
	}

	private void setWaterSource() throws SensorFailedException {
		if (waterDataSource == null) {
			if (ctx != null) {
				BundleContext bundleContext = ctx.getBundleContext();
				ServiceReference ref = bundleContext.getServiceReference(WaterDataService.class.getName());
				if (ref != null) {
					waterDataSource = (WaterDataService) bundleContext.getService(ref);
					isOnline = true;
					log(LogService.LOG_DEBUG, "Installed WaterDataService with meter");
				}
			} else {
				log(LogService.LOG_ERROR, "No BundleContext available. Cannot find WaterDataService");
				throw new SensorFailedException();
			}
		}
	}

	private WaterSample getRangeSample(DateTime from, DateTime to) {
		List<WaterDataPoint> points = waterDataSource.getInstance().getRange(
				getName(), from, to);
		WaterSample sample = new MkbWaterSample(points);
		return sample;
	}

	@Override
	public UUID getDataId() {
		return uuid;
	}

	@Override
	public void setDataId(UUID uuid) {
		this.uuid = uuid;
	}

	@Override
	public void setOwnerId(UUID userId) {
		this.userOwner = userId;
	}

	@Override
	public UUID getOwnerId() {
		return userOwner;
	}

	@Override
	public DateTime created() {
		return null;
	}

	protected void bindLog(LogService ls) {
		log = ls;
	}
	
	protected void unbindLog(LogService ls) {
		log = null;
	}
	
	private void log(int level, String message) {
		if (log != null)
			log.log(level, message);
	}
}
