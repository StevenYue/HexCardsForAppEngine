package steeng.hexcards.datatype;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

import steeng.hexcards.utility.SixCardsCalculator;
import steeng.hexcards.utility.SixCardsExceptions.RoundDiedException;
import steeng.hexcards.utility.SixCardsExceptions.TypeTwoGameDiedException;
import static steeng.hexcards.utility.SixCardsExceptions.*;
import static steeng.hexcards.datatype.SixCardsConstant.*;

public class Round {
	private Card[] round;
	private boolean isDead = false;
	private HashSet<Card> impossibleTypeOneGameSelection = null;
	
	private Queue<Hand> threeHands = null;
	private ArrayList<Hand[]> botThoughts = null;

	public Round(Card[] r){
		round = new Card[6];
		
		for(int i=0;i<6;i++){
			round[i] = new Card(r[i].getRank(),r[i].getSuit());
		}
		if(isDied(round)){
			isDead = true;
			
		}else{
			impossibleTypeOneGameSelection = new HashSet<Card>();
			for(Card c:round){
				Card temp[] = new Card[5];
				int i = 0;
				for(Card inc: round){
					if(inc != c){
						temp[i] = inc;
						i++;
					}
				}
				if(isDied(temp)){
					impossibleTypeOneGameSelection.add(c);
				}
			}
		}
	}

	public Card[] getRound() {return round;}
	public void setRound(Card[] round) {this.round = round;}
	public boolean isDead(){return this.isDead;}
	public HashSet<Card> getImpossibleTypeOneGameSelection() {return impossibleTypeOneGameSelection;}
	public Queue<Hand> getThreeHands(){ return this.threeHands;}
	public ArrayList<Hand[]> getThoughts(){ return this.botThoughts;}

	private boolean isDied(Card[] cards){
		ArrayList<Card[]> res = new ArrayList<Card[]>();
		Card[] typeTwoGame = new Card[2];
		isDiedAssist(cards,res, typeTwoGame, 0, 0);
		int totalCombinations=0, diedCombinations=0;
		
		for(Card[] cc:res){
			totalCombinations++;
			try {
				new Hand(cc);
			} catch (TypeTwoGameDiedException e) {
				diedCombinations++;
			}
		}
		return totalCombinations == diedCombinations;
	}
	
	private Card[] getTheOtherFour(Card[] cards, Card[] cc) {
		Card[] res = new Card[4];
		int index = 0;
		for(int i=0;i<cards.length;i++){
			if(cc[0] != cards[i] && cc[1] != cards[i]){
				res[index] = new Card(cards[i].getRank(),cards[i].getSuit());
				index++;
			}
		}
		return res;
	}

	private void isDiedAssist(Card[] cards,ArrayList<Card[]> list, Card[] typeTwoGame, int index, int n){
		if(n == 2){
			Card[] t = new Card[2];
			t[0] = typeTwoGame[0]; t[1] = typeTwoGame[1];
			list.add(t);
			return;
		}
		for(int i = index;i<cards.length;i++){
			typeTwoGame[n] = cards[i];
			isDiedAssist(cards, list, typeTwoGame, i+1, n+1);
		}
	}
	 
	
	//Method for bots, apply the mathmatical model here
	public void botThinking(){
		botThoughts = new ArrayList<Hand[]>();
		threeHands = new LinkedList<Hand>();
		ArrayList<Card[]> res = new ArrayList<Card[]>();
		Card[] typeTwoGame = new Card[2];
		isDiedAssist(round,res, typeTwoGame, 0, 0);
		
		for(Card[] cc:res){
				try {
				Card[] theOtherFour = getTheOtherFour(round, cc);
				for(int i=0;i<4;i++){
					Hand[] temp = new Hand[3];
					temp[1] = new Hand(cc);
					temp[0] =new Hand(theOtherFour[i]);
					temp[2] = new Hand(theOtherFour[(i+1)%4],theOtherFour[(i+2)%4],theOtherFour[(i+3)%4]);
					botThoughts.add(temp);
				}
				
				
				
			} catch (TypeTwoGameDiedException e) {}
		}
		
		Collections.sort(botThoughts, new Comparator<Hand[]>() {
			public int compare(Hand[] o1, Hand[] o2) {
				float v1 = evaluate(o1);
				float v2 = evaluate(o2);
				if(v1 < v2) return 1;
				else if(v1 > v2) return -1;
				else return 0;
			}
		});
		
		threeHands.add(botThoughts.get(0)[0]);
		threeHands.add(botThoughts.get(0)[1]);
		threeHands.add(botThoughts.get(0)[2]);
	}
	
	public float debugging(Hand[] hands){
		float res = 0;
		for(Hand h:hands){
			int val[] = h.getValue();
			if(h.getType() == TYPE_ONE_GAME){
				res += ((val[0]-1)*4 + val[1])*100/52;
				System.out.println();
				System.out.println(h.toString());
				System.out.print("Value: ");
				for(Integer i:h.getValue())
					System.out.print(i + "---");
				System.out.println();
				System.out.println("1 score:" + ((val[0]-1)*4 + val[1])*100/52);
			}else if(h.getType() == TYPE_TWO_GAME){
				float beta = 0;
				if((val[0] - 5 )%10 == 0){// there is a face card
					beta = 1;
				}else{
					int A = val[0]/10 - 1;
					int B = (val[0]/10)%2 == 0? val[0]/20: val[0]/20 + 1;
					int n = A-B;
					if(n<=0) beta = 1;
					else beta = (float) (0.9 + 0.1*(val[1]/10 - B)/n);
				}
				
				res += ((val[0]*2/10 - 1) * 4 * beta + val[2]) * 100 * 2/84;
				System.out.println(h.toString());
				System.out.print("Value: ");
				for(Integer i:h.getValue())
					System.out.print(i + "---");
				System.out.println();
				System.out.println("2 score:" + ((val[0]*2/10 - 1) * 4 * beta + val[2]) * 100 * 2/84);
			}else if(h.getType() == TYPE_THREE_GAME){
				int B = 0;
				if(val[1] == FLUSHHAND){
					B = (val[2] - 1) * 14 + val[3];
				}else{
					B = (val[2] - 1) * 4 + val[3];
				}
				
				res += (56*val[0] + 56*val[1] + B) * 100 * 3 /(56*7);
				System.out.println(h.toString());
				System.out.print("Value: ");
				for(Integer i:h.getValue())
					System.out.print(i + "---");
				System.out.println();
				System.out.println("3 score:" + (56*val[0] + 56*val[1] + B) * 100 * 3 /(56*7));
			}
			
		}
		return res;
	}
	
	public float evaluate(Hand[] hands) {
		float res = 0;
		for(Hand h:hands){
			int val[] = h.getValue();
			if(h.getType() == TYPE_ONE_GAME){
				res += ((val[0]-1)*4 + val[1])*100/52;
//				System.out.println(h.toString());
//				for(Integer i:h.getValue())
//					System.out.print(i + "---");
//				System.out.println();
//				System.out.println("1:" + ((val[0]-1)*4 + val[1])*100/52);
			}else if(h.getType() == TYPE_TWO_GAME){
				float beta = 0;
				if((val[0] - 5 )%10 == 0){// there is a face card
					beta = 1;
				}else{
					int A = val[0]/10 - 1;
					int B = (val[0]/10)%2 == 0? val[0]/20: val[0]/20 + 1;
					int n = A-B;
					if(n<=0) beta = 1;
					else beta = (float) (0.9 + 0.1*(val[1]/10 - B)/n);
				}
				
				res += ((val[0]*2/10 - 1) * 4 * beta + val[2]) * 100 * 2/84;
			}else if(h.getType() == TYPE_THREE_GAME){
				int B = 0;
				if(val[1] == FLUSHHAND){
					B = (val[2] - 1) * 14 + val[3];
				}else{
					B = (val[2] - 1) * 4 + val[3];
				}
				
				res += (56*val[0] + 56*val[1] + B) * 100 * 3 /(56*7);
			}
		}
		return res;
	}

	public String toString(){
		String res = "";
		for(Card c:round)
			res += c.toString();
		return res;
	}
	
	public static void main(String[] args) {
//		CardDeck deck = new CardDeck(2);
//		Round r = deck.getARound();
		Card cards[] = new Card[6];
		cards[0] = new Card(1,'s');
		cards[1] = new Card(11,'d');
		cards[2] = new Card(2,'e');
		cards[3] = new Card(10,'h');
		cards[4] = new Card(9,'s');
		cards[5] = new Card(9,'e');
		Round r = new Round(cards);
		r.botThinking();
		System.out.println(r.toString());
		Queue<Hand> hands = r.getThreeHands();
		for(Hand h:hands)
			System.out.println(h.toString() + "  ");
		
		ArrayList<Hand[]> list = r.getThoughts();
		for(Hand[] handarray:list){
			for(Hand h:handarray){
				System.out.print(h.toString() + "  ");
			}
//			System.out.println(r.debugging(handarray));
			System.out.println("***************************");
		}
	}
}
