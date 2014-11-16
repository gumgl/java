package ca.qc.johnabbott.ian.tasks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.json.JSONArray;

public class TaskList {
	private ArrayList<Task> tasks;
	
	
	public TaskList() {
		tasks = new ArrayList<Task>();
	}
	
	public boolean add(Task task) {
		return tasks.add(task);
	}
	
	public Task get(int pos) {
		return tasks.get(pos);
	}
	
	public int size() {
		return tasks.size();
	}
	
	public void sort(Comparator<Task> comp) {
		Collections.sort(tasks, comp);
	}
	
	public JSONArray toJSON() {
		JSONArray a = new JSONArray();
		for(Task t : tasks) {
			a.put(t.toJSON());
		}
		return a;
	}
	
}
