package edu.scu.hummingbee;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.quartz.SchedulerException;

import com.jtheory.jdring.PastDateException;
/**
 * 
 * @author team17
 * 
 */
public interface SprinklerSystemController {
		void initSprinklerSystem(SprinklerActionListener l) throws PastDateException;
		void initSprinklerSystem(SprinklerActionListener l,String FileName) throws PastDateException, FileNotFoundException, IOException;
		void saveContents(String FileName) throws FileNotFoundException, IOException;
		List<Sprinkler> getAllSprinklers();
		Sprinkler getSprinkler(int id);
		List<Sprinkler> getAllSprinklersForGroup(SprinklerGroup group);
		SprinklerStatus getSprinklerStatus(Sprinkler S);
		SprinklerStatus getSprinklerStatus(int SprinklerID) throws SprinklerNotFoundException;
		//SprinklerStatus getSprinklerStatus(String name);
		int getTemperature(SprinklerGroup group);
		void setTemperature(SprinklerGroup group,int Temperature);
		void setSprinklerStatus(int SprinklerID, SprinklerStatus status);
		
		List<Schedule> getSchedules();
		void addSchedule(Schedule s) throws PastDateException;
		void addSprinkler(Sprinkler s);
		void setActionListener(SprinklerActionListener listener);
		void turnSprinklersON(List<Sprinkler> sprinklers);
		void turnSprinklersON(SprinklerGroup group );
		void turnSprinklersON(int[] sprinklerIDs);
		void turnSprinklersOFF(List<Sprinkler> sprinklers);
		void turnSprinklersOFF(SprinklerGroup group );
		void turnSprinklersOFF(int[] sprinklerIDs);
		public int[] getWaterQuantities();
		public int getWaterQuantity(SprinklerGroup group);
		
		
}
