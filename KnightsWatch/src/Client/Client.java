package Client;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;

import Game.KnightsWatch;
import Game.Piece;
import Game.Position;
import Game.KnightsWatch.Color;
import Server.Server;


public class Client {

	final private static String DEFAULT_HOST = "127.0.0.1";
	final private static int DEFAULT_PORT = 9999;
	private static KnightsWatch game;
	private static Scanner input;
	private static Color color;
	private static Socket socket;
	private static Scanner in;
	private static PrintStream out;
	private static String opponent = "";
	private static String username = "";
	private static boolean watcher = false;

	public static void main(String[] args) {
		String host = args.length > 0 ? args[0] : DEFAULT_HOST;
		int port = args.length > 1 ? Integer.parseInt(args[1]) :  DEFAULT_PORT;
		
		input = new Scanner(System.in);
		game = new KnightsWatch();
		
		System.out.print("Choose a username or \""+Server.WATCH_USERNAME+"\": ");
		username = input.nextLine();
		
		if (username.equals(Server.WATCH_USERNAME))
			watcher = true;
		
		try {
			try {
				socket = new Socket(host, port);
				in = new Scanner(socket.getInputStream());
				out = new PrintStream(socket.getOutputStream());
				
				out.println(username);
				
				boolean gameOver = false;
				
				while(!gameOver) { // Game loop
					if (in.hasNext()) { // Wait for server input
						String message = in.nextLine();
						String type = parseType(message);
						String info = parseInfo(message);
						//System.out.println(message);
						//System.out.println(type+":"+info);
						switch (type) {
						case "status":
							switch (info) {
							case "connecting":
								System.out.println("Waiting for an opponent...");
								break;
							case "watch_wait":
								System.out.println("Waiting for a game to start...");
								break;
							case "watch_start":
								System.out.println("You are watching "+username+" & " + opponent);
								game.getBoard().Print(color);
								break;
							case "playing":
								game.getBoard().Print(KnightsWatch.inverse(color));
								System.out.println("Waiting for "+opponent+"'s move...");
								break;
							case "start":
								System.out.println("Game started against "+opponent);
								break;
							case "win":
								if (watcher)
									System.out.println("Game Over! "+username+" won.");
								else
									System.out.println("Game Over! You won.");
								gameOver = true;
								break;
							case "loss":
								if (watcher)
									System.out.println("Game Over! "+opponent+" won.");
								else
									System.out.println("Game Over! You lost.");
								gameOver = true;
								break;
							case "draw":
								System.out.println("Game Over! Draw.");
								gameOver = true;
								break;
							case "disconnected":
								System.out.println("Game Over! "+opponent+" disconnected");
								gameOver = true;
								break;
							case "disconnected_white":
								System.out.println("Game Over! "+username+" disconnected");
								gameOver = true;
								break;
							case "disconnected_black":
								System.out.println("Game Over! "+opponent+" disconnected");
								gameOver = true;
								break;
							case "move":
								game.getBoard().Print(color);
								while(true) {
									System.out.print("Enter move or \"pass\": ");
									String choice = input.nextLine().toLowerCase();
									
									if (choice.equals("pass")) {
										out.println(choice);
										break;
									}
									else if (choice.length() >= 5) {
										// Parse user input
										ArrayList<Object> parsed = game.parseMove(choice);
										Piece piece = (Piece)parsed.get(0);
										Position move = (Position)parsed.get(1);
										
										if (piece == null)
											System.out.println("Error: Piece not found at "+choice.charAt(1)+choice.charAt(2));
										else if (piece.getColor() != color)
											System.out.println("Error: You cannot move a "+piece.getColor().toString()+" piece");
										else if (piece.canMove(move)) {
											game.getBoard().movePiece(piece, move);
											out.println(choice);
											break;
										} else
											System.out.println("Error: Cannot move "+piece.getClass().getSimpleName()+" to "+choice.charAt(3)+choice.charAt(4));
									} else
										System.out.println("Error: Invalid notation");
								}
								break;
							}
							break;
						case "username":
							if (watcher && username.equals(Server.WATCH_USERNAME))
								username = info; // Set white's name
							else
								opponent = info; // Set black's name
							//System.out.println("Your opponent is: "+opponent);
							break;
						case "message":
							System.out.println("Server: "+info);
							break;
						case "color":
							for (KnightsWatch.Color colors : KnightsWatch.Color.values())
								if (colors.toString().equals(info))
									color = colors;
							System.out.println("Your color is: "+color.name());
							break;
						case "moved":
							String user = opponent;
							if (watcher)
								if (color == Color.WHITE) { // If white is playing, replace with white's username
									user = username;
									color = Color.BLACK;
								} else {
									user = opponent;
									color = Color.WHITE;
								}
							if (info.equals("pass"))
								System.out.println(user+" passed his turn");
							else if (info.length() >= 5) {
								ArrayList<Object> parsed = game.parseMove(info);
								Piece piece = (Piece)parsed.get(0);
								Position move = (Position)parsed.get(1);
								game.getBoard().movePiece(piece, move);
								System.out.println(user+" played "+info);
							}
							if (watcher)
								game.getBoard().Print(color);
							break;
						case "watchers":
							System.out.println("You are now being watched by "+info+" people");
							break;
						}
					} else {
						System.out.println("You have been disconnected");
					}
				}
			} finally {
				if(in != null)
					in.close();
				if(out != null)
					out.close();
				if(socket != null)
					socket.close();
			}
		} catch (UnknownHostException e) { // thrown by socket constructor
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static String parseType(String message) { // Everything before the separator
		return message.substring(0, message.indexOf(Server.SEPARATOR));
	}
	
	private static String parseInfo(String message) { // Everything after the separator
		return message.substring(message.indexOf(Server.SEPARATOR)+1);
	}
}
