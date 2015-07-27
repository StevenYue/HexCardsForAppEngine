package steeng.hexcards.servlet;

import static steeng.hexcards.datatype.SixCardsConstant.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import steeng.hexcards.datatype.Game;

/**
 * Servlet implementation class Logoff
 */
public class Logoff extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Logoff() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		ServletContext brain = getServletContext();
		Set<String> userSet = (Set<String>) brain.getAttribute(PLAYERSET);
		List<String> roomList = (List<String>) brain.getAttribute(ROOMLIST);
		Map<String, Game> gameMap = (Map<String, Game>) brain.getAttribute(GAMEMAP);
		
		String userId = request.getParameter("ID");
		String alterID =  userId +" (In Game)"; 
		String gameID = request.getParameter("GAMEID");
		Game game = null;
		int roomIndex = Integer.parseInt(request.getParameter("ROOMID")) - 1;
		if(userSet.contains(userId))
			userSet.remove(userId);
		
		if(userSet.contains(alterID)){
			userSet.remove(alterID);
			if(gameMap.containsKey(gameID)){
				game = gameMap.get(gameID);
				if(game.isGameWithBot()){
					gameMap.remove(gameID);
					String botId = "";
					if(game.getPlayer1ID().equals(userId)){ //p2 is bot
						botId = game.getPlayer2ID();
					}else{ // p1 is bot
						botId = game.getPlayer1ID();
					}
					String alterBotId = botId + " (In Game)";
					if(userSet.contains(alterBotId))
						userSet.remove(alterBotId);
					
					userSet.add(botId);
				}else{
					if(game.getPlayer1ID().equals(userId)){ //This if P1 log off
						game.setP1MsgReceived(true);
						game.setPlayer1ID("");
						if(game.getPlayer2ID().equals(""))	gameMap.remove(gameID);
						
					}else{
						game.setP2MsgReceived(true);
						game.setPlayer2ID("");
						if(game.getPlayer1ID().equals(""))	gameMap.remove(gameID);
					}
				}
				
			}
		}
			

		if(roomIndex > -1){
			String str = roomList.get(roomIndex);
			if(str.contains(userId)){// means you already in a room
				if(game != null && game.isGameWithBot()){
					roomList.set(roomIndex,"");
				}else{
					String ss[] = str.split("-");
					String strToPutBack = "";
					if(ss[0].equals(userId)){
						if(ss.length == 2)
							strToPutBack = ss[1] + "-" ;
					}else{
						strToPutBack = ss[0] + "-" ;
					}
					roomList.set(roomIndex,strToPutBack);
				}
				
				
			}
		}
			
			
	}

	
}
