package yy;
import java.util.Calendar;
import java.util.Date;

public class Watch {
	private long startTime;
	private long endTime;
	public void start(){
		startTime=Calendar.getInstance().getTimeInMillis();
	}
	public void end(){
		endTime=Calendar.getInstance().getTimeInMillis();
	}
	public long getTime(){
		long reLong=endTime-startTime;
		return reLong;
	}
	public long getNowTime(){
		long reLong=Calendar.getInstance().getTimeInMillis()-startTime;
		return reLong;
	}
}
