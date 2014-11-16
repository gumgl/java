
public class Card {

	public enum Rank {
	    ACE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING;	    
	}
	
	public enum Suit {
		HEARTS, DIAMONDS, SPADES, CLUBS;
	}

	private Rank rank;
	private Suit suit;
	
	public Card(Rank face, Suit suit) {
		super();
		this.rank = face;
		this.suit = suit;
	}
	
	public Rank getRank() { return rank; }
	public Suit getSuit() { return suit; }

	@Override
	public String toString() {
		return rank.toString() + suit.toString() ;
	}

	public boolean equals(Rank rank, Suit suit) {
		return this.rank == rank && this.suit == suit;
	}
	
	public int getScore() {
		switch(rank) {
		case ACE: return 11;
		case TWO: return 2;
		case THREE: return 3;
		case FOUR: return 4;
		case FIVE: return 5;
		case SIX: return 6;
		case SEVEN: return 7;
		case EIGHT: return 8;
		case NINE: return 9;
		case TEN: return 10;
		case JACK: return 10;
		case QUEEN: return 10;
		case KING: return 10;
		default:
			return 0;
		}
	}
	

}
