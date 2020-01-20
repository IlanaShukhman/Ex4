package algorithms;

import java.util.Collection;
import java.util.List;

import dataStructure.DGraph;
import dataStructure.edge_data;
import dataStructure.graph;
import dataStructure.node_data;
import gameClient.Fruit_Client;
import utils.Point3D;

public class Ex3_Algo {
	private Double EPS=0.000000001;
	/**
	 * Searching all the edges of the graph and check the right edge of given fruit 
	 * @param fruit
	 * @return
	 */
	public edge_data fetchFruitToEdge(Fruit_Client fruit,graph g)
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
				if(fruitOnEdge(start, end, mid) && ((dest.getKey()- src.getKey()>0 && fruit.getType()==1)||( dest.getKey()-src.getKey()<0 &&fruit.getType()==-1)))
				{
					//System.out.println("FETCH FRUIT TO EDGE, FRUIT: SRC="+edge.getSrc()+" DEST="+edge.getDest()+" TYPE="+fruit.getType());
					return edge;
				}//if
			}//for
		}//for
		System.out.println("NULL FRUIT");
		return null;
	}//fetchFruitToEdge
	
	private boolean fruitOnEdge(Point3D start,Point3D end,Point3D mid){
		return(Math.abs(start.distance2D(end)-(start.distance2D(mid)+end.distance2D(mid)))<=EPS);
	}//fruitOnEdge
	
}
