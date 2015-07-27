package steeng.hexcards.datatype;
import static steeng.hexcards.datatype.SixCardsConstant.*;


public class Game {
	private String gameID;
	private String player1ID;
	private String player2ID;
	private CardDeck cardDeck;
	private Hand player1Hand;
	private Hand player2Hand;
	private Round p1Round;
	private Round p2Round;
	private String gameMsg = null;
	private boolean p1MsgReceived = false;
	private boolean p2MsgReceived = false;
	
	private boolean gameWithBot = false;
	
	//n is the amount of card decks
	public Game(String gameID){
		this.player1Hand = null;
		this.player2Hand = null;
		this.p1Round = null;
		this.p2Round = null;
		this.gameID = gameID;
		String ss[] = gameID.split("-");
		this.player1ID = ss[0].trim();
		this.player2ID = ss[1].trim();
		if(gameID.contains(BOT_IDENTIFIER)) gameWithBot = true;
		
		cardDeck = new CardDeck(2);
	}

	//All getter and setter
	public boolean isGameWithBot(){return gameWithBot;}
	public String getGameID() {return gameID;}
	public String getPlayer1ID() {return player1ID;}
	public void setPlayer1ID(String id) {player1ID = id;}
	public String getPlayer2ID() {return player2ID;}
	public void setPlayer2ID(String id) {player2ID = id;}
	public CardDeck getCardDeck() {return cardDeck;}
	public Hand getPlayer1Handin() {return player1Hand;}
	public void setPlayer1Handin(Hand player1Handin) {this.player1Hand = player1Handin;}
	public Hand getPlayer2Handin() {return player2Hand;}
	public void setPlayer2Handin(Hand player2Handin) {this.player2Hand = player2Handin;}
	public String getGameMsg() {return gameMsg;}
	public void setGameMsg(String gameMsg) {this.gameMsg = gameMsg;}
	
	
	public Round getP1Round() {return p1Round;}
	public void setP1Round(Round p1Round) {
		if(player1ID.contains(BOT_IDENTIFIER)) p1Round.botThinking();
		this.p1Round = p1Round;
	}
	public Round getP2Round() {return p2Round;}
	public void setP2Round(Round p2Round) {
		if(player2ID.contains(BOT_IDENTIFIER)) p2Round.botThinking();
		this.p2Round = p2Round;
	}

	public boolean isP1MsgReceived() {
		if(player1ID.contains(BOT_IDENTIFIER)) p1MsgReceived = true;
		return p1MsgReceived;
	}

	public void setP1MsgReceived(boolean p1MsgReceived) {
		this.p1MsgReceived = p1MsgReceived;
	}

	public boolean isP2MsgReceived() {
		if(player2ID.contains(BOT_IDENTIFIER)) p2MsgReceived = true;
		return p2MsgReceived;
	}

	public void setP2MsgReceived(boolean p2MsgReceived) {
		this.p2MsgReceived = p2MsgReceived;
	}
	
}
