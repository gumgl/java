package Game;

import java.util.ArrayList;
import Game.KnightsWatch.Color;

public abstract class Piece {
	protected Board board;
	protected Color color;
	protected Position position;
	protected ArrayList<Position> moves;

	public Piece(Board board, Color color) {
		this.board = board;
		this.color = color;
	}

	public Position getPosition() {
		return this.position;
	}
	public Color getColor() {
		return this.color;
	}
	public void setPosition(Position position) {
		this.position = new Position(position);
	}
	public void setColor(Color color) {
		this.color = color;
	}
	// Returns positions that a Piece can go theoretically
	public ArrayList<Position> getMoves() {
		return moves;
	}
	
	protected boolean isMoveValid(Position move) {
		// moves contains what Black can do, inverse for white:
		for (Position validMove : moves)
			if (color == Color.BLACK && validMove.equals(move)
				|| color == Color.WHITE && validMove.equals(Position.multiply(move, -1)))
				return true; // We have found a move that is valid!
		return false;
	}
		
	public abstract boolean canMove(Position move);
	public abstract String getLetter();
	public abstract String getIcon();
}
