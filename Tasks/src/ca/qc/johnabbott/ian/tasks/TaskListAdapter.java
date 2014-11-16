package ca.qc.johnabbott.ian.tasks;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

final class TaskListAdapter implements ListAdapter {
	/**
	 * 
	 */
	private Context context;
	private TaskList taskList;

	/**
	 * @param taskListActivity
	 */
	TaskListAdapter(TaskList taskList, Context context) {
		this.taskList = taskList;
		this.context = context;
	}

	@Override
	public int getCount() {
		return taskList.size();
	}

	@Override
	public Object getItem(int pos) {
		return taskList.get(pos);
	}

	@Override
	public long getItemId(int pos) {
		return pos;
	}

	@Override
	public int getItemViewType(int pos) {
		return IGNORE_ITEM_VIEW_TYPE;
	}

	@Override
	public View getView(int pos, View convert, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.view_task, null);
		TextView descriptionTextView = (TextView)view.findViewById(R.id.descriptionTextView);
		TextView authorTextView = (TextView)view.findViewById(R.id.authorTextView);
		TextView dueTextView = (TextView)view.findViewById(R.id.dueTextView);
		
		descriptionTextView.setText(taskList.get(pos).getDescription());
		authorTextView.setText(taskList.get(pos).getAuthor());
		dueTextView.setText(taskList.get(pos).getDue().toString());
		
		return view;
	}

	@Override
	public int getViewTypeCount() {
		return 1;
	}

	@Override
	public boolean hasStableIds() { 
		return false;
	}

	@Override
	public boolean isEmpty() {
		return taskList.size() == 0;
	}

	@Override
	public void registerDataSetObserver(DataSetObserver arg0) {
	}

	@Override
	public void unregisterDataSetObserver(DataSetObserver arg0) {
	}

	@Override
	public boolean areAllItemsEnabled() {
		return true;
	}

	@Override
	public boolean isEnabled(int pos) {
		return true;
	}
}