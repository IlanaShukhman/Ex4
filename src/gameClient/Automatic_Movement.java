package gameClient;

import java.util.List;
import java.util.Random;

import algorithms.Ex4_Algo;
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
	 * Constructor:
	 */
	public Automatic_Movement(Graph_Algo g_algo, List<Fruit_Client> fruits, List<Robot_Client> robots) {
		super();
		this.g_algo = g_algo;
		this.fruits = fruits;
		this.robots = robots;
	}





	/**
	 * Automatic next robot step by the recalculating path
	 * @param g is a given graph - roadmap
	 * @param src is the source node
	 * @param robot is a given robot
	 * @param numOfFruits is the number of fruits on the graph
	 * @return
	 */
	public int nextNodeAuto(graph g, int src, Robot_Client robot, int numOfFruites) {	
		Fruit_Client close_fruit=choose_Close_Fruites(robot,g,numOfFruites);
		Ex4_Algo algo=new Ex4_Algo();
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
	 * Choosing the fruit with the lowest distance and highest value by proportion 
	 * @param robot is a given robot
	 * @param g is the graph
	 * @return
	 */
	private Fruit_Client choose_Close_Fruites(Robot_Client robot,graph g,int numOfFruites) {
		int src=robot.get_src();
		double shortestpath=0;
		g_algo=new Graph_Algo(g);
		Fruit_Client target=robot.getTarget();
		double innerDist=g.getNode(target.getEdge().getSrc()).getLocation().distance2D(target.getLocation());
		double wholeDist=g.getNode(target.getEdge().getSrc()).getLocation().distance2D(g.getNode(target.getEdge().getDest()).getLocation());
		double porportion=(innerDist*wholeDist)*target.getValue();
		double min= (g_algo.shortestPathDist(src,target.getEdge().getSrc())+porportion);
		for (Fruit_Client fruit : fruits) {
			innerDist=g.getNode(fruit.getEdge().getSrc()).getLocation().distance2D(fruit.getLocation());
			wholeDist=g.getNode(fruit.getEdge().getSrc()).getLocation().distance2D(g.getNode(fruit.getEdge().getDest()).getLocation());
			porportion=(innerDist*wholeDist)*fruit.getValue();
			shortestpath=(g_algo.shortestPathDist(src,fruit.getEdge().getSrc())+porportion);
			if(alreadyTargeted(fruit)==-1 && min>shortestpath)
			{		
				min=shortestpath;
				target=fruit;
			}//if
			else if(alreadyTargeted(fruit)!=-1 && min>shortestpath && robots.get(alreadyTargeted(fruit)).getPathLength()>shortestpath)
			{
				min=shortestpath;
				target=fruit;
			}//else if
		}//for
		
		
		if(alreadyTargeted(target)!=-1) {//Changing the fruit randomally
			Random rand=new Random();
			int id=rand.nextInt(numOfFruites);
			robots.get(alreadyTargeted(target)).setPathLength(g_algo.shortestPathDist(robots.get(alreadyTargeted(target)).get_src(),fruits.get(id).getEdge().getSrc()));
			robots.get(alreadyTargeted(target)).setTarget(fruits.get(id));
			fruits.get(0).getEdge().setTag(1);
		}//if
		
		robot.getTarget().getEdge().setTag(0);
		robot.setTarget(target);
		target.getEdge().setTag(1);
		robot.setPathLength(min);
		return target;
	}//choose_Close_Fruites






	/**
	 * Check if is already targeted
	 * true: return the robot id 
	 * false: return -1
	 * @param f is a given fruit
	 * @return -1 if its not any robot's target. Else, returns the robot's ID. 
	 */
	private int alreadyTargeted(Fruit_Client f) {
		for (int i=0;i<robots.size();i++) {
			if(onTheSameEdge(robots.get(i).getTarget().getEdge().getSrc(), robots.get(i).getTarget().getEdge().getDest(), f.getEdge().getSrc(),  f.getEdge().getDest()))
				return i;
		}//for
		return -1;
	}//alreadyTarget
	
	private boolean onTheSameEdge(int src1,int dest1,int src2,int dest2){
		return ((src1==src2)&&(dest1==dest2)) || ((src1==dest2)&&(dest1==src2));
	}//onTheSameEdge
}
