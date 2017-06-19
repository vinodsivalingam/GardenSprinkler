
package com.jtheory.jdring;
import java.util.Calendar;
import java.util.Date;

import edu.scu.hummingbee.Schedule;
/**
 * @author team17
 * This class extends the AlarmEntry class , representing additional attributes of an alarm for the temperature schedule.
 */
public class TemperatureAlarmEntry extends AlarmEntry {
	/**
	 * 
	 * @param _listener
	 * @return
	 * @throws PastDateException
	 */
	public static TemperatureAlarmEntry create(AlarmListener _listener) throws PastDateException{
		
		// this is to start the temperature controller like it occurs naturally
		int minute= Calendar.getInstance().get(Calendar.MINUTE);
		return new TemperatureAlarmEntry("Temperature_Nature", minute+2, -1, -1, -1, -1, -1, _listener);
		
	}
/**
 * 
 * @param _name
 * @param _date
 * @param _listener
 * @throws PastDateException
 */
	private TemperatureAlarmEntry(String _name, Date _date, AlarmListener _listener) throws PastDateException {
		super(_name, _date, _listener);
		
	}
/**
 * 
 * @param _name
 * @param _delayMinutes
 * @param _isRepeating
 * @param _listener
 * @throws PastDateException
 */
	private TemperatureAlarmEntry(String _name, int _delayMinutes, boolean _isRepeating, AlarmListener _listener)
			throws PastDateException {
		super(_name, _delayMinutes, _isRepeating, _listener);
		
	}
	/**
	 * 
	 * @param _name
	 * @param _minute
	 * @param _hour
	 * @param _dayOfMonth
	 * @param _month
	 * @param _dayOfWeek
	 * @param _year
	 * @param _listener
	 * @throws PastDateException
	 */

	private TemperatureAlarmEntry(String _name, int _minute, int _hour, int _dayOfMonth, int _month, int _dayOfWeek,
			int _year, AlarmListener _listener) throws PastDateException {
		super(_name, _minute, _hour, _dayOfMonth, _month, _dayOfWeek, _year, _listener);
		
	}
	/**
	 * 
	 * @param _name
	 * @param _minutes
	 * @param _hours
	 * @param _daysOfMonth
	 * @param _months
	 * @param _daysOfWeek
	 * @param _year
	 * @param _listener
	 * @param s
	 * @throws PastDateException
	 */

	private TemperatureAlarmEntry(String _name, int[] _minutes, int[] _hours, int[] _daysOfMonth, int[] _months,
			int[] _daysOfWeek, int _year, AlarmListener _listener, Schedule s) throws PastDateException {
		super(_name, _minutes, _hours, _daysOfMonth, _months, _daysOfWeek, _year, _listener, s);
		
	}
	/**
	 * 
	 * @param _name
	 * @param _minutes
	 * @param _hours
	 * @param _daysOfMonth
	 * @param _months
	 * @param _daysOfWeek
	 * @param _year
	 * @param _listener
	 * @throws PastDateException
	 */

	private TemperatureAlarmEntry(String _name, int[] _minutes, int[] _hours, int[] _daysOfMonth, int[] _months,
			int[] _daysOfWeek, int _year, AlarmListener _listener) throws PastDateException {
		super(_name, _minutes, _hours, _daysOfMonth, _months, _daysOfWeek, _year, _listener);
		
	}


	

}
