package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import Game.KnightsWatch;
import Game.Piece;
import Game.Position;
import Game.KnightsWatch.Color;

public class GameThread extends Thread {
	private KnightsWatch game;
	private ServerSocket server;
	private Client playerW;
	private Client playerB;
	private ArrayList<Client> watchers;
	private int id;
	private int moves = 0; // number of moves since start of game

	public GameThread(ServerSocket server, int id) {
		this.id = id;
		this.game = new KnightsWatch();
		this.server = server;
		this.watchers = new ArrayList<Client>();
	}

	public void run() {
		try {
			try {
				//log("run() executed");
				playerW.sendUsername(playerB.getName());
				playerB.sendUsername(playerW.getName());
				playerW.sendColor();
				playerB.sendColor();
				playerW.sendStatus("start");
				playerB.sendStatus("start");
				log("Started! "+playerW.getName()+" & "+playerB.getName());
				while (game.getWinner() == null && playerW.isConnected() && playerB.isConnected()) { // Game loop
					playerMove(playerW, playerB);
					playerMove(playerB, playerW);
				}
				if (!playerW.isConnected()) {
					playerB.sendStatus("disconnected");
					sendStatusToWatchers("disconnected_white");
					log("Over! White disconnected.");
				} else if (!playerB.isConnected()) {
					playerW.sendStatus("disconnected");
					sendStatusToWatchers("disconnected_black");
					log("Over! Black disconnected.");
				} else if (game.getWinner() == playerW.getColor()) {
					playerW.sendStatus("win");
					playerB.sendStatus("loss");
					sendStatusToWatchers("win");
					log("Over! White wins.");
				} else if (game.getWinner() == playerB.getColor()){
					playerW.sendStatus("loss");
					playerB.sendStatus("win");
					sendStatusToWatchers("loss");
					log("Over! Black wins.");
				} else {
					playerW.sendStatus("draw");
					playerB.sendStatus("draw");
					sendStatusToWatchers("draw");
					log("Over! Draw.");
				}
				
			} finally {
				playerW.close();
				playerB.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	protected void playerMove(Client player, Client other) {
		if (game.getWinner() == null) {
			other.sendStatus("playing");
			String notation = player.askMove();
			if (notation.equals("pass")) {
				other.sendMove(notation);
				sendMoveToWatchers(notation);
				log(player.getName()+" passed his turn");
				moves ++;
			}
				
			if (notation.length() >= 5) {
				ArrayList<Object> parsed = game.parseMove(notation);
				Piece piece = (Piece)parsed.get(0);
				Position move = (Position)parsed.get(1);
				game.getBoard().movePiece(piece, move);
				other.sendMove(notation);
				sendMoveToWatchers(notation);
				log(player.getName()+" played "+notation);
				moves ++;
			}
		}
	}
	
	protected void sendMoveToWatchers(String move) {
		for (Client watcher : watchers)
			watcher.sendMove(move);
	}
	
	protected void sendStatusToWatchers(String status) {
		for (Client watcher : watchers)
			watcher.sendStatus(status);
	}
	
	protected void log(String message) {
		Server.log("Game #"+id+": "+message);
	}
	
	public int getMoves() {
		return moves;
	}
	
	public int getPlayers() {
		int players = 0;
		if (playerW != null)
			players ++;
		if (playerB != null)
			players ++;
		return players;
	}
	
	public int getWatchers() {
		return watchers.size();
	}
	
	public boolean isFull() {
		return (getPlayers() >= 2);
	}
	
	public void addPlayer(Client player) {
		int players = getPlayers();
		if (players == 0) {
			this.playerW = player;
			this.playerW.setColor(Color.WHITE);
			this.playerW.sendStatus("connecting"); // Waiting for other player to connect
		} else if (players == 1) {
			this.playerB = player;
			this.playerB.setColor(Color.BLACK);
			start(); // Start thread run()
		}
	}
	
	public void addWatcher(Client watcher) {
		log("New watcher");
		watchers.add(watcher);
		watcher.setColor(Color.WHITE); // Game starts with white
		watcher.sendUsername(playerW.getName());
		watcher.sendUsername(playerB.getName());
		watcher.sendStatus("watch_start");
		playerW.sendWatchers(getWatchers());
		playerB.sendWatchers(getWatchers());
	}
}