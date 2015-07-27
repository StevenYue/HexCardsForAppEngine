package steeng.hexcards.utility;

import static steeng.hexcards.datatype.SixCardsConstant.*;
import static steeng.hexcards.utility.SixCardsExceptions.*;

import org.datanucleus.exceptions.NucleusObjectNotFoundException;

import steeng.hexcards.datatype.Card;
import steeng.hexcards.datatype.Hand;
import steeng.hexcards.utility.SixCardsExceptions.TypeTwoGameDiedException;

public class SixCardsCalculator {
	
	
	/**
	 * return value, res[0] : total value;
	 * 				 res[1] : bigger rank value;
	 * 				 res[2] : suit
	 * @param hand
	 * @return
	 * @throws TypeTwoGameDiedException 
	 */
	public static int[] calTypeTwoHand(Hand hand) throws TypeTwoGameDiedException{
		int res[] = new int[3];
		if(hand.getJokerCounts()>0){
			if(hand.getCards()[0].getRank() == BIGJOKER && hand.getCards()[1].getRank() == BIGJOKER){
				res[0] = TEN_HALF; res[1] = TEN*10; res[2] = 4;
			}else{
				Card temp =  hand.getCards()[1];
				if(temp.isFaceCard()){
					res[0] = TEN_HALF; res[1] = TEN*10; res[2] = 4;
				}else if(temp.getRank() == TEN){
					res[0] = TEN_HALF; res[1] = TEN*10;
					switch(temp.getSuit()){
					case SPADE: res[2] = 4;break;
					case HEART: res[2] = 3;break;
					case CLUB: res[2] = 2; break;
					case DIAMOND: res[2] = 1;break;
					}
					
				}else{
					res[0] = TEN*10;
					res[1] = Math.max(temp.getRank(), TEN - temp.getRank())*10;
					char suit = temp.getRank() > TEN - temp.getRank()?temp.getSuit(): SPADE;
					switch(suit){
					case SPADE: res[2] = 4;break;
					case HEART: res[2] = 3;break;
					case CLUB: res[2] = 2; break;
					case DIAMOND: res[2] = 1;break;
					}
				}
			}
		}else{
			if(hand.getCards()[0].isFaceCard()) res[0] += 5;
			else res[0] += hand.getCards()[0].getRank()*10;
			if(hand.getCards()[1].isFaceCard()) res[0] += 5;
			else res[0] += hand.getCards()[1].getRank()*10;
			if(res[0] > TEN_HALF) throw new TypeTwoGameDiedException();
			
			res[1] = Math.max(hand.getCards()[0].isFaceCard()==true ? 5 : hand.getCards()[0].getRank()*10 ,
				hand.getCards()[1].isFaceCard()==true ? 5 : hand.getCards()[1].getRank()*10);
			
			
			if(hand.getCards()[0].isFaceCard() && hand.getCards()[1].isFaceCard()){
				char suit = (char) Math.max(hand.getCards()[0].getSuit(), hand.getCards()[1].getSuit());
				switch(suit){
				case SPADE: res[2] = 4;break;
				case HEART: res[2] = 3;break;
				case CLUB: res[2] = 2; break;
				case DIAMOND: res[2] = 1;break;
				}
			}else if(!hand.getCards()[0].isFaceCard() && !hand.getCards()[1].isFaceCard()){
				char suit = (char) Math.max(hand.getCards()[0].getSuit(), hand.getCards()[1].getSuit());
				switch(suit){
				case SPADE: res[2] = 4;break;
				case HEART: res[2] = 3;break;
				case CLUB: res[2] = 2; break;
				case DIAMOND: res[2] = 1;break;
				}
			}else{
				Card temp = hand.getCards()[0].isFaceCard()? hand.getCards()[1]:hand.getCards()[0];
				switch(temp.getSuit()){
				case SPADE: res[2] = 4;break;
				case HEART: res[2] = 3;break;
				case CLUB: res[2] = 2; break;
				case DIAMOND: res[2] = 1;break;
				}
			}
		}
		return res;
	}


	
	public static int[] calTypeThreeHand(Hand hand) {
		int res[] = new int[4];
		int cat = calTypeThreeCat(hand);
		int flushType = isFlushedHand(hand);
		if(cat == BOMB){
			res[0] = BOMB;
			if(flushType != NONEFLUSHHAND){
				res[1] = FLUSHHAND;
				res[2] = flushType;
				if(hand.getJokerCounts() == 3 || hand.getCards()[2].getRank()==ACE) 
					res[3] = ACEINBOMBORSTRAIGHT;
				else res[3] = hand.getCards()[2].getRank();
			}else{
				res[1] = NONEFLUSHHAND;
				res[2] = hand.getCards()[2].getRank();
			}
			
		}else if(cat == STRAIGHT){
			res[0] = STRAIGHT;
			if(flushType != NONEFLUSHHAND){
				res[1] = FLUSHHAND;
				res[2] = flushType;
				if(hand.getJokerCounts() == 1){
					if(hand.getCards()[1].getRank() - hand.getCards()[2].getRank() == 2)
						res[3] = hand.getCards()[1].getRank();
					else if(hand.getCards()[1].getRank() - hand.getCards()[2].getRank() == 1){
						res[3] = hand.getCards()[1].getRank() + 1;
					}else res[3] = ACEINBOMBORSTRAIGHT;
				}else{
					if(hand.getCards()[0].getRank() == KING && hand.getCards()[2].getRank()==ACE)
						res[3] = ACEINBOMBORSTRAIGHT;
					else res[3] = hand.getCards()[0].getRank();
				}
			}else{
				res[1] = NONEFLUSHHAND;
				if(hand.getJokerCounts() == 1){
					if(hand.getCards()[1].getRank() - hand.getCards()[2].getRank() == 2)
						res[2] = hand.getCards()[1].getRank();
					else if(hand.getCards()[1].getRank() - hand.getCards()[2].getRank() == 1){
						res[2] = hand.getCards()[1].getRank() + 1;
					}else res[2] = ACEINBOMBORSTRAIGHT;
				}else{
					if(hand.getCards()[0].getRank() == KING && hand.getCards()[2].getRank()==ACE)
						res[2] = ACEINBOMBORSTRAIGHT;
					else res[2] = hand.getCards()[0].getRank();
				}
			}
			
		}else if(cat ==  FLUSH){
			res[0] = FLUSH;
			res[1] = FLUSHHAND;
			res[2] = flushType;
			res[3] = 0;
		}else if(cat == PAIR){
			res[0] = PAIR;
			res[1] = NONEFLUSHHAND;
			res[2] = hand.getCards()[1].getRank();			
			
		}else{
			res[0] = HIGH_CARD;
			res[1] = NONEFLUSHHAND;
			res[2] = hand.getCards()[0].getRank();
			switch(hand.getCards()[0].getSuit()){
			case SPADE: res[3] = 4;break;
			case HEART: res[3] = 3;break;
			case CLUB: res[3] = 2; break;
			case DIAMOND: res[3] = 1;break;
			}
		}
		return res;
	}
	
	private static int isFlushedHand(Hand hand){
		char suit = 0;
		if(hand.getJokerCounts() == 3) suit = SPADE;
		if(hand.getJokerCounts() == 2) suit = hand.getCards()[2].getSuit();
		if(hand.getJokerCounts() == 1){
			if(hand.getCards()[1].getSuit() == hand.getCards()[2].getSuit()) 
				suit = hand.getCards()[2].getSuit();
			else suit = NONEFLUSHHAND;
		}else{
			if(hand.getCards()[1].getSuit() == hand.getCards()[2].getSuit() &&
					hand.getCards()[1].getSuit() == hand.getCards()[0].getSuit()) 
				suit =  hand.getCards()[0].getSuit();
			else suit = NONEFLUSHHAND;
		}
		int res = 0;
		switch(suit){
		case SPADE: res = 4;break;
		case HEART: res = 3;break;
		case CLUB: res = 2;break;
		case DIAMOND: res =  1;break;
		default: res = NONEFLUSHHAND;break;
		}
		return res;
	}
	
	private static int calTypeThreeCat(Hand hand){
		if(hand.getJokerCounts() >= 2) return BOMB;
		else if(hand.getJokerCounts() == 1){
			if(hand.getCards()[1].getRank() == hand.getCards()[2].getRank()) return BOMB;
			if((hand.getCards()[1].getRank() - hand.getCards()[2].getRank()) <= 2 ||
				(hand.getCards()[1].getRank()==QUEEN && hand.getCards()[2].getRank()==ACE)||
				(hand.getCards()[1].getRank()==KING && hand.getCards()[2].getRank()==ACE)) return STRAIGHT;
			if(hand.getCards()[1].getSuit() == hand.getCards()[2].getSuit()) return FLUSH;
			return PAIR;
		}else{
			if(hand.getCards()[1].getRank() == hand.getCards()[2].getRank() &&
					hand.getCards()[1].getRank() == hand.getCards()[0].getRank()) return BOMB;
			if((hand.getCards()[0].getRank() - hand.getCards()[1].getRank()) == 1 &&
					(hand.getCards()[1].getRank() - hand.getCards()[2].getRank()) == 1) return STRAIGHT;
			if(hand.getCards()[0].getRank()==KING && hand.getCards()[1].getRank() == QUEEN
					&&hand.getCards()[2].getRank() == ACE) return STRAIGHT;
			if(hand.getCards()[1].getSuit() == hand.getCards()[2].getSuit() &&
					hand.getCards()[1].getSuit() == hand.getCards()[0].getSuit()) return FLUSH;
			if(hand.getCards()[1].getRank() == hand.getCards()[2].getRank() ^
					hand.getCards()[1].getRank() == hand.getCards()[0].getRank()) return PAIR;
			return HIGH_CARD;
		}
	}
	
	
	
}
