package ca.qc.johnabbott.ian.tasks;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

public class TaskCreateActivity extends Activity {

    private boolean connected = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_task_create);
		System.out.println("hellogumgl!");
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.task_create, menu);
		return true;
	}
	
	public void buttonAdd(View v){
		AddTask addTask = new AddTask();
		addTask.execute();
	}
	
	public class AddTask extends AsyncTask<Void, Void, Boolean> {
		public Boolean success = false;
		public Exception error = null;
		@Override
		protected Boolean doInBackground(Void... arg0) {
			
			try {
				EditText descriptionEditText = (EditText) findViewById(R.id.descriptionEditText);
				String description = descriptionEditText.getText().toString();
	
				EditText authorEditText = (EditText) findViewById(R.id.authorEditText);
				String author = authorEditText.getText().toString();
	
				EditText dueEditText = (EditText) findViewById(R.id.dueEditText);
				DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				Date due;
					due = dateFormatter.parse(dueEditText.getText().toString());
	
				EditText priorityEditText = (EditText) findViewById(R.id.priorityEditText);
				Task.Priority priority = Task.Priority.valueOf(priorityEditText.getText().toString());
	
				EditText statusEditText = (EditText) findViewById(R.id.statusEditText);
				Task.Status status = Task.Status.valueOf(statusEditText.getText().toString());
			
				Task task = new Task(author, description, due, priority, status);
				
				//InetAddress serverAddr = InetAddress.getByName(Settings.HOST);
				//Socket socket = new Socket(serverAddr, Settings.PORT);
				Socket socket = new Socket();
				InetSocketAddress socketAddr = new InetSocketAddress(Settings.HOST, Settings.PORT);
				socket.connect(socketAddr, (int)TaskListActivity.connectionTimeout*1000);
				
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader( new InputStreamReader(socket.getInputStream()));
                JSONObject request = new JSONObject();
                JSONObject taskJSON = task.toJSON();
                request.put("request", "add");
                request.put("task", taskJSON);
                success = true;
                
                out.println(request.toString());
                in.close();
                out.close();
                socket.close();
                
                Intent i = new Intent();
				i.putExtra("task", taskJSON.toString());
				setResult(100,i);
                finish();
				//System.out.println(task.toString());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				error = e;
				e.printStackTrace();
			}
			return false;
		}

		@Override
		protected void onPostExecute(Boolean result) {
		    // TODO: UI thread
			System.out.println("postExecute!");
			if (!success)
				Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
		}
		
	}

}
