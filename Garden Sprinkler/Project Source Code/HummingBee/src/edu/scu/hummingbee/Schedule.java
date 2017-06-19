package edu.scu.hummingbee;

import java.io.Serializable;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.mchange.v2.codegen.bean.SerializableExtension;
/**
 * 
 *@author team17 code
 *This class has the constructors for both time and temperature based schedules.
 *It has the methods to turn on and off the sprinkler and sprinkler groups.
 */
public class Schedule{
	private ScheduleType Type;
	private int TimeOfDay; // 0-23
	private int Minute; //0-59
	private int Duration; // duration of the task
	private List<Integer> DaysOfWeek;
	private List<SprinklerGroup> SprinklerGroups; // in the order [N, E, W, S]
	public List<SprinklerGroup> getSprinklerGroups() {
		return SprinklerGroups;
	}
	private List<Integer> SprinklerIDs;
	private Integer Threshold_Temperature;
	private SprinklerActionListener listener;
	private TimerTask TaskForTheDuration;
	
	
	// schedule constructor for sprinkler group-TIMED
	/**
	 * 
	 * @param timeOfDay
	 * @param minute
	 * @param duration
	 * @param daysOfWeek
	 * @param sprinklerGroups
	 */
	public Schedule(int timeOfDay, int minute, int duration, List<Integer> daysOfWeek, List<SprinklerGroup> sprinklerGroups) {
		super();
		
			TimeOfDay = timeOfDay;
			Minute = minute;
			Duration = duration;
			DaysOfWeek = daysOfWeek;
			SprinklerGroups = sprinklerGroups;
			Type=ScheduleType.TIMED;	
		
	}
	// schedule constructor for sprinkler ID -TIMED
	/**
	 * 
	 * @param SprinklerIDs
	 * @param timeOfDay
	 * @param minute
	 * @param duration
	 * @param daysOfWeek
	 */
	public Schedule(List<Integer> SprinklerIDs,int timeOfDay, int minute, int duration, List<Integer> daysOfWeek) {
		super();
		TimeOfDay = timeOfDay;
		Minute = minute;
		Duration = duration;
		DaysOfWeek = daysOfWeek;
		SprinklerIDs = SprinklerIDs;
		Type=ScheduleType.TIMED;
	}
	/**
	 * 
	 * @param duration
	 * @param daysOfWeek
	 * @param sprinklerGroups
	 * @param threshold_Temperature
	 */
	public Schedule(int duration, List<Integer> daysOfWeek, List<SprinklerGroup> sprinklerGroups, int threshold_Temperature) {
		super();
		Duration = duration;
		DaysOfWeek = daysOfWeek;
		SprinklerGroups = sprinklerGroups;
		Threshold_Temperature = threshold_Temperature;
		Type=ScheduleType.TEMPERATURE;
	}
	/**
	 * 
	 * @param daysOfWeek
	 * @param duration
	 * @param SprinklerIDs
	 * @param threshold_Temperature
	 */
	public Schedule(List<Integer> daysOfWeek,int duration,  List<Integer> SprinklerIDs, Integer threshold_Temperature) {
		super();
		Duration = duration;
		DaysOfWeek = daysOfWeek;
		SprinklerIDs = SprinklerIDs;
		Threshold_Temperature = threshold_Temperature;
		Type=ScheduleType.TEMPERATURE;
	}
	public ScheduleType getType() {
		return Type;
	}
/**
 * @param c
 */
	public void execute(SprinklerSystemController c) {
		
		//turn on timer task for all Sprinklers attached;
		System.out.println("Turning on all Sprinklers for the Schedule");
		if(SprinklerGroups!=null && SprinklerGroups.size()>0){
			for(SprinklerGroup g:SprinklerGroups){
				c.getAllSprinklersForGroup(g).forEach(new Consumer<Sprinkler>() {
					@Override
					public void accept(Sprinkler t) {
						t.turnON(Schedule.this.getListener());
						
					}
				});
			}
		}
		if(SprinklerIDs!=null && SprinklerIDs.size()>0){
			SprinklerIDs.forEach(new Consumer<Integer>() {

				@Override
				public void accept(Integer t) {
					c.getSprinkler(t).turnON(Schedule.this.getListener());
					
				}
			});
		}
		
		TaskForTheDuration = new TimerTask() {
			
			@Override
			public void run() {
				
				// turn off the sprinklers after timer for the duration is done
				
				System.out.println("Turning off all Sprinklers for the Schedule ");
				if(SprinklerGroups!=null && SprinklerGroups.size()>0){
					for(SprinklerGroup g:SprinklerGroups){
						c.getAllSprinklersForGroup(g).forEach(new Consumer<Sprinkler>() {
							@Override
							public void accept(Sprinkler t) {
								t.turnOFF(Schedule.this.getListener());
								
							}
						});
					}
				}
				if(SprinklerIDs!=null && SprinklerIDs.size()>0){
					SprinklerIDs.forEach(new Consumer<Integer>() {

						@Override
						public void accept(Integer t) {
							c.getSprinkler(t).turnOFF(Schedule.this.getListener());
						}
					});
				}
				
			}
		};
		
		Timer timer= new java.util.Timer(); 
		timer.schedule(TaskForTheDuration, Duration*60*1000);		
	}
	public SprinklerActionListener getListener() {
		return listener;
	}
	public void setListener(SprinklerActionListener listener) {
		this.listener = listener;
	}
	public int getTimeOfDay() {
		return TimeOfDay;
	}
	public int getMinute() {
		return Minute;
	}
	public List<Integer>  getDaysOfWeek() {
		return DaysOfWeek;
	}
	public Integer getThreshold_Temperature() {
		return Threshold_Temperature;
	}
	public int getDuration() {
		// TODO Auto-generated method stub
		return this.Duration;
	}
	public List<Integer> getSprinklerIDs() {
		// TODO Auto-generated method stub
		return this.SprinklerIDs;
	}
	/**
	 * 
	 * @param s
	 */
	
	public Schedule( SerializbleSchedule s){
		if(s.getType()==ScheduleType.TIMED){
			this.Type= ScheduleType.TIMED;
			this.Minute= s.getMinute();
			this.TimeOfDay= s.getTimeOfDay();
		}
		else{
			this.Threshold_Temperature=s.getThreshold_Temperature();
		}
		this.Duration= s.getDuration();
		this.DaysOfWeek= s.getDaysOfWeek();
		this.SprinklerGroups= s.getSprinklerGroups();
		this.SprinklerIDs= s.getSprinklerIDs();
		
	}
	
	
	
	

}
