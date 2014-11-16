import java.util.ArrayList;


public class Table {

	private static Deck deck;
	private static Player dealer;
	private static Player player;
	private static Hand pHand1;
	private static Hand pHand2;
	private static Hand dHand1;
	private static int bet;
	
	public static void main(String[] args) {
		System.out.println("$$$ $$$ $$$ $$$ $$$ $$$ $$$");
		System.out.println("$$$ Guillaume's Casino  $$$");
		System.out.println("   Blackjack pays 3 to 2   ");
		
		dealer = new Player(0, "Dealer");
		player = new Player(200, IO.getName());
		
		while (player.getMoney() > 0) {
			deck = new Deck();
			deck.shuffle();
			//deck = Deck.testBJ();
			//deck = Deck.testBJTie();
			//deck = Deck.testAceLow();
			//deck = Deck.testSplit();
			//deck = Deck.testSplitAces();
			//deck = Deck.testSplitSplitSplit();
			//deck = Deck.testSplitBJ();
			
			bet = IO.getBet((int)player.getMoney());
	
			pHand1 = new Hand(deck, player, bet);
			pHand2 = null;
			dHand1 = new Hand(deck, dealer, 0);
			pHand1.dealCard();
			dHand1.dealCard();
			pHand1.dealCard();
			dHand1.dealCard();
			
			PlayPlayerHand(pHand1, dHand1);
			
			if (pHand2 != null)
				PlayPlayerHand(pHand2, dHand1);
			
			System.out.println("==================================================");
			System.out.println("Now playing dealer\'s hand");
			
			PlayDealerHand(dHand1);
			System.out.println("==================================================");
			System.out.println("Results:");
			// FOR EACH SPLIT HAND, RESULTS
			PrintResults(pHand1, dHand1);
			if (pHand2 != null)
				PrintResults(pHand2, dHand1);
			System.out.println("$$$ $$$ $$$ $$$ $$$ $$$ $$$");
		}
		System.out.println("==================================================");
		System.out.println("Game Over.");
		System.out.println("You have no more money. Goodbye.");
	}
	
	public static void PlayPlayerHand(Hand pHand, Hand dHand) {
		System.out.println("==================================================");
		System.out.println("Now playing hand: " + pHand.getPlayer().getName());
		System.out.println("--------------------------------------------------");
		
		while (!pHand.IsOver()) { // Actions
			IO.printHand(pHand);
			IO.printHandHidden(dHand);
			Action action = IO.getAction(pHand.availableActions());
			switch (action) {
			case HIT:
				pHand.performHit();
				break;
			case STAND:
				pHand.performStand();
				break;
			case SURRENDER:
				pHand.performSurrender();
				break;
			case DOUBLE:
				float amt = IO.getBet((int)pHand.getBet());
				pHand.performDouble(amt);
				break;
			case SPLIT:
				pHand2 = pHand.performSplit();
				break;
			}
		}
		PrintFinalHand(pHand);
	}
	public static void PlayDealerHand(Hand dHand) {
		while (dHand.getScore() < 17)
			dHand.performHit();
		dHand.performStand();
		PrintFinalHand(dHand);
	}
	
	public static void PrintFinalHand(Hand hand) {
		System.out.println("--------------------------------------------------");
		System.out.print("Final hand: ");
		IO.printHand(hand);
	}
	
	public static void PrintResults(Hand pHand, Hand dHand) {
		int pScore = pHand.getScore();
		int dScore = dHand.getScore();
		Player player = pHand.getPlayer();
		float payout; // portion of bet (0: draw, > 1: win, < 1: loss)

		if (pHand.getSurrendered())
			payout = -0.5f;
		else if (pScore > 21 && dScore > 21) // Both lose, so draw
			payout = 0f;
		else if (pScore > 21 && dScore <= 21)
			payout = -1f;
		else if (pScore <= 21 && dScore > 21)
			payout = 1f;
		else if (pScore == dScore) // same scores under 22
			if (pHand.IsBlackJack() && !dHand.IsBlackJack())
				payout = 1.5f; // player wins
			else if (!pHand.IsBlackJack() && dHand.IsBlackJack())
				payout = -1f; // player loses
			else // both blackjack or both not blackjack
				payout = 0f; // draw
		else if (pScore > dScore)
			payout = 1f;
		else
			payout = -1f;
		player.setMoney(player.getMoney() + pHand.getBet() * payout);
		System.out.println("payout: " + payout);
		System.out.println("pBet: " + pHand.getBet());
		System.out.println("pMoney: " + player.getMoney());
		
		if (payout > 0)
			System.out.println(player.getName() + ": wins $" + pHand.getBet() * payout);
		else if (payout < 0)
			System.out.println(player.getName() + ": loses $" + pHand.getBet() * payout * -1);
		else
			System.out.println(player.getName() + ": draw");
	}
}
