package Logging;

import java.time.LocalDateTime;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

import bl.WarTime;

public class WarFormatter extends Formatter {

	@Override
	public String format(LogRecord rec) {
		StringBuffer buf = new StringBuffer(1000);
 
		
		LocalDateTime date = java.time.LocalDateTime.now();
		
		buf.append(date.getDayOfMonth() + "/" + date.getMonthValue() + "/" + date.getYear() + " ");
		buf.append(date.getHour() + ":" + date.getMinute() + ":" + date.getSecond() + "\n");
		//buf.append("War time: " + WarTime)
		buf.append(formatMessage(rec) + "\n");
        
        return buf.toString();
	}
}