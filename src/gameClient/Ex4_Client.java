package gameClient;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.swing.JOptionPane;

import Server.Game_Server;
import Server.game_service;
import algorithms.Ex3_Algo;
import algorithms.Graph_Algo;
import dataStructure.DGraph;
import dataStructure.edge_data;
import dataStructure.graph;
import utils.Point3D;
/**
 * This class represents a simple example for using the GameServer API:
 * the main file performs the following tasks:
 * 0. login as a user ("999") for testing - do use your ID.
 * 1. Creates a game_service [0,23] (user "999" has stage 9, can play in scenarios [0,9] not above
 *    Note: you can also choose -1 for debug (allowing a 600 second game).
 * 2. Constructs the graph from JSON String
 * 3. Gets the scenario JSON String 
 * 5. Add a set of robots  // note: in general a list of robots should be added
 * 6. Starts game 
 * 7. Main loop (vary simple thread)
 * 8. move the robot along the current edge 
 * 9. direct to the next edge (if on a node) 
 * 10. prints the game results (after "game over"), and write a KML: 
 *     Note: will NOT work on case -1 (debug).
 *  
 * @author boaz.benmoshe
 *
 */

public class Ex4_Client implements Runnable{

	private static List<Robot_Client> robots;
	private static List<Fruit_Client> fruits;
	private static MyGameGUI gui;
	private static DGraph gameGraph;
	private static Graph_Algo g_algo;


	public static void main(String[] a) {
		Thread client = new Thread(new Ex4_Client());
		client.start();
	}

	@Override
	public void run() {

		String getID=JOptionPane.showInputDialog(this, "Type in your ID:");
		int id = Integer.valueOf(getID);
		Game_Server.login(id);

		//Create Graph
		String s=chooseScenarioFromList();
		//if the user decided to cancel
		if(s==null)
			return;

		int scenario_num =Integer.valueOf(s);


		try {
			game_service g = Game_Server.getServer(scenario_num);
		}catch(Exception e) {
			JOptionPane.showInputDialog("you are trying to play in a level above yours!");

		}
		game_service game = Game_Server.getServer(scenario_num);
		String g = game.getGraph();
		gameGraph = new DGraph();
		gameGraph.init(g);
		//init(game);

		game.startGame();

		//Create the lists of robots and fruits
		robots=new ArrayList<Robot_Client>();
		fruits=new ArrayList<Fruit_Client>();



		//Game Server information such as:fruites,moves,grade,robots,graph,data
		String info = game.toString();
		GameServer_Client gameServer=new GameServer_Client();
		gameServer.initFromJson(info);
		int numRobots = gameServer.get_robots_number();

		System.out.println(gameServer);
		System.out.println(g);

		Ex3_Algo ex3_alg=new Ex3_Algo();

		// update and displaying the fruites
		int numFruits = gameServer.get_fruits_number();
		for (int i = 0; i < numFruits; i++) {
			Fruit_Client fruit=new Fruit_Client();
			fruit.initFromJson(game.getFruits().get(i));
			edge_data edge=ex3_alg.fetchFruitToEdge(fruit, gameGraph);
			fruit.setEdge(edge);
			fruits.add(fruit);
		}//for

		Comparator<Fruit_Client> compare=new Comparator<Fruit_Client>() {

			@Override
			public int compare(Fruit_Client f1, Fruit_Client f2) {
				int dp =(int)(f2.getValue()-f1.getValue());
				return dp;
			}
		};


		fruits.sort(compare);
		System.out.println(fruits.toString());


		for(int i = 0;i<numRobots;i++) {
			game.addRobot(fruits.get(i).getEdge().getSrc());
			Robot_Client r=new Robot_Client();
			r.initFromJson(game.getRobots().get(i));
			robots.add(i, r);
			robots.get(i).setTarget(fruits.get(i));
			System.out.println(r);
		}//for

		gui=new MyGameGUI(gameGraph, robots, fruits, id, scenario_num);
		game.startGame();
		gui.setIsRunning(true);
		gui.setLevel(scenario_num);
		System.out.println(gameServer.get_data());




		//int ind=0;
		long dt=100;
		int jj = 0;


		while(game.isRunning()) {
			moveRobots(game, gameGraph);

			try {
				List<String> stat = game.getRobots();
				for(int i=0;i<stat.size();i++) {
					//System.out.println(jj+") "+stat.get(i));
				}
				//ind++;
				Thread.sleep(dt);
				jj++;
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}




		

		KML_Logger kmlfile = new KML_Logger(scenario_num, gameGraph, robots, fruits, game);
		String res = game.toString();
		String remark = kmlfile.getKMLFile();
		game.sendKML(remark); // Should be your KML (will not work on case -1).
		System.out.println(res);



	}

	/** 
	 * Moves each of the robots along the edge, 
	 * in case the robot is on a node the next destination (next edge) is chosen (randomly).
	 * @param game
	 * @param gg
	 * @param log
	 */
	private static void moveRobots(game_service game, graph graph) {
		List<String> log = game.move();

		if(log!=null) {
			long t = game.timeToEnd();
			gui.setTimeToEnd(t/1000);
			for(int i=0;i<log.size();i++) {

				String info = game.toString();
				GameServer_Client gameServer=new GameServer_Client();
				gameServer.initFromJson(info);

				gui.setScore(gameServer.get_grade());
				gui.setMoves(gameServer.get_number_of_moves());
				String robot_json = log.get(i);
				Robot_Client robot=new Robot_Client();
				robot.initFromJson(robot_json);
				int rid = robot.get_id();
				int src = robot.get_src();
				int dest = robot.get_dest();
				Point3D pos = robot.get_pos();
				robots.get(i).set_pos(pos);


				//if it is automatic
				if(gui.getState()==1) {

					Automatic_Movement am = new Automatic_Movement(g_algo, fruits, robots);
					dest = am.nextNodeAuto(graph, src, robots.get(i));
					robot.set_dest(dest);	
					game.chooseNextEdge(rid, dest);
				}//if

				//if it is manual
				else if(gui.getState()==0) {
					robot=gui.getSelectedRobot();
					dest=gui.getSelectedNode();
					Manual_Movement mm = new Manual_Movement(g_algo, gameGraph, robots, fruits);

					//after the user clicked 
					if(robot!=null && dest!=-1) {
						if(mm.okayToGo(dest)) {
							robot.set_dest(dest);		
						}
						int d = mm.nextNodeManual(src, robots.get(i).get_dest());
						game.chooseNextEdge(rid, d);
					}
				}//else if

				updateSrc();
			}//for

			updateFruites(game);
		}
	}

	/**
	 * Pop up window to determine which scenario the client wants
	 * @return string of the chosen value
	 */
	private static String chooseScenarioFromList() {
		String[] choices = new String [24];
		for (int i = 0; i < choices.length; i++) {
			choices[i]=String.valueOf(i);
		}//for
		String input = (String) JOptionPane.showInputDialog(null, "Please choose the level from [0,23]",
				"The Maze Of Waze", JOptionPane.QUESTION_MESSAGE, null,choices,choices[0]);
		return input;
	}//chooseFromList


	/**
	 * Extract information of the fruites from server in Json language and Update them
	 * @param game
	 */
	private static void updateFruites(game_service game) {
		List<String> fruitInformation=game.getFruits();

		for (int i = 0; i < fruitInformation.size(); i++) {
			Fruit_Client fruit=new Fruit_Client();
			fruit.initFromJson(fruitInformation.get(i));
			fruits.get(i).set_pos(fruit.getLocation());
		}//for

	}//updateFruites


	/**
	 * This function 
	 */
	private static void updateSrc() {
		for(Robot_Client robot: robots) {
			for(Integer node : gameGraph.get_Node_Hash().keySet()) {
				if(isClose(robot.get_pos(), gameGraph.getNode(node).getLocation())){
					robot.set_src(node);
				}
			}
		}

	}

	private static boolean isClose(Point3D node1, Point3D node2) {
		if(node1.distance2D(node2)<0.0005)
			return true;
		return false;
	}





}
