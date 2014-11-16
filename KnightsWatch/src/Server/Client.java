package Server;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

import Game.KnightsWatch.Color;

public class Client {
	protected Socket socket = null;
	protected Scanner in = null;
	protected PrintStream out = null;
	protected Color color;
	protected String name;
	protected boolean connected;
	
	public Client(Socket socket) throws IOException {
		this.color = color;
		
		// make a scanner & printstream out of the socket's I/O streams
		this.in = new Scanner(socket.getInputStream());
		this.out = new PrintStream(socket.getOutputStream());
		this.connected = true;
	}
	
	public void close() throws IOException {
		if(in != null)
			in.close();
		if(out != null)
			out.close();
		if(socket != null)
			socket.close();
	}
	public void sendMessage(String message) {
		out.println("message"+Server.SEPARATOR+message);
	}
	public void sendStatus(String status) {
		out.println("status"+Server.SEPARATOR+status);
	}
	public void sendUsername(String username) {
		out.println("username"+Server.SEPARATOR+username);
	}
	public void sendMove(String move) {
		out.println("moved"+Server.SEPARATOR+move);
	}
	public void sendColor() {
		out.println("color"+Server.SEPARATOR+color.toString());
	}
	public void sendWatchers(int watchers) {
		out.println("watchers"+Server.SEPARATOR+watchers);
	}
	public void askName() {
		connected = in.hasNext(); // Get next client input
		if (connected)
			this.name = in.nextLine();
	}
	public String askMove() {
		sendStatus("move");
		connected = in.hasNext(); // Get next client input
		if (connected)
			return in.nextLine();
		else
			return "";
			
	}
	public boolean isConnected() {
		return connected;
	}
	public String getName() {
		return name;
	}
	public Color getColor() {
		return color;
	}
	public Scanner getScanner() {
		return in;
	}
	public PrintStream getOutputStream() {
		return out;
	}
	public void setColor(Color color) {
		this.color = color;
	}

}
