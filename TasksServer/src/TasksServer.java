import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;


public class TasksServer {

	private final static int PORT = 9999;
	private static TaskList taskList;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int port = args.length > 0 ? Integer.parseInt(args[0]) :  PORT;
		
		// to listen to incoming connections		
		ServerSocket server = null;
		try {
			server = new ServerSocket(port);
		} catch (IOException e) {
			System.err.println("Could not listen on port: " + port);
			System.exit(-1);
		}
		System.out.println("Listening on port: " + port);

		taskList = new TaskList();
		try {
			taskList.add(new Task("ian", "Remember the milk.", Task.dateFormatter.parse("Wed, 03 Jul 2013 21:00:00 -0400"), Task.Priority.LOW, Task.Status.OPEN));
			taskList.add(new Task("ian", "Solve P versus NP problem", Task.dateFormatter.parse("Thu, 04 Jul 2013 12:00:00 -0400"), Task.Priority.HIGH, Task.Status.OPEN));
			taskList.add(new Task("ian", "Walk dog", new Date(), Task.Priority.MEDIUM, Task.Status.CLOSED));
		} catch (ParseException e1) {
		    // should never execute
			e1.printStackTrace();
		}
		
		taskList.sort(new Comparator<Task>() {

			@Override
			public int compare(Task t1, Task t2) {
				return t1.getDue().compareTo(t2.getDue());
			}
			
		});

		/* main server loop: wait for incoming request and start thread */
		while(true) {
			
			try {
				Socket socket  = server.accept();
				System.out.println("Receiving request from " + socket.getInetAddress());
				Request r = new Request(socket);
				r.start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	// process a single request
	static class Request extends Thread {
	
		private synchronized void addTaskToList(Task task) {
			taskList.add(task);
		}
		private synchronized JSONArray taskListToJSON() {
			return taskList.toJSON();
		}

		private Socket socket;
		//BufferedReader in;
		//PrintWriter out;
		Scanner in = null;
		PrintStream out = null;
		
		public Request(Socket socket) {
			this.socket = socket;
		}	
	
		@Override
		public void run() {
			System.out.println("New Request thread started.");
			try {
				//out = new PrintWriter(socket.getOutputStream(), true);
				//in = new BufferedReader( new InputStreamReader(socket.getInputStream()));
				//BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
				in = new Scanner(socket.getInputStream());
				out = new PrintStream(socket.getOutputStream());
				String clientInput;
				
				//while ((clientInput = stdIn.readLine()) != null) {
				while (in.hasNext()) {
					clientInput = in.nextLine();
					System.out.println(socket.getInetAddress()+", "+clientInput);
					JSONObject json = new JSONObject(clientInput);
					String requestType = json.getString("request");
					// TODO: synchronization
					switch (requestType) {
					case "fetch":
						out.println(taskListToJSON().toString());
						break;
					case "add":
						Task task = Task.fromJSON(json.getJSONObject("task"));
						addTaskToList(task);
						break;
					}
				}
			} catch (IOException | JSONException | ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
