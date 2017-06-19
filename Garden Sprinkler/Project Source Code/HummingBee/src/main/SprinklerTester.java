package main;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;

import org.quartz.SchedulerException;

import com.jtheory.jdring.PastDateException;

import edu.scu.hummingbee.Schedule;
import edu.scu.hummingbee.Sprinkler;
import edu.scu.hummingbee.SprinklerActionListener;
import edu.scu.hummingbee.SprinklerGroup;
import edu.scu.hummingbee.SprinklerStatus;
import edu.scu.hummingbee.SprinklerSystem;
import edu.scu.hummingbee.SprinklerSystemController;

public class SprinklerTester {
	SprinklerSystemController controller;
	SprinklerActionListener listener;
	
	

	public SprinklerTester() throws PastDateException, FileNotFoundException, IOException   {
		super();
		this.listener= new SprinklerActionListener() {
			
			@Override
			public void SprinklerON(Sprinkler s) {
				System.out.println("Sprinkler "+s.getName()+"Turned ON");
				
			}
			
			@Override
			public void SprinklerOFF(Sprinkler s) {
				System.out.println("Sprinkler "+s.getName()+"Turned OFF");
				
			}
		};
		
		this.controller= new SprinklerSystem();
		
		this.controller.initSprinklerSystem(listener); //,"db.txt"
		
		System.out.println(this.controller.getAllSprinklers().size());
		
		this.controller.addSprinkler(new Sprinkler(1, "1", SprinklerGroup.North, 100,SprinklerStatus.OFF));
		this.controller.addSprinkler(new Sprinkler(2, "2", SprinklerGroup.North, 100,SprinklerStatus.OFF));
		this.controller.addSprinkler(new Sprinkler(3, "3", SprinklerGroup.South, 100,SprinklerStatus.OFF));
		this.controller.addSprinkler(new Sprinkler(4, "4", SprinklerGroup.South, 100,SprinklerStatus.OFF));
		this.controller.addSprinkler(new Sprinkler(5, "5", SprinklerGroup.West, 100,SprinklerStatus.OFF));
		this.controller.addSprinkler(new Sprinkler(6, "6", SprinklerGroup.West, 100,SprinklerStatus.OFF));
		this.controller.addSprinkler(new Sprinkler(7, "7", SprinklerGroup.East, 100,SprinklerStatus.OFF));
		this.controller.addSprinkler(new Sprinkler(8, "8", SprinklerGroup.East, 100,SprinklerStatus.NOTOK));
		
	}
	
	public void setSchedule() throws PastDateException{
		/*Schedule s= new Schedule(14, 07, 1, new int[]{Calendar.MONDAY,Calendar.TUESDAY,Calendar.THURSDAY,Calendar.WEDNESDAY,Calendar.FRIDAY},
				new SprinklerGroup[]{SprinklerGroup.North});
		this.controller.addSchedule(s);*/
		
		Schedule s= new Schedule(1,Arrays.asList(new Integer[]{Calendar.MONDAY,Calendar.TUESDAY,Calendar.THURSDAY,Calendar.WEDNESDAY,Calendar.FRIDAY}),
				Arrays.asList(new SprinklerGroup[]{SprinklerGroup.North}),62);
			this.controller.addSchedule(s);
	}
	
	public void save() throws FileNotFoundException, IOException{
		
		(this.controller).saveContents("db.txt");
	}



	public static void main(String[] args) throws PastDateException, FileNotFoundException, IOException {
		// TODO Auto-generated method stub
		SprinklerTester tester= new SprinklerTester();
	//	tester.setSchedule();
		tester.save();
	}

}
