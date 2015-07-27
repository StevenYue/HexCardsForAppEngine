package steeng.hexcards.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONException;
import org.json.JSONObject;

import steeng.hexcards.services.*;
import static steeng.hexcards.datatype.SixCardsConstant.*;
/**
 * Servlet implementation class LoginServlet
 */

public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
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
		String userID = null, password = null;
		
		userID = request.getParameter("ID");
		password = request.getParameter("password");
		
		//brain here means the central unit, memorizing everything within this application
		ServletContext brain = getServletContext();
	
		Set<String> playerSet = (Set<String>) brain.getAttribute(PLAYERSET);
		LoginService loginService = LoginService.getLoginService();
		
		int res = loginService.varify(userID, password, playerSet);
		if (res > 0) {
			// perfect match
			playerSet.add(userID);
			// jMSG.put(MSG_TYPE_ACTION, MSG_RELOAD_PAGE_ON_SUCCESS);
			request.setAttribute("ID", userID);
			RequestDispatcher dispatcher = request.getRequestDispatcher("gameplay.jsp");
			dispatcher.forward(request, response);
		} else if (res == -1) {
			// password is wrong
			// jMSG.put(MSG_TYPE_ACTION, MSG_PASSWORDINCORRECT);
			// System.out.println("Password wrong");
			response.sendRedirect("loginError.html?MSG=passwordwrong");
		} else if (res == -2) {
			// id existed
			// jMSG.put(MSG_TYPE_ACTION, MSG_USERNAMEUSED);
			// System.out.println("User Existed");
			response.sendRedirect("loginError.html?MSG=userexisted");
		} else {
			// no such user
			// jMSG.put(MSG_TYPE_ACTION, MSG_NOSUCHUSER);
			// System.out.println("No such user");
			response.sendRedirect("loginError.html?MSG=nosuchuser");
		}
					
			//Now update the online user info in the playerMsgMap.
	}
}
