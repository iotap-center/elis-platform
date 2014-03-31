package se.mah.elis.adaptor.water.mkb;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.joda.time.DateTime;
import org.osgi.service.log.LogService;

import se.mah.elis.adaptor.water.mkb.data.WaterDataPoint;
import se.mah.elis.data.OrderedProperties;
import se.mah.elis.data.WaterSample;

/**
 * Water sample from an MKB water meter. 
 * 
 * @author Marcus Ljungblad
 * @since 1.0
 * @version 1.0
 *
 */
@Component
public class MkbWaterSample implements WaterSample {

	@Reference
	private LogService log;
	
	private static final long serialVersionUID = 7438609549499091999L;
	private float volume;
	private UUID uuid;
	private DateTime sampleTimestamp;
	private int sampleLength;
	private UUID userOwner;
	
	public MkbWaterSample() { }

	public MkbWaterSample(WaterDataPoint point) {
		this.volume = point.getValue();
		this.sampleTimestamp = point.getRecordedDateTime();
		this.sampleLength = 0;
	}

	public MkbWaterSample(List<WaterDataPoint> points) {
		if (points.size() > 0) {
			WaterDataPoint firstPoint = points.get(0);
			WaterDataPoint lastPoint = points.get(points.size() - 1);
			
			this.sampleLength = (int) calculateLength(firstPoint, lastPoint);
			this.volume = calculateTotalVolume(firstPoint, lastPoint);
			this.sampleTimestamp = lastPoint.getRecordedDateTime();
		} else {
			log(LogService.LOG_INFO, "Sample does not contain any data points");
		}
	}

	private float calculateTotalVolume(WaterDataPoint firstPoint,
			WaterDataPoint lastPoint) {
		return lastPoint.getValue() - firstPoint.getValue();
	}

	private long calculateLength(WaterDataPoint firstPoint,
			WaterDataPoint lastPoint) {
		long end = lastPoint.getRecordedDateTime().toInstant().getMillis();
		long start = firstPoint.getRecordedDateTime().toInstant().getMillis();
		return end - start;
	}

	@Override
	public long getSampleLength() {
		return sampleLength;
	}

	@Override
	public DateTime getSampleTimestamp() {
		return sampleTimestamp;
	}

	@Override
	public List<String> getTraversableMethods() {
		List<String> methods = new ArrayList<>();
		methods.add("getVolume");
		return methods;
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
	public float getVolume() {
		return volume;
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
		return sampleTimestamp;
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
