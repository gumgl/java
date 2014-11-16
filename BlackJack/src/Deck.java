import java.io.PrintStream;
import java.util.Random;

public class Deck {

	private static final Random GENERATOR = new Random();
	private static final int DECK_SIZE = Card.Rank.values().length * Card.Suit.values().length;

	private Card[] deck;
	private int current;
	
	public Deck() {
		deck = new Card[DECK_SIZE];

		int i = 0;
		for(Card.Rank r : Card.Rank.values()) 
			for(Card.Suit s : Card.Suit.values()) 
				deck[i++] = new Card(r, s);

		current = 0;
	}
	
	public void shuffle() {
		
		for(int i=0; i<deck.length; i++) {
			int r = GENERATOR.nextInt(deck.length);
			swap(r, i);
		}
		current = 0;
	}
	
	public Card draw() {
		if (current < deck.length)
			return deck[current++];
		else
			return null;
	}
	
	public boolean hasCards() {
		return current < deck.length;
	}
	
	/**
	 * Private methods to move a card to a specific position
	 */
	private int find(Card.Rank r, Card.Suit s) {
		for(int i=0; i<deck.length; i++)
			if(deck[i].getSuit() == s && deck[i].getRank() == r)
				return i;
		return -1;
	}
	
	private void swap(int i, int j) {
		Card tmp = deck[i];
		deck[i] = deck[j];
		deck[j] = tmp;
	}
	private void move(Card.Rank r, Card.Suit s, int to) {
		int from = find(r, s);
		if(from > 0)
			swap(from, to);
	}
	
	/** 
	 * Test decks for testing specific scenarios
	 */
	public static Deck testBJ()  {
		Deck deck = new Deck();
		deck.shuffle();
		deck.move(Card.Rank.ACE, Card.Suit.HEARTS, 0);
		deck.move(Card.Rank.JACK, Card.Suit.DIAMONDS, 2);
		return deck;
	}
	
	public static Deck testBJTie()  {
		Deck deck = new Deck();
		deck.shuffle();
		deck.move(Card.Rank.ACE, Card.Suit.HEARTS, 0);
		deck.move(Card.Rank.ACE, Card.Suit.DIAMONDS, 1);
		deck.move(Card.Rank.JACK, Card.Suit.DIAMONDS, 2);
		deck.move(Card.Rank.QUEEN, Card.Suit.HEARTS, 3);
		return deck;
	}
	
	public static Deck testAceLow()  {
		Deck deck = new Deck();
		deck.shuffle();
		deck.move(Card.Rank.ACE, Card.Suit.HEARTS, 0);
		deck.move(Card.Rank.EIGHT, Card.Suit.DIAMONDS, 2);
		deck.move(Card.Rank.EIGHT, Card.Suit.SPADES, 4);		
		return deck;
	}

	public static Deck testSplit()  {
		Deck deck = new Deck();
		deck.shuffle();
		deck.move(Card.Rank.EIGHT, Card.Suit.HEARTS, 0);
		deck.move(Card.Rank.EIGHT, Card.Suit.DIAMONDS, 2);
		return deck;
	}
	public static Deck testSplitAces()  {
		Deck deck = new Deck();
		deck.shuffle();
		deck.move(Card.Rank.ACE, Card.Suit.HEARTS, 0);
		deck.move(Card.Rank.ACE, Card.Suit.DIAMONDS, 2);
		return deck;
	}
	
	public static Deck testSplitSplitSplit()  {
		Deck deck = new Deck();
		deck.shuffle();
		deck.move(Card.Rank.EIGHT, Card.Suit.HEARTS, 0);
		deck.move(Card.Rank.EIGHT, Card.Suit.DIAMONDS, 2);
		deck.move(Card.Rank.EIGHT, Card.Suit.CLUBS, 4);
		deck.move(Card.Rank.EIGHT, Card.Suit.SPADES, 5);
		return deck;
	}
	
	public static Deck testSplitBJ()  {
		Deck deck = new Deck();
		deck.shuffle();
		deck.move(Card.Rank.JACK, Card.Suit.HEARTS, 0);
		deck.move(Card.Rank.JACK, Card.Suit.DIAMONDS, 2);
		deck.move(Card.Rank.ACE, Card.Suit.CLUBS, 4);
		return deck;
	}
	
}
