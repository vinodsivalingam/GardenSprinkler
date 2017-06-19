/*
 *  com/jtheory/jdring/Test.java
 *  Copyright (C) 1999 - 2004 jtheory creations, Olivier Dedieu et al.
 *
 *  This library is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU Library General Public License as published
 *  by the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Library General Public License for more details.
 *
 *  You should have received a copy of the GNU Library General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */

package main;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Iterator;

import com.jtheory.jdring.*;

import edu.scu.hummingbee.Constants;
import edu.scu.hummingbee.Schedule;
import edu.scu.hummingbee.SprinklerGroup;
/**
  * This class run a bunch of tests.
  *
  * @author  Olivier Dedieu
  * @version 1.1, 09/13/1999
  */
public class Test {
  private static final AlarmManager mgr=new AlarmManager();

public static void main(String[] args) throws Exception {

   

    long current = System.currentTimeMillis();
    System.out.println("Current date is " + new Date(current));

    AlarmListener listener = new AlarmListener() {
      public void handleAlarm(AlarmEntry entry) {
        System.out.println("\u0007fixed date alarm : " + entry);
      }
    };
    Schedule s = new Schedule(1, 26, 1, Arrays.asList(new Integer[]{0,1,2,3,4,5,6}),Arrays.asList(new SprinklerGroup[]{SprinklerGroup.North,SprinklerGroup.South}));     
    int[] days=  s.getDaysOfWeek().stream().mapToInt(i->i).toArray();;
    /*mgr.addAlarm("ComplexCron2",new int[]{s.getMinute()}, new int[]{s.getTimeOfDay()}, new int[]{-1}, new int[]{-1},days, -1, new AlarmListener() {
      public void handleAlarm(AlarmEntry entry) {
        System.out.println("\u0007Cron complex2 (" + new Date() + ")");
       // entry.getSchedule().execute(null);
      }
    },s);*/
   
    
    mgr.addAlarm( TemperatureAlarmEntry.create(new AlarmListener() {
		
		@Override
		public void handleAlarm(AlarmEntry entry) {
			int hour= Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
			System.out.println(Constants.Default_Temperature[hour]);
			
		}
	}));
    
    System.out.println("Here are the registered alarms: ");
    System.out.println("----------------------------");
    List list = mgr.getAllAlarms();
    for(Iterator it = list.iterator(); it.hasNext();) {
      System.out.println("- " + it.next());
    }
    System.out.println("----------------------------");
    
    
  }
}

