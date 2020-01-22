package algorithmsTest;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.jupiter.api.Test;

import Server.Game_Server;
import Server.game_service;
import algorithms.Ex3_Algo;
import dataStructure.DGraph;
import dataStructure.EdgeData;
import dataStructure.edge_data;
import gameClient.Fruit_Client;
import gameClient.GameServer_Client;
import gameClient.Robot_Client;

class Ex3_AlgoTest {

	private static List<Robot_Client> robots;
	private static List<Fruit_Client> fruits;

	@Test
	final void testFetchFruitToEdge() {
		//Choose scenario num
		int scenario_num = 0;
		game_service game = Game_Server.getServer(scenario_num); // you have [0,23] games
		//Create Graph
		String g = game.getGraph();
		DGraph gameGraph = new DGraph();
		gameGraph.init(g);
		//Create the lists of robots and fruits
		robots=new ArrayList<Robot_Client>();
		fruits=new ArrayList<Fruit_Client>();
		//Game Server information such as:fruites,moves,grade,robots,graph,data
		String info = game.toString();


		GameServer_Client gameServer=new GameServer_Client();
		gameServer.initFromJson(info);
		int numRobots = gameServer.get_robots_number();

		System.out.println(info);
		System.out.println(g);

		// the list of fruits should be considered in your solution
		Iterator<String> f_iter = game.getFruits().iterator();
		while(f_iter.hasNext()) {System.out.println(f_iter.next());}

		int src_node = 0;  // arbitrary node, you should start at one of the fruits

		for(int a = 0;a<numRobots;a++) {
			game.addRobot(src_node+a);

			Robot_Client r=new Robot_Client();
			r.initFromJson(game.getRobots().get(a));
			robots.add(r);
		}//for

		int numFruits = gameServer.get_fruits_number();
		for (int i = 0; i < numFruits; i++) {
			Fruit_Client fruit=new Fruit_Client();
			fruit.initFromJson(game.getFruits().get(i));
			fruits.add(fruit);

		}//for


		Fruit_Client fruit= fruits.get(0);
		
		Ex3_Algo a=new Ex3_Algo();

		edge_data EXPECTED=new EdgeData(9,8,1.8);
		edge_data ACTUAL=a.fetchFruitToEdge(fruit, gameGraph);
		assertEquals(EXPECTED.getSrc(),ACTUAL.getSrc(),"ERR: failed to return true when the src nodes are the same");
		assertEquals(EXPECTED.getDest(),ACTUAL.getDest(),"ERR: failed to return true when the dst nodes are the same");
		



	}

}
