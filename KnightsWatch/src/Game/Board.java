package Game;

import Game.KnightsWatch.Color;

//import Game.KnightsWatch.Color;

public class Board {
	public final int SIZE = 6; // Does not work for size >= 10 (input and output 2 chars)
	protected Piece board[][] = new Piece[SIZE][SIZE];
	
	public Board() {
		// Initially all cells are empty (i.e. == null)
	}
	
	public boolean addPiece(Piece piece, Position position) {
		if (position.inBounds(0, 0, SIZE - 1, SIZE - 1)) { // If position is within bounds
			setPiece(piece, position);
			piece.setPosition(position); // So that piece also knows its own position
			return true;
		} else // cannot add piece at position
			return false;
	}
	
	/* Moves piece on the board.              */
	/* Returns if move was successful or not. */
	public void movePiece(Piece piece, Position move) {
		if (piece != null) {
			Position from = piece.getPosition();
			Position to = Position.add(from, move);
			
			if (getPiece(to) != null) // Remove piece from board
				getPiece(to).setPosition(new Position(-1, -1));
			setPiece(piece, to);
			piece.setPosition(to);
			setPiece(null, from); // Old place is now empty
		}
	}
	
	public void Print(Color current) {
		// technically speaking, should be in KnightsWatch.java or KnigthsWatch extension of Board
		String horizontalSeparator = " -";
		for (int col=0; col<SIZE; col++)
			horizontalSeparator += "---";
		
		System.out.println(horizontalSeparator);
		
		for (int row=0; row<SIZE; row++) {
			String line = (SIZE-row)+"|";
			for (int col=0; col<SIZE; col++) {
				if (board[row][col] == null)
					line += "  ";
				else
					line += board[row][col].getIcon();
				line += "|";
			}
			if (row == 0 && current == Color.BLACK)
				line += "\u2b24";
			if (row == SIZE-1 && current == Color.WHITE)
				line += "\u25ef";
			System.out.println(line);
			System.out.println(horizontalSeparator);
		}
		String letters = "";
		for (int col=0; col<SIZE; col++)
			letters += "  " + Character.toString((char)("a".charAt(0) + col));
		System.out.println(letters);
	}
	
	public Piece getPiece(Position position) {
		return getPiece(position.getX(), position.getY());
	}
	public Piece getPiece(int x, int y) {
		int row = y;
		int column = x;
		if (row >= 0 && column >= 0 && row < SIZE && column < SIZE)
			return board[row][column];
		else
			return null;
	}
	protected void setPiece(Piece piece, Position position) {
		setPiece(piece, position.getX(), position.getY());
	}
	protected void setPiece(Piece piece, int x, int y) {
		int row = y;
		int column = x;
		board[row][column] = piece;
	}
}
