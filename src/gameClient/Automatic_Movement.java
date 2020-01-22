package gameClient;

import java.util.List;

import algorithms.Ex3_Algo;
import algorithms.Graph_Algo;
import dataStructure.graph;
import dataStructure.node_data;

/**
 * This class represents the auto-move decision making. 
 * @author Ilana && Michael
 *
 */

public class Automatic_Movement {
	private Graph_Algo g_algo;
	private List<Fruit_Client> fruits;
	private List<Robot_Client> robots;
	
	/**
	 * Constructor
	 * @param g_algo
	 * @param fruits
	 * @param robots
	 */
	public Automatic_Movement(Graph_Algo g_algo, List<Fruit_Client> fruits, List<Robot_Client> robots) {
		super();
		this.g_algo = g_algo;
		this.fruits = fruits;
		this.robots = robots;
	}

	
	
	

	/**
	 * Automatic next robot step by the recalculating path
	 * @param g
	 * @param src
	 * @return
	 */
	public int nextNodeAuto(graph g, int src,Robot_Client robot) {	
		if(robot.get_id()==1)
			return src;
		Fruit_Client close_fruit=choose_Close_Fruites(robot,g);
		Ex3_Algo algo=new Ex3_Algo();
		close_fruit.setEdge(algo.fetchFruitToEdge(close_fruit, g));

		g_algo=new Graph_Algo(g);
		List<node_data> path=g_algo.shortestPath(src, close_fruit.getEdge().getSrc());
		
		
		
		if(path.size()==1)
			return close_fruit.getEdge().getDest();
		
		
		path.add(g.getNode(close_fruit.getEdge().getDest()));
		int dest=path.get(1).getKey();
		g.getNode(dest).setInfo(String.valueOf(src));
		robot.setTarget(close_fruit);
		
		return dest;
	}

	/**
	 * Choosing the fruit with the lowest distanse and highest value by proportion 
	 * @param robot
	 * @param g
	 * @return
	 */
	private Fruit_Client choose_Close_Fruites(Robot_Client robot,graph g) {

		int src=robot.get_src();
		float shortestpath=0;
		g_algo=new Graph_Algo(g);
		//g_algo.BFS(src);
		Fruit_Client target=robot.getTarget();
		float min=(float) ((g_algo.shortestPathDist(src,target.getEdge().getSrc())+g.getNode(target.getEdge().getSrc()).getLocation().distance2D(target.getLocation())));
		//float min=(float) ((g.getNode(target.getEdge().getSrc()).getWeight()+g.getNode(target.getEdge().getSrc()).getLocation().distance2D(target.getLocation()))/target.getValue());
		for (Fruit_Client fruit : fruits) {
			if(alreadyTargeted(fruit)==-1)
			{
				double innerDistance=g.getNode(fruit.getEdge().getSrc()).getLocation().distance3D(fruit.getLocation());
				shortestpath=(float) (float) ((g_algo.shortestPathDist(src,fruit.getEdge().getSrc())+innerDistance));
				//shortestpath=(float) ((g.getNode(fruit.getEdge().getSrc()).getWeight()+g.getNode(fruit.getEdge().getSrc()).getLocation().distance2D(target.getLocation()))/fruit.getValue());
				if(min>shortestpath )
				{
					//System.out.println("Change the min was: "+min+" Now: "+shortestpath);
					min=shortestpath;
					target=fruit;
				}//if
			}//else
		}//for

		robot.setTarget(target);
		target.getEdge().setTag(1);
		return target;
	}//choose_Close_Fruites

	
	/**
	 * Check if is already targeted
	 * true: return the robot id 
	 * false: return -1
	 * @param f
	 * @return
	 */
	private int alreadyTargeted(Fruit_Client f) {
		for (int i=0;i<robots.size();i++) {
			if(robots.get(i).getTarget().equals(f))
				return i;
		}//for
		return -1;
	}//alreadyTarget

}
