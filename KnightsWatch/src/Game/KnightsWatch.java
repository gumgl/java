package Game;

import java.util.ArrayList;

public class KnightsWatch {
	public static final boolean UNICODE = false;
	public enum Color {WHITE, BLACK, BLANK};
	
	private Board board;
	
	public KnightsWatch() {
		board = new Board();
		initBoard(board);
		
	}
	
	public static void initBoard(Board board) {
		for (int x=0; x<board.SIZE; x++) {
			board.addPiece(new Pawn(board, Color.BLACK), new Position(x, 1));
			board.addPiece(new Pawn(board, Color.WHITE), new Position(x, board.SIZE-1-1));
		}
		board.addPiece(new Knight(board, Color.BLACK), new Position(1, 0));
		board.addPiece(new Knight(board, Color.BLACK), new Position(board.SIZE-1-1, 0));
		board.addPiece(new Knight(board, Color.WHITE), new Position(1, board.SIZE-1));
		board.addPiece(new Knight(board, Color.WHITE), new Position(board.SIZE-1-1, board.SIZE-1));
		// Now basic setup of the board is done
	}
	
	// Returns color of winner or null if no winner
	public Color getWinner() {
		// Look for white pawns on black's wall (top)
		for (int x=0; x<board.SIZE; x++) {
			Piece piece = board.getPiece(x, 0);
			if (piece != null && piece instanceof Pawn && piece.getColor() == Color.WHITE)
				return Color.WHITE;
		}
		// Look for black pawns on white's wall (bottom)
		for (int x=0; x<board.SIZE; x++) {
			Piece piece = board.getPiece(x, board.SIZE-1);
			if (piece != null && piece instanceof Pawn && piece.getColor() == Color.BLACK)
				return Color.BLACK;
		}
		int whitePawns = 0;
		int whiteKnights = 0;
		int blackPawns = 0;
		int blackKnights = 0;
		
		for (int x=0; x<board.SIZE; x++) {
			for (int y=0; y<board.SIZE; y++) {
				Piece piece = board.getPiece(x, y);
				if (piece != null)
					switch (piece.getColor()) {
					case WHITE:
						if (piece instanceof Pawn)
							whitePawns ++;
						else
							whiteKnights ++;
						break;
					case BLACK:
						if (piece instanceof Pawn)
							blackPawns ++;
						else
							blackKnights ++;
						break;
					}
			}
		}
		if (whitePawns == 0)
			return Color.BLACK;
		if (blackPawns == 0)
			return Color.WHITE;
		if (blackPawns+blackKnights == 0 || whitePawns+whiteKnights == 0)
			return Color.BLANK;
		
		return null; // No winning pawn found
	}
	
	public ArrayList<Object> parseMove(String notation) {
		ArrayList<Object> results = new ArrayList<Object>();
		Position from = parsePosition(board.SIZE, notation.charAt(1), notation.charAt(2));
		Position to = parsePosition(board.SIZE, notation.charAt(3), notation.charAt(4));
		Piece piece = board.getPiece(from);
		Position move = Position.subtract(to, from);
		
		results.add(piece);
		results.add(move);
		
		return results;
	}
	
	public Board getBoard() {
		return board;
	}
	
	protected static Position parsePosition(int size, char letter, char number) {
		// a-f represents x-axis, 0-5 represents y-axis
		///*DEBUG*/System.out.println("Parsing "+letter+""+number);
		int x = (int)letter - (int)'a';
		int y = size-((int)number - (int)'0');
		///*DEBUG*/System.out.println(new Position(x, y).toString());
		return new Position(x, y);
	}
	
	public static Color inverse(Color color) {
		switch (color) {
		case WHITE:
			return Color.BLACK;
		case BLACK:
			return Color.WHITE;
		default:
			return null;
		}
	}

}
