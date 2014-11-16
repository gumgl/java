import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class IO
{
	private final static boolean UNICODE = true;

	/* Unicode box drawing character ;) */
	private final static char DR = '\u250c';
	private final static char LH = '\u2500';
	private final static char LV = '\u2502';
	private final static char DL = '\u2510';
	private final static char UR = '\u2514';
	private final static char UL = '\u2518';

	private static PrintStream output;
	private static Scanner input;

	static {
		output = System.out;
		//output = new PrintStream(System.out, true, "UTF-8");
		input = new Scanner(System.in);
	}

	/*
	 * Make IO un-instantiable by "hiding" it's constructor.
	 */
	private IO() {
	}

	/*
	 * printHand(Hand)
	 *  prints to console:
	 *    - player name and hand attributes (description, score and bet)
	 *    - the hand's cards with ASCII fun
	 */
	public static void printHand(Hand hand) {

		// print hand header
		ArrayList<String> attrs = new ArrayList<String>(3);
		if(!hand.getDescription().equals(""))
			attrs.add(hand.getDescription());
		attrs.add("score: " + hand.getScoreString());
		if(hand.getBet() > 0.0f)
			attrs.add("bet: $" + hand.getBet());

		output.println(hand.getPlayer().getName() + "\'s hand (" + join(attrs, ", ") + ")");

		if(UNICODE) {

			// create a "buffer" to contain the ASCII hand
			StringBuilder[] buffer;
			buffer = new StringBuilder[5]; // card height is 5
			for(int i=0; i<5; i++)
				buffer[i] = new StringBuilder();
			try {
				for(Card c : hand.getCards())
					printCardToBuffer(buffer, c);
			} catch (Exception e) {
				e.printStackTrace();
			}

			// hand buffer populated, print to output stream
			for(StringBuilder sb : buffer)
				output.println(sb.toString());
		}
		else {
			for(Card c : hand.getCards()) {
				output.print(rank(c) + suit(c) + " ");
			}
			output.println();
		}
	}

	/*
	 * printHandHidden(Hand)
	 *  prints to console:
	 *    - player name and hand attributes (description and bet, no score!)
	 *    - the hand's cards with ASCII fun
	 *    - prints only the face of the first card, all others are turned over.
	 *    - used for dealer's hand
	 */
	public static void printHandHidden(Hand hand) {

		// print hand header
		ArrayList<String> attrs = new ArrayList<String>(3);
		if(!hand.getDescription().equals(""))
			attrs.add(hand.getDescription());
		if(hand.getBet() > 0.0f)
			attrs.add("bet: $" + hand.getBet());

		output.println(hand.getPlayer().getName() + "\'s hand (" + join(attrs, ", ") + ")");


		if(UNICODE) {
			// create a "buffer" to contain the ASCII hand
			List<Card> cards = hand.getCards();
			StringBuilder[] buffer;
			buffer = new StringBuilder[5]; // card height is 5
			for(int i=0; i<5; i++)
				buffer[i] = new StringBuilder();
			try {
				printCardToBuffer(buffer, cards.get(0));
				for(int i=1; i<cards.size(); i++)
					printHiddenCardToBuffer(buffer);
			} catch (Exception e) {
				e.printStackTrace();
			}

			// hand buffer populated, print to output stream
			for(StringBuilder sb : buffer)
				output.println(sb.toString());
		}
		else {
			boolean first=true;
			for(Card c : hand.getCards()) {
				if(first) {
					output.print(rank(c) + suit(c) + " ");
					first = false;
				}
				else{
					output.print("## ");	
				}
			}
			output.println();
		}
	}

	/*
	 * getAction(List<Action>)
	 * 
	 *   - prompt the user for an action.
	 *   - warning: doesn't check that input is in available...
	 */
	public static Action getAction(List<Action> available) {

		while(true) {
			output.print("Available actions: ");
			if(available == null) {
				for(int i=0; i<Action.values().length; i++)
					output.print(Action.values()[i].name().toLowerCase() + " ");
			}
			else {
				for(Action action : available)
					output.print(action.name().toLowerCase() + " ");
			}

			output.print("\n> ");
			Action action;

			// java 7
			while (true) {
				String choice = input.nextLine().toLowerCase();
				switch(choice) 
				{
				case "hit":
					action = Action.HIT;
					break;
				case "stand":
					action = Action.STAND;
					break;
				case "split":
					action = Action.SPLIT;
					break;
				case "double":
					action = Action.DOUBLE;
					break;
				case "surrender":
					action = Action.SURRENDER;
					break;
				case "insurance":
					action = Action.INSURANCE;
					break;
				case "abort":
					action = Action.NOTHING;
					break;
				default:
					action = null;
				}
				//output.println("\""+action.toString()+"\"");
				if (action != null && available.contains(action))
					return action;
				else
					output.println("Unsupported action");
					
			}

			/*
			// java 6
			if(action.equals("hit"))
				return Action.HIT;
			if(action.equals("stand"))
				return Action.STAND;
			if(action.equals("double"))
				return Action.DOUBLE;
			if(action.equals("surrender"))
				return Action.SURRENDER;
			if(action.equals("split"))
				return Action.SPLIT;
			output.println("Unsupported action");
			 */
		}
	}

	/*
	 * getBet(int)
	 * 
	 *   - prompt the user for a money amount <= max
	 */
	public static int getBet(int max) {
		int bet;
		output.println("Bet must be between $1 and $" + max + ".");
		output.print("bet > ");
		bet = input.nextInt();
		while (bet <= 0 || bet > max) {
			output.println("Bet must be between $1 and $" + max + ".");
			output.print("bet > ");
			bet = input.nextInt();
		}  
		input.nextLine();
		return bet;
	}

	/*
	 * getName()
	 * 
	 *   - prompt the user for a name, rejects only blank lines
	 */
	public static String getName() {
		String name ;
		do {
			output.print("name > ");
			name = input.nextLine();
		} while (name.equals(""));
		return name;
	}

	/*
	 * warn(String)
	 */
	public static void warn(String message) {
		output.println(message);
	}

	/*
	 * hitEnter()
	 */
	public static void hitEnter() {
		output.print("...hit enter to continue.");
		input.nextLine();
	}

	/* Private helper methods */

	/*
	 * printCardToBuffer()
	 *   - appends the provided card to the array of StringBuilders
	 */
	private static void printCardToBuffer(StringBuilder[] buffer, Card card) throws Exception
	{
		if(buffer.length != 5)
			throw new Exception("Incorrect buffer size.");

		// 10 is a special case because it takes 2 chars
		buffer[0].append(""+DR + LH + LH + LH + LH + LH + DL);
		buffer[1].append(
				card.getRank() == Card.Rank.TEN ? 
						LV + rank(card) + "   " + LV :
							LV + rank(card) + "    " + LV	
				);
		buffer[2].append(""+LV+ "  " + suit(card) +"  " + LV+"");
		buffer[3].append(
				card.getRank() == Card.Rank.TEN ? 
						LV + "   " + rank(card) + LV :
							LV + "    " + rank(card) + LV	
				);	
		buffer[4].append(""+UR + LH + LH + LH + LH + LH + UL);
	}

	/*
	 * printCardToBuffer()
	 *   - appends an overturned card to the buffer
	 */
	private static void printHiddenCardToBuffer(StringBuilder[] buffer) throws Exception
	{
		if(buffer.length != 5)
			throw new Exception("Incorrect buffer size.");

		buffer[0].append(""+ DR + LH + LH + LH + LH + LH + DL);
		buffer[1].append(LV + "#####" + LV);
		buffer[2].append(LV + "#####" + LV);
		buffer[3].append(LV + "#####" + LV);
		buffer[4].append(""+ UR + LH + LH + LH + LH + LH + UL);
	}


	/*
	 * rank(Card)
	 *  - returns the rank of the card
	 */
	private static String rank(Card card) {
		switch(card.getRank()) {
		case ACE: return "A";
		case TWO: return "2";
		case THREE: return "3";
		case FOUR: return "4";
		case FIVE: return "5";
		case SIX: return "6";
		case SEVEN: return "7";
		case EIGHT: return "8";
		case NINE: return "9";
		case TEN: return "10";
		case JACK: return "J";
		case QUEEN: return "Q";
		case KING: return "K";
		default:
			return null;
		}
	}

	/*
	 * suit(Card)
	 *   - returns the suit in either ASCII or UTF-8, depending on the UTF constant
	 */
	private static String suit(Card card) {
		switch(card.getSuit()) {
		case HEARTS: 
			return !UNICODE ? "H" : "\u2661";
		case DIAMONDS: 
			return !UNICODE ? "D" : "\u2662";
		case SPADES: 
			return !UNICODE ? "S" : "\u2660";
		case CLUBS: 
			return !UNICODE ? "C" : "\u2663";
		default:
			return null;
		}
	}

	/*
	 * join(ArrayList<String>, String)
	 * 
	 *   - "joins" the elements of the array with the glue string
	 *   - returns a single string.
	 *   - missing(?) from the java libraries.
	 */
	private static String join(ArrayList<String> s, String glue) {
		if(s.size() == 0) 
			return "";

		StringBuilder sb = new StringBuilder();
		int 	i;
		for(i=0;i<s.size()-1;i++)
			sb.append(s.get(i) + glue);
		return sb.toString() + s.get(i);
	}

}