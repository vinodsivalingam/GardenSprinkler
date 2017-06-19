package edu.scu.hummingbee;

import java.io.Serializable;
import java.util.List;
import java.util.TimerTask;
/**
 * 
 * @author team17 code
 * This class creates SerializbleSchedule cons
 */

public class SerializbleSchedule implements Serializable {
	private ScheduleType Type;
	private int TimeOfDay; // 0-23
	private int Minute; // 0-59
	private int Duration; // duration of the task
	private List<Integer> DaysOfWeek;
	private List<SprinklerGroup> SprinklerGroups; // in the order [N, E, W, S]

	public ScheduleType getType() {
		return Type;
	}

	public int getTimeOfDay() {
		return TimeOfDay;
	}

	public int getMinute() {
		return Minute;
	}

	public int getDuration() {
		return Duration;
	}

	public List<Integer> getDaysOfWeek() {
		return DaysOfWeek;
	}

	public List<SprinklerGroup> getSprinklerGroups() {
		return SprinklerGroups;
	}

	public List<Integer> getSprinklerIDs() {
		return SprinklerIDs;
	}

	public Integer getThreshold_Temperature() {
		return Threshold_Temperature;
	}

	private List<Integer> SprinklerIDs;
	private Integer Threshold_Temperature;
/**
 * 
 * @param type
 * @param timeOfDay
 * @param minute
 * @param duration
 * @param daysOfWeek
 * @param sprinklerGroups
 * @param sprinklerIDs
 * @param threshold_Temperature
 */
	public SerializbleSchedule(ScheduleType type, int timeOfDay, int minute, int duration, List<Integer> daysOfWeek,
			List<SprinklerGroup> sprinklerGroups, List<Integer> sprinklerIDs, Integer threshold_Temperature) {
		super();
		Type = type;
		TimeOfDay = timeOfDay;
		Minute = minute;
		Duration = duration;
		DaysOfWeek = daysOfWeek;
		SprinklerGroups = sprinklerGroups;
		SprinklerIDs = sprinklerIDs;
		Threshold_Temperature = threshold_Temperature;
	}
/**
 * @param s
 */
	public SerializbleSchedule(Schedule s) {
		Type = s.getType();
		TimeOfDay = s.getTimeOfDay();
		Minute = s.getMinute();
		Duration = s.getDuration();
		DaysOfWeek = s.getDaysOfWeek();
		SprinklerGroups = s.getSprinklerGroups();
		SprinklerIDs = s.getSprinklerIDs();
		Threshold_Temperature = s.getThreshold_Temperature();
	}

	

}
