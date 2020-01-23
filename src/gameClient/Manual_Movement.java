package gameClient;

import java.util.List;

import algorithms.Graph_Algo;
import dataStructure.DGraph;
import dataStructure.node_data;

/**
 * This class represents the manual-move decision making. 
 * @author Ilana && Michael
 */

public class Manual_Movement {
	private Graph_Algo g_algo;
	private DGraph gameGraph;
	private List<Robot_Client> robots;
	
	
	public Manual_Movement(Graph_Algo g_algo, DGraph gameGraph, List<Robot_Client> robots) {
		super();
		this.g_algo = g_algo;
		this.gameGraph = gameGraph;
		this.robots = robots;
	}

	/**
	 * Returns the robot's next node in a manual mode.
	 * @param src is the robot's source node
	 * @param dest is the destination node
	 * @param i 
	 * @return the next node in the robot's path to the dest node.
	 */
	public int nextNodeManual(int src, int dest, int i) {
		if(dest==-1)
			return -1;
		g_algo = new Graph_Algo(gameGraph);
		List<node_data> path=g_algo.shortestPath(src, dest);
		List<Integer> path_key=g_algo.NodeToKeyConverter(path);
		this.robots.get(i).setPath(path);
		if(path_key.size()==0) {
			return -1;
		}
		else if(path_key.size()==1) {
			return path_key.get(0);
		}
		return path_key.get(1);
	}
	
	

	/**
	 * Return false if dest is a node that a robot is on-
	 * that means the user probably tried to pick up the robot, and not the node.
	 * @param dest is the node the user clicked on
	 * @return false if there is a robot on dest - else, true, and it is okay to go there.
	 */
	public boolean okayToGo(int dest) {
		if(robots.size()==1)
			return true;
		for(Robot_Client robot : robots) {
			if(gameGraph.get_Node_Hash().get(dest).getLocation().equals(robot.get_pos())) {
				return false;
			}				
		}


		return true;
	}


}
