package Game;

import java.util.ArrayList;

import Game.KnightsWatch.Color;

public class Knight extends Piece {

	public Knight(Board board, Color color) {
		super(board, color);
		moves = new ArrayList<Position>();
		moves.add(new Position(-2, -1));
		moves.add(new Position(-1, -2));
		moves.add(new Position(+1, -2));
		moves.add(new Position(+2, -1));
		moves.add(new Position(+2, +1));
		moves.add(new Position(+1, +2));
		moves.add(new Position(-1, +2));
		moves.add(new Position(-2, +1));
	}

	@Override
	public boolean canMove(Position move) {
		Position to = Position.add(position, move);
		
		if (! to.inBounds(0, 0, board.SIZE - 1, board.SIZE - 1))
			return false; // Can't go outside the board
		
		if (! isMoveValid(move))
			return false; // Validate that move is indeed valid for this type of piece
		
		// Go only if empty or opposite color
		return (board.getPiece(to) == null || board.getPiece(to).getColor() != this.getColor());
	}
	
	@Override
	public String getLetter() {
		return "K";
	}
	@Override
	public String getIcon() {
		switch (color) {
		case WHITE:
			return "\u2658 ";
		case BLACK:
			return "\u265E ";
		default:
			return "X ";
		}
	}

}
