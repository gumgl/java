import java.util.ArrayList;

public class Hand {
	
	//public enum Action {HIT, STAND, SPLIT, DOUBLE, SURRENDER, INSURANCE, NOTHING;}
	
	private ArrayList<Card> cards;
	private Deck deck;
	private Player player;
	private int bet;
	private int score;
	private boolean doubled;
	private boolean stand;
	private boolean surrendered;
	private int splitID;
	
	public Hand(Deck deck, Player player, int bet) {
		this.deck = deck;
		this.player = player;
		this.bet = bet;
		this.cards = new ArrayList<Card>();
		this.score = 0;
		this.doubled = false;
		this.stand = false;
		this.surrendered = false;
		this.splitID = 0;
	}

	public boolean dealCard() { // Add a card from deck
		cards.add(deck.draw());
		return true;
	}
	public boolean dealCard(Card card) { // A a provided card
		cards.add(card);
		return true;
	}
	public ArrayList<Action> availableActions() {
		ArrayList<Action> actions = new ArrayList<Action>();
		for (Action action : Action.values())
			if (canDo(action))
				actions.add(action);
		return actions;
	}
	public boolean performHit() {
		if (canDo(Action.HIT)) {
			dealCard();
			return true;
		} else
			return false;
	}
	public boolean performStand() {
		if (canDo(Action.STAND)) {
			this.stand = true;
			return true;
		} else
			return false;
	}
	public boolean performDouble(float amt) {
		if (canDo(Action.DOUBLE)) {
			doubled = true;
			bet += amt;
			dealCard();
			return true;
		} else
			return false;
	}
	public Hand performSplit() {
		if (canDo(Action.SPLIT)) {
			Hand hand = new Hand(deck, this.player, this.bet); // New hand (2nd hand)
			
			hand.dealCard(cards.remove(1)); // Move card to new hand
			hand.dealCard(); // Add new card to new hand
			this.dealCard(); // Add new card to this hand
			
			this.splitID = 1;
			hand.splitID = 2;
			if (this.cards.get(0).getRank() == Card.Rank.ACE) {
				this.stand = true;
				hand.stand = true;
			}
			
			return hand;
		} else
			return null;
	}
	public boolean performSurrender() {
		surrendered = true;
		return true;
	}
	public String getDescription() {
		if (splitID > 0)
			return "split hand " + splitID;
		else
			return "";
	}
	public int getBet() {
		return bet;
	}
	public int getScore() {
		updateScore();
		return score;
	}
	public boolean IsOver() {
		if (doubled || stand || surrendered)
			return true;
		updateScore();
		if (score > 21)
			return true;
		return false;
	}
	public boolean getSurrendered() {
		return surrendered;
	}
	public boolean IsBlackJack() {
		return (this.getScore() == 21 && cards.size() == 2 && splitID == 0);
	}
	public Player getPlayer() {
		return player;
	}
	public String getScoreString() {
		updateScore();
		if (IsBlackJack())
			return "BlackJack";
		return Integer.toString(this.score);
	}
	public ArrayList<Card> getCards() {
		return cards;
	}
	
	private void updateScore() {
		score = 0;
		int aces = 0;
		for(Card card : cards) {
			score += card.getScore();
			if (card.getRank() == Card.Rank.ACE)
				aces ++;
		}
		for (int i = 0; i < aces && score > 21; i ++) // For every aces, make it value 1 until score <= 21
			score -= 10;
		
		//System.out.println("@@@Score calculated of: "+score);
	}
	private boolean canDo(Action a) {
		if (a == Action.STAND)
			return true;
		if (a == Action.HIT) {
			updateScore();
			return (score < 21);
		}
		if ((a == Action.SURRENDER || a == Action.DOUBLE) && cards.size() == 2)
			return true;
		
		if (a == Action.SPLIT
				&& player.getMoney() >= bet * 2 // player has money to bet double
				&& cards.size() == 2 && splitID == 0 // unplayed hand
				&& cards.get(0).getRank() == cards.get(1).getRank())
			return true;
		
		return false;
	}

}
