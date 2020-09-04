package fiddle;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;

public class DateboxViewModel {
	
	DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	
	private Date date = new Date();
	
	private Timestamp timestamp = new Timestamp(date.getTime());
	
	private String message;
	
	@Command("click")
	@NotifyChange("message")
	public void onClicked() {
		String dateString = "date: " + dateFormat.format(date);
		String timestampString = "timestamp: " + dateFormat.format(timestamp);
		
		message= dateString + ", " + timestampString;
		
		System.out.println(dateString);
		System.out.println(timestampString);
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	public Timestamp getTimestamp() {
		return timestamp;
	}
	
	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}

	public String getMessage() {
		return message;
	}
}
