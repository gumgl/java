package ca.qc.johnabbott.ian.tasks;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.Comparator;
import java.util.Date;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class TaskListActivity extends Activity {
	
	private ListView taskListView;
	private Timer timer;
	private TaskList taskList;
	private Context context;
	public static final double refreshInterval = 10; // in seconds
	public static final double connectionTimeout = 2; // in seconds 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tasks);
		taskListView = (ListView) findViewById(R.id.taskListView);
		context = getApplicationContext();
		taskList = new TaskList();
       
		startTimer(true);
	}

	public void startTimer(boolean startNow) {
		timer = new Timer();
		timer.schedule(new FetchTimer(), startNow ? 0 : (int)refreshInterval*1000, (int)refreshInterval*1000);
	}
	public void stopTimer() {
		timer.cancel();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_tasks, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case R.id.menu_add:
			Intent intent = new Intent(this, TaskCreateActivity.class);
			stopTimer();
			startActivityForResult(intent, 100);
		    // TODO
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    // TODO: when returning from Intent
		if(resultCode == 100){
			startTimer(false);
            // Storing result in a variable called myvar
            // get("website") 'website' is the key value result data
            try {
				Task task = Task.fromJSON(new JSONObject((String)data.getExtras().get("task")));
				taskList.add(task);
				TaskListAdapter adapter = new TaskListAdapter(taskList, context);
				taskListView.setAdapter(adapter);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
       }
	}
	
	class FetchTimer extends TimerTask {
        public void run() {
        	System.out.println("TimerTask FetchTimer run!");
        	new FetchTaskList().execute(); // fetching done in background.
            //timer.cancel(); //Terminate the timer thread
        }
    }

	private class FetchTaskList extends AsyncTask<Void, Void, JSONArray> {

		@Override
		protected JSONArray doInBackground(Void... params) {
			Socket socket = new Socket();
			Scanner in = null;
			PrintStream out = null;
			try {
				try {
		            System.out.println("connecting to server...");
					//InetAddress serverAddr = InetAddress.getByName(Settings.HOST);
					//socket = new Socket(serverAddr, Settings.PORT);
		            InetSocketAddress socketAddr = new InetSocketAddress(Settings.HOST, Settings.PORT);
					socket.connect(socketAddr, (int)connectionTimeout*1000);
	
					in = new Scanner(socket.getInputStream());
					out = new PrintStream(socket.getOutputStream());
		            JSONObject request = new JSONObject();
		            request.put("request", "fetch");
		            out.println(request.toString());
		            
		            System.out.println("trying to get the list");
		            if (in.hasNext()) {
		            	//String serverInput = in.readLine();
		            	String serverInput = in.nextLine();
		            	JSONArray list = new JSONArray(serverInput);
		            	System.out.println("got the list!");
		            	return list;
		            } else {
		            	throw new IOException();
		            }
				} finally {
					System.out.println("closing all connections!");
					if (in != null)
						in.close();
					if (out != null)
						out.close();
					if (socket!= null)
						socket.close();
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(JSONArray list) {
			try {
				taskList = new TaskList();
				for (int i = 0; i < list.length(); i++)
					taskList.add(Task.fromJSON(list.getJSONObject(i)));
				
				TaskListAdapter adapter = new TaskListAdapter(taskList, getApplicationContext());
				
				taskListView.setAdapter(adapter);
			    // TODO: executes on UI thread
			} catch (Exception e) {
				Toast.makeText(context, "Cannot communicate with server", Toast.LENGTH_LONG).show();
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	
	}

	
	
	// stack overflow question 411592
	public static void copyStream(InputStream input, OutputStream output) throws IOException {
		byte[] buffer = new byte[1024];
		int length;
		while((length = input.read(buffer, 0, buffer.length)) > 0) 
			output.write(buffer, 0, length);	
	}
	
	public static String convertStreamToString(InputStream is) {
	    Scanner s = new Scanner(is).useDelimiter("\\A");
	    return s.hasNext() ? s.next() : "";
	}
	
}
