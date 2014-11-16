package ca.qc.johnabbott.ian.tasks;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

public class Task {

	//TODO: android.text has a DateFormat.. might be better
	public static DateFormat dateFormatter = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z");

	public enum Priority { LOW, MEDIUM, HIGH };
	public enum Status { OPEN, CLOSED };
	
	public static Task fromJSON(JSONObject o) throws JSONException, ParseException {
			return new Task(o.getString("author"),
					         o.getString("description"),
					         Task.dateFormatter.parse(o.getString("due")),
					         Task.Priority.valueOf(o.getString("priority")),
					         Task.Status.valueOf(o.getString("status")));
	}
	
	
	private String author, description;
	private Date due;
	private Priority priority;
	private Status status;
	
	public Task(String author, String description, Date due, Priority priority, Status status) {
		super();
		this.author = author;
		this.description = description;
		this.due = due;
		this.priority = priority;
		this.status = status;
	}

	public Status getStatus() {
		return status;
	}

	public String getAuthor() {
		return author;
	}

	public String getDescription() {
		return description;
	}

	public Date getDue() {
		return due;
	}

	public Priority getPriority() {
		return priority;
	}
	
	public JSONObject toJSON() {
		JSONObject o = new JSONObject();
		try {
			o.put("author", author);
			o.put("description", description);
			o.put("due", dateFormatter.format(due));
			o.put("priority", priority.name());
			o.put("status", status.name());
			return o;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
