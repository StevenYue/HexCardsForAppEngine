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
public class GetOutofGameRequest extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetOutofGameRequest() {
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
		Map<String, Game> gameMap = (Map<String, Game>) brain.getAttribute(GAMEMAP);
		Set<String> userSet = (Set<String>) brain.getAttribute(PLAYERSET);
		
		
		String userId = request.getParameter("ID");
		String alterID =  userId +" (In Game)"; 
		String gameID = request.getParameter("GAMEID");
		gameMap.remove(gameID);		
		if(userSet.contains(alterID)){
			userSet.remove(alterID);
			userSet.add(userId);
		}
	}

	
}
