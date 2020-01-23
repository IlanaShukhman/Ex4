package algorithms;

import java.util.Collection;

import dataStructure.edge_data;
import dataStructure.graph;
import dataStructure.node_data;
import gameClient.Fruit_Client;
import utils.Point3D;

/**
 * This class represents a set of algorithms done for the project Ex4.
 * Currently, there is one public method - fetchFruitToEdge, 
 * which helps us with the AutomaticMove class. 
 *  
 * @author Ilana & Michael
 */

public class Ex4_Algo {
	private Double EPS=0.000000001;
	
	
	/**
	 * Search for the edge a given fruit is on. 
	 * @param fruit is a given fruit
	 * @return the edge it sits on.
	 */
	public edge_data fetchFruitToEdge(Fruit_Client fruit, graph g)
	{
		Point3D mid=fruit.getLocation();
		Collection<node_data> nodes=g.getV();
		
		for (node_data src : nodes) {
			Collection<edge_data> edges=g.getE(src.getKey());
			
			for (edge_data edge : edges) {
				Point3D start=src.getLocation();
				node_data dest=g.getNode(edge.getDest());
				Point3D end=dest.getLocation();
				
				//Check if it's on the right edge by definition and math
				if(fruitOnEdge(start, end, mid) &&
						((dest.getKey()- src.getKey()>0 && fruit.getType()==1)
								||( dest.getKey()-src.getKey()<0 &&fruit.getType()==-1))){
					//System.out.println("FETCH FRUIT TO EDGE, FRUIT: SRC="+edge.getSrc()+" DEST="+edge.getDest()+" TYPE="+fruit.getType());
					return edge;
				}//if
			}//for
		}//for
		System.out.println("NULL FRUIT");
		return null;
	}//fetchFruitToEdge
	
	/**
	 * A simple function to determine if a point is on an edge or not.
	 * @param start is the (x0,y0) point of the edge
	 * @param end is the (x1,y1) point of the edge
	 * @param point is a given point
	 * @return true if point is on the edge, else, return false.
	 */
	private boolean fruitOnEdge(Point3D start,Point3D end,Point3D point){
		return(Math.abs(start.distance2D(end)-(start.distance2D(point)+end.distance2D(point)))<=EPS);
	}//fruitOnEdge
	
}
