package steeng.hexcards.listener;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import steeng.hexcards.datatype.Game;
import static steeng.hexcards.datatype.SixCardsConstant.*;

public class ContextInitializeListener implements ServletContextListener{

	public void contextInitialized(ServletContextEvent sce) {
		// TODO Auto-generated method stub
		System.out.println("Game SixCards Services Started");
		ServletContext appScope = sce.getServletContext();
		//list of online clients 
		final Set<String> playerSet = Collections.synchronizedSet(new HashSet<String>());
		final Map<String,Game> gameMap= Collections.synchronizedMap(new HashMap<String,Game>());
		final List<String> roomList = Collections.synchronizedList(new ArrayList<String>());
		for(int i=0;i<5;i++){
			roomList.add("");
		}
		for(int i=0;i<BOT_NAMES.length;i++){
			playerSet.add(BOT_NAMES[i]);
		}
//		final Map<String,String> playerMsgMap= Collections.synchronizedMap(new HashMap<String,String>());
//		appScope.setAttribute(PLAYERMSGMAP, playerMsgMap);
		appScope.setAttribute(GAMEMAP, gameMap);
		appScope.setAttribute(PLAYERSET, playerSet);
		appScope.setAttribute(ROOMLIST, roomList);
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// TODO Auto-generated method stub
		System.out.println("Game SixCards Services Stopped");
	}

}
