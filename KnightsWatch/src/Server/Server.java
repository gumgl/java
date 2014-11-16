package Server;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;


public class Server {
	public static final boolean UNICODE = false;
	public static final char SEPARATOR = ':';
	private final static int DEFAULT_PORT = 9999;
	public final static String WATCH_USERNAME = "watch";
	//private final static int WATCHER_MOVES = 3; // Let watchers join games with n or fewer moves
	private static int gameUID = 0;
	private static ServerSocket server;
	private static ArrayList<GameThread> threads;
	private static ArrayList<Client> outstandingWatchers;

	public static void main(String[] argv) {
		int port = argv.length > 0 ? Integer.parseInt(argv[0]) :  DEFAULT_PORT;
		threads = new ArrayList<GameThread>();
		outstandingWatchers = new ArrayList<Client>();
		
		// to listen to incoming connections
		
		try {
			server = new ServerSocket(port);
		} catch (IOException e) {
			System.err.println("Could not listen on port: " + port);
			System.exit(-1);
		}
		System.out.println("Listening on port: " + port);

		while(true) { // Looking for new clients, start a game
			try {
				// Wait for a client connection, socket is obtained when a client connects
				Socket socket = server.accept(); // One client found
				Client client = new Client(socket);
				client.askName();
				if (client.getName().equals(WATCH_USERNAME)) {
					int n;
					for (n=threads.size()-1; n>=0; n--) {
						GameThread game = threads.get(n);
						if (game.isFull() && game.isAlive() && game.getMoves() == 0) {
							game.addWatcher(client);
							break;
						}
					}
					if (n == -1) { // We went through all game threads without finding a good one
						outstandingWatchers.add(client);
						client.sendStatus("watch_wait");
					}
				} else {
					boolean found = false;
					for (GameThread game : threads)
						if (!game.isFull()) {
							game.addPlayer(client);
							found = true;
							if (game.isFull()) // New full game, send all watchers there
								for (Client watcher : outstandingWatchers)
									game.addWatcher(watcher);
							break;
						}
					if (!found) {
						GameThread thread = new GameThread(server, gameUID++); // Start a game
						threads.add(thread);
						thread.addPlayer(client);
					}
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void log(String message) {
		System.out.println(message);
	}
}
