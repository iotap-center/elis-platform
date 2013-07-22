/**
 * 
 */
package se.mah.elis.services.automation.scheduling;

/**
 * 
 * 
 * @author "Johan Holmberg, Malmö University"
 */
public interface ScheduleExecutor {
	void startSchedule(Schedule schedule);
	void stopSchedule(Schedule schedule);
}
