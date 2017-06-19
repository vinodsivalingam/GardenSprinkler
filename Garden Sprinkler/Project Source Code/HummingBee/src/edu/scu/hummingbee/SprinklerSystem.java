package edu.scu.hummingbee;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;
import java.util.stream.IntStream;

import javax.swing.SpinnerDateModel;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;

import com.jtheory.jdring.AlarmEntry;
import com.jtheory.jdring.AlarmListener;
import com.jtheory.jdring.AlarmManager;
import com.jtheory.jdring.PastDateException;
import com.jtheory.jdring.TemperatureAlarmEntry;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.JobKey.jobKey;

/**
 * @author team17 code
 * 
 */
public class SprinklerSystem implements SprinklerSystemController, AlarmListener {
	private SystemStatus Status;
	private List<Sprinkler> Sprinklers;
	private List<Schedule> Schedules;
	private HashMap<ScheduleType, List<Schedule>> ScheduleMap;
	private HashMap<Integer, Sprinkler> SprinklerMap;
	private HashMap<SprinklerGroup, List<Integer>> GroupMap;
	private HashMap<SprinklerGroup, Integer> TemperatureMap;
	private SchedulerFactory sf;// = new StdSchedulerFactory();
	private Scheduler sched; // = sf.getScheduler();
	private AlarmManager alarmManager;
	private AlarmListener TemperatureAlarmListener;

	private SprinklerActionListener listener;

	@Override
	public void initSprinklerSystem(SprinklerActionListener l) throws PastDateException {
		this.listener = l;
		this.TemperatureAlarmListener = new AlarmListener() {
			@Override
			public void handleAlarm(AlarmEntry entry) {
				int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);

				Random rand = new java.util.Random();

				// System.out.println(Constants.Default_Temperature[hour]);
				int globalTemperature = Constants.Default_Temperature[hour];
				for (SprinklerGroup g : SprinklerGroup.values()) {
					SprinklerSystem.this.setTemperature(g, globalTemperature); // +rand.nextInt(4)-2
					checkAlarmsForTempaerature(g, SprinklerSystem.this.getTemperature(g));
				}
			}
		};
		this.alarmManager = new AlarmManager();
		this.ScheduleMap = new HashMap<>();
		this.SprinklerMap = new HashMap<>();
		this.GroupMap = new HashMap<>();
		this.TemperatureMap = new HashMap<>();
		this.Sprinklers = new ArrayList();
		this.Schedules = new ArrayList<>();
		this.alarmManager.addAlarm(TemperatureAlarmEntry.create(this.TemperatureAlarmListener));
	}

	@Override
	public void initSprinklerSystem(SprinklerActionListener l, String FileName)
			throws PastDateException, FileNotFoundException, IOException {
		this.initSprinklerSystem(l);

		List<SerializbleSchedule> schedules = null;
		List<Sprinkler> sprinklers = null;
		ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(FileName));
		try {
			sprinklers = (List<Sprinkler>) objectInputStream.readObject();
		} catch (ClassNotFoundException e) {

			e.printStackTrace();
		}
		try {
			schedules = (List<SerializbleSchedule>) objectInputStream.readObject();
		} catch (ClassNotFoundException e) {

			e.printStackTrace();
		}

		if (sprinklers != null)
			for (Sprinkler s : sprinklers) {
				this.addSprinkler(s);
			}
		if (schedules != null)
			for (SerializbleSchedule s : schedules) {
				Schedule sc = new Schedule(s);
				sc.setListener(this.listener);
				this.addSchedule(sc);
			}

	}

	protected void checkAlarmsForTempaerature(SprinklerGroup g, int temperature) {
		List<Schedule> list = ScheduleMap.get(ScheduleType.TEMPERATURE);
		if (list == null || list.isEmpty())
			return;
		list.forEach(new Consumer<Schedule>() {

			@Override
			public void accept(Schedule t) {
				if (t.getThreshold_Temperature() == temperature && t.getSprinklerGroups() != null
						&& t.getSprinklerGroups().size() != 0 && t.getSprinklerGroups().contains(g)) {
					Calendar c = Calendar.getInstance();

					if (t.getDaysOfWeek().contains(c.get(Calendar.DAY_OF_WEEK)))
						t.execute(SprinklerSystem.this);
				}

			}
		});

	}

	@Override
	public void handleAlarm(AlarmEntry entry) {
		entry.getSchedule().execute(this);

	}

	@Override
	public List<Sprinkler> getAllSprinklers() {

		return Sprinklers;
	}

	@Override
	public List<Sprinkler> getAllSprinklersForGroup(SprinklerGroup group) {
		if (GroupMap == null || GroupMap.get(group) == null)
			return null;
		List<Sprinkler> sprinklers = new ArrayList<>();
		for (Integer i : GroupMap.get(group)) {
			sprinklers.add(SprinklerMap.get(i));
		}
		return sprinklers;
	}

	@Override
	public SprinklerStatus getSprinklerStatus(Sprinkler S) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SprinklerStatus getSprinklerStatus(int SprinklerID) throws SprinklerNotFoundException {
		// TODO Auto-generated method stub
		if (SprinklerMap.get(SprinklerID) == null)
			throw new SprinklerNotFoundException("Not Found");
		return null;
	}

	@Override
	public int getTemperature(SprinklerGroup group) {
		return TemperatureMap.getOrDefault(group, Constants.DEFAULT_TEMPERATURE);
	}

	@Override
	public void setTemperature(SprinklerGroup group, int Temperature) {
		// TODO trigger any temperature based schedules
		TemperatureMap.put(group, Temperature);
		checkAlarmsForTempaerature(group, SprinklerSystem.this.getTemperature(group));

	}

	@Override
	public List<Schedule> getSchedules() {

		return Schedules;
	}

	/**
	 * Create Schedule like this Schedule s = new Schedule(1, 26, 1, new
	 * int[]{0,1,2,3,4,5,6},new boolean[]{true,true,false,false}); and pass it
	 * here
	 * 
	 * @throws PastDateException
	 */
	@Override
	public void addSchedule(Schedule s) throws PastDateException {
		Schedules.add(s);
		s.setListener(this.listener);
		List<Schedule> mapList = ScheduleMap.getOrDefault(s.getType(), new ArrayList<>());
		mapList.add(s);
		ScheduleMap.put(s.getType(), mapList);
		int[] days = s.getDaysOfWeek().stream().mapToInt(i -> i).toArray();

		if (s.getType() == ScheduleType.TIMED) {

			this.alarmManager.addAlarm(s.getTimeOfDay() + ":" + s.getMinute(), new int[] { s.getMinute() },
					new int[] { s.getTimeOfDay() }, new int[] { -1 }, new int[] { -1 }, days, -1, this, s);
		} else if (s.getType() == ScheduleType.TEMPERATURE) {
			for (SprinklerGroup g : s.getSprinklerGroups()) {
				// checkAlarmsForTempaerature(g,s.getThreshold_Temperature());
			}
		}

	}

	@Override
	public void addSprinkler(Sprinkler s) {
		if (s == null)
			return;
		Sprinklers.add(s);
		List<Integer> list = GroupMap.getOrDefault(s.getGroup(), new ArrayList<>());
		list.add(s.getID());
		SprinklerMap.put(s.getID(), s);
		GroupMap.put(s.getGroup(), list);

	}

	@Override
	public void setActionListener(SprinklerActionListener listener) {
		this.listener = listener;

	}

	@Override
	public void turnSprinklersON(List<Sprinkler> sprinklers) {
		// TODO Turn Sprinklers ON
		for (Sprinkler s : sprinklers) {
			s.turnON(listener);
		}

	}

	@Override
	public void turnSprinklersON(SprinklerGroup group) {

		for (Integer i : GroupMap.get(group)) {
			Sprinkler s = getSprinkler(i);
			if (s == null)
				continue;
			s.turnON(listener);
		}

	}

	@Override
	public void turnSprinklersON(int[] sprinklerIDs) {
		for (int id : sprinklerIDs) {
			Sprinkler s = getSprinkler(id);
			if (s == null)
				continue;
			s.turnON(listener);
		}

	}

	@Override
	public void turnSprinklersOFF(List<Sprinkler> sprinklers) {

		for (Sprinkler s : sprinklers) {
			s.turnOFF(listener);
		}

	}

	@Override
	public void turnSprinklersOFF(SprinklerGroup group) {
		for (Integer i : GroupMap.get(group)) {
			Sprinkler s = getSprinkler(i);
			if (s == null)
				continue;
			s.turnOFF(listener);
		}

	}

	@Override
	public void turnSprinklersOFF(int[] sprinklerIDs) {
		for (int id : sprinklerIDs) {
			Sprinkler s = getSprinkler(id);
			if (s == null)
				continue;
			s.turnOFF(listener);
		}

	}

	@Override
	public int[] getWaterQuantities() {
		int[] quantities = new int[4];
		quantities[0] = this.getWaterQuantity(SprinklerGroup.North);
		quantities[1] = this.getWaterQuantity(SprinklerGroup.East);
		quantities[2] = this.getWaterQuantity(SprinklerGroup.West);
		quantities[3] = this.getWaterQuantity(SprinklerGroup.South);
		return quantities;
	}

	@Override
	public int getWaterQuantity(SprinklerGroup group) {
		int quantity = 0;
		List<Sprinkler> sprinklers = this.getAllSprinklersForGroup(group);
		for (Sprinkler s : sprinklers) {
			quantity += s.getConsumedQuantity();
		}
		return quantity;
	}

	@Override
	public Sprinkler getSprinkler(int id) {
		if (SprinklerMap == null)
			return null;
		else
			return SprinklerMap.getOrDefault(id, null);
	}

	@Override
	public void setSprinklerStatus(int SprinklerID, SprinklerStatus status) {
		Sprinkler s = getSprinkler(SprinklerID);
		if (s == null)
			return;
		s.setStatus(status);
	}

	@Override

	public void saveContents(String fileName) throws FileNotFoundException, IOException {
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(fileName));

		objectOutputStream.writeObject(this.getAllSprinklers());
		List<SerializbleSchedule> ss = new ArrayList<>();
		for (Schedule s : this.getSchedules()) {
			ss.add(new SerializbleSchedule(s));
		}
		objectOutputStream.writeObject(ss);

	}

}
