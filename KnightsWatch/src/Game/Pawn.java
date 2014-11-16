package Game;

import java.util.ArrayList;

import Game.KnightsWatch.Color;

public class Pawn extends Piece {

	public Pawn(Board board, Color color) {
		super(board, color);
		moves = new ArrayList<Position>();
		moves.add(new Position(-1, 1));
		moves.add(new Position(0, 1));
		moves.add(new Position(1, 1));
	}

	static String whitePawn   = "\u2659 ";
	static String whiteKnight = "\u2658 ";
	static String blackPawn   = "\u265F ";
	static String blackKnight = "\u265E ";

	@Override
	public boolean canMove(Position move) {
		Position to = Position.add(position, move);
		
		if (! to.inBounds(0, 0, board.SIZE - 1, board.SIZE - 1))
			return false; // Can't go outside the board
		
		if (! isMoveValid(move))
			return false; // Validate that move is indeed valid for this type of piece
		
		if (move.getX() == 0) // Going straight up or down (moving)
			return (board.getPiece(to) == null); // Go if there's already no piece
		else // Going sideways (attacking)
			return (board.getPiece(to) != null && board.getPiece(to).getColor() != this.getColor()); // Go if there's opposite color
	}
	@Override
	public String getLetter() {
		return "P";
	}
	@Override
	public String getIcon() {
		switch (color) {
		case WHITE:
			return "\u2659 ";
		case BLACK:
			return "\u265F ";
		default:
			return "X ";
		}
	}
}
