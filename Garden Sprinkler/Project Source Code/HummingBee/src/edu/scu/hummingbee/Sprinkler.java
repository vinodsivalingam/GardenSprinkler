package edu.scu.hummingbee;
import java.io.Serializable;
import java.util.Calendar;
/**
 * 
 * @author team17 code
 *
 */
public class Sprinkler implements SprinkerActions, Serializable {
public int getID() {
		return ID;
	}
	public String getName() {
		return Name;
	}
private int ID;
private String Name;
private SprinklerGroup Group;
private SprinklerStatus Status;
private int SprinkleRate;
private int ConsumedQuantity;
private long LastTurnOnTime;
public Sprinkler(int iD, String name, SprinklerGroup group, int sprinkleRate, SprinklerStatus status) {
	super();
	ID = iD;
	Name = name;
	Group = group;
	SprinkleRate = sprinkleRate;
	Status= status;
}
public SprinklerStatus getStatus() {
	return Status;
}
public void setStatus(SprinklerStatus status) {
	Status = status;
}
public int getSprinkleRate() {
	return SprinkleRate;
}
public void setSprinkleRate(int sprinkleRate) {
	SprinkleRate = sprinkleRate;
}
public int getConsumedQuantity() {
	return ConsumedQuantity;
}
public void setConsumedQuantity(int consumedQuantity) {
	ConsumedQuantity = consumedQuantity;
}
public long getLastTurnOnTime() {
	return LastTurnOnTime;
}
public void setLastTurnOnTime(long lastTurnOnTime) {
	LastTurnOnTime = lastTurnOnTime;
}
public SprinklerGroup getGroup() {
	return Group;
}
@Override
public void turnON(SprinklerActionListener l) {
	
	if(this.Status==SprinklerStatus.ON || this.Status==SprinklerStatus.NOTOK)
			return;
	this.Status=SprinklerStatus.ON;
	this.LastTurnOnTime=Calendar.getInstance().getTimeInMillis();
	l.SprinklerON(this);
	
	
	
}
@Override
public void turnOFF(SprinklerActionListener l) {
	if(this.Status==SprinklerStatus.OFF || this.Status==SprinklerStatus.NOTOK)
		return;
	this.Status=SprinklerStatus.OFF;
	long elapsed=Calendar.getInstance().getTimeInMillis()-this.LastTurnOnTime;
	float elapsedHours= (float)elapsed /(float)(1000*60*60);
	this.ConsumedQuantity+=elapsedHours*this.SprinkleRate;
	l.SprinklerOFF(this);
	
}
}
