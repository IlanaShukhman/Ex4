package algorithmsTest;

import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;


import algorithms.Graph_Algo;
import dataStructure.DGraph;
import dataStructure.NodeData;
import dataStructure.node_data;
import utils.Point3D;

class Graph_AlgoTest {

	DGraph ACTUAL;
	DGraph EXPECTED;
	Graph_Algo graphAlgo;

	public void graph_Factory()
	{
		this.EXPECTED=new DGraph();
		Point3D p0=new Point3D(0,0);
		Point3D p1=new Point3D(1,1);
		Point3D p2=new Point3D(2,2);
		Point3D p3=new Point3D(3,3);
		Point3D p4=new Point3D(4,4);

		this.EXPECTED.addNode(new NodeData(0, p0));
		this.EXPECTED.addNode(new NodeData(1, p1));
		this.EXPECTED.addNode(new NodeData(2, p2));
		this.EXPECTED.addNode(new NodeData(3, p3));
		this.EXPECTED.addNode(new NodeData(4, p4));
		
		this.EXPECTED.connect(0, 1, 1);
		this.EXPECTED.connect(1, 2, 2);
		this.EXPECTED.connect(2, 3, 3);
		this.EXPECTED.connect(3, 4, 4);
		this.EXPECTED.connect(0, 4, 4);

	}


	@Test
	void testInitGraph() {
		graph_Factory();
		
		this.graphAlgo= new Graph_Algo();
		this.graphAlgo.init(EXPECTED);
		assertEquals(EXPECTED, graphAlgo.getGraph(),"ERR:Failing saving to init and save to file correctly.");
	}

	@Test
	void equalsTest() {
		graph_Factory();
		this.graphAlgo= new Graph_Algo(this.EXPECTED);
		Graph_Algo g=new Graph_Algo(EXPECTED);
		assertEquals(graphAlgo, g);
	}//equalsTest
	
	@Test
	void testIsConnected() {
		graph_Factory();
		EXPECTED.connect(4, 0, 5);
		this.graphAlgo= new Graph_Algo((DGraph) this.EXPECTED);
		boolean e=this.graphAlgo.isConnected();
		assertTrue(e,"ERR:Failed to return false when graph is not connected");

	}

	@Test
	void testShortestPathDist() {
		graph_Factory();
		this.EXPECTED.connect(0, 4, 11);
		this.graphAlgo= new Graph_Algo((DGraph) this.EXPECTED);
		double actual=this.graphAlgo.shortestPathDist(0, 4);
		double expected=10;
		assertEquals(expected, actual,"ERR: Failing to get length of shortest path distination. Expected: "+expected+" Actual: "+actual);
	}//testShortestPathDist

	@Test
	void testShortestPath() {
		graph_Factory();
		this.graphAlgo= new Graph_Algo((DGraph) this.EXPECTED);
		List<node_data> expected=new ArrayList<node_data>();
		expected.add(this.EXPECTED.get_Node_Hash().get(0));
		expected.add(this.EXPECTED.get_Node_Hash().get(4));
		List<node_data> actual=this.graphAlgo.shortestPath(0, 4);
		assertEquals(expected, actual,"ERR: Failing to save the shortest path. Expected: "+expected+" Actual: "+actual);
	}

	@Test
	void testTSP() {
		graph_Factory();
		this.EXPECTED.connect(1, 0, 2);
		this.EXPECTED.connect(0, 2, 3);
		this.EXPECTED.connect(2, 0, 4);
		this.EXPECTED.connect(0, 3, 4);
		this.graphAlgo= new Graph_Algo(this.EXPECTED);
		List<Integer> targets=new ArrayList<Integer>();
		targets.add(0);
		targets.add(1);
		targets.add(2);
		List<node_data> actual=this.graphAlgo.TSP(targets);
		List<node_data> expected=new ArrayList<node_data>();
		expected.add(this.EXPECTED.getNode(0));
		expected.add(this.EXPECTED.getNode(1));
		expected.add(this.EXPECTED.getNode(2));
		assertEquals(expected, actual,"ERR:Failing to TSP");
	}

	@Test
	void testCopy() {
		graph_Factory();
		this.ACTUAL=new DGraph(this.EXPECTED);
		assertEquals(this.EXPECTED, this.ACTUAL,"ERR: Failing to copy Graph");
	}//testCopy

}
