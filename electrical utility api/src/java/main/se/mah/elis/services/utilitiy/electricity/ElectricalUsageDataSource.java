package se.mah.elis.services.utilitiy.electricity;

import java.util.Date;

import se.mah.elis.adaptor.building.api.entities.devices.Device;
import se.mah.elis.adaptor.building.api.entities.devices.DeviceSet;
import se.mah.elis.adaptor.building.api.plan.Apartment;
import se.mah.elis.auxiliaries.data.ElectricitySample;
import se.mah.elis.auxiliaries.exceptions.OutOfDateRangeException;

/**
 * The ElectricalUsageDataSource interface provides a set of methods used to
 * retrieve historical electricity usage data.
 * 
 * @since 1.0
 * @author "Johan Holmberg, Malmö University"
 */
public interface ElectricalUsageDataSource {
	
	/**
	 * Fetches accumulated energy usage data from a single device, wrapped in
	 * an ElectricitySample object.
	 * 
	 * @param device The device for which we want to fetch energy usage data.
	 * @param from From what date we want to fetch data.
	 * @param to Up until what date we want to fetch data.
	 * @return The energy data encapsulated in an ElectricitySample object.
	 * @throws OutOfDateRangeException if the date given is in the future or
	 * 		   simply older than the earliest data available.
	 * @since 1.0
	 */
	ElectricitySample fetchAccumulatedData(Device device, Date from, Date to)
			throws OutOfDateRangeException;
	
	/**
	 * Fetches accumulated energy usage data from a single device, wrapped in
	 * an ElectricitySample object.
	 * 
	 * @param set The device set for which we want to fetch energy usage data.
	 * @param from From what date we want to fetch data.
	 * @param to Up until what date we want to fetch data.
	 * @return The energy data encapsulated in an ElectricitySample object.
	 * @throws OutOfDateRangeException if the date given is in the future or
	 * 		   simply older than the earliest data available.
	 * @since 1.0
	 */
	ElectricitySample fetchAccumulatedData(DeviceSet set, Date from, Date to)
				throws OutOfDateRangeException;
	
	/**
	 * Fetches accumulated energy usage data from a single device, wrapped in
	 * an ElectricitySample object.
	 * 
	 * @param apartment The apartment for which we want to fetch energy usage
	 * 		  data.
	 * @param from From what date we want to fetch data.
	 * @param to Up until what date we want to fetch data.
	 * @return The energy data encapsulated in an ElectricitySample object.
	 * @throws OutOfDateRangeException if the date given is in the future or
	 * 		   simply older than the earliest data available.
	 * @since 1.0
	 */
	ElectricitySample fetchAccumulatedData(Apartment apartment, Date from, Date to);

	/**
	 * Fetches energy usage data from a single device, presented as a set of
	 * ElectricitySample objects, each corresponding to a defined period of
	 * time.
	 * 
	 * @param device The device for which we want to fetch energy usage data.
	 * @param from From what date we want to fetch data.
	 * @param to Up until what date we want to fetch data.
	 * @param interval The interval length in milliseconds.
	 * @return The energy data encapsulated in a set of ElectricitySample
	 * 		   objects.
	 * @throws OutOfDateRangeException if the date given is in the future or
	 * 		   simply older than the earliest data available.
	 * @since 1.0
	 */
	ElectricitySample[] fetchGranularData(Device device, Date from, Date to,
			int interval) throws OutOfDateRangeException;
	
	/**
	 * Fetches energy usage data for a set of devices, presented as a set of
	 * ElectricitySample objects, each corresponding to a defined period of
	 * time.
	 * 
	 * @param set The device set for which we want to fetch energy usage data.
	 * @param from From what date we want to fetch data.
	 * @param to Up until what date we want to fetch data.
	 * @param interval The interval length in milliseconds.
	 * @return The energy data encapsulated in a set of ElectricitySample
	 * 		   objects.
	 * @throws OutOfDateRangeException if the date given is in the future or
	 * 		   simply older than the earliest data available.
	 * @since 1.0
	 */
	ElectricitySample[] fetchGranularData(DeviceSet set, Date from, Date to,
			int interval) throws OutOfDateRangeException;
	
	/**
	 * Fetches energy usage data for an apartment, presented as a set of
	 * ElectricitySample objects, each corresponding to a defined period of
	 * time.
	 * 
	 * @param apartment The apartment for which we want to fetch energy usage
	 * 		  data.
	 * @param from From what date we want to fetch data.
	 * @param to Up until what date we want to fetch data.
	 * @param interval The interval length in milliseconds.
	 * @return The energy data encapsulated in a set of ElectricitySample
	 * 		   objects.
	 * @throws OutOfDateRangeException if the date given is in the future or
	 * 		   simply older than the earliest data available.
	 * @since 1.0
	 */
	ElectricitySample[] fetchGranularData(Apartment apartment, Date from, Date to,
			int interval) throws OutOfDateRangeException;
}
