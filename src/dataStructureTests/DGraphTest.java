package dataStructureTests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Collection;

import org.junit.jupiter.api.Test;

import dataStructure.DGraph;
import dataStructure.EdgeData;
import dataStructure.NodeData;
import dataStructure.edge_data;
import dataStructure.graph;
import dataStructure.node_data;
import utils.Point3D;

class DGraphTest {


	String ACTUAL;
	String EXPECTED;

	//Points for the location
	Point3D p1=new Point3D(1,1,1);
	Point3D p2=new Point3D(2,1,4);
	Point3D p3=new Point3D(10,0,10);
	Point3D p4=new Point3D(3,8,-1);
	Point3D p5=new Point3D(-1,-1,-1);
	Point3D p6=new Point3D(0,0,0);

	DGraph g=new DGraph();
	node_data n1=new NodeData(10,p1,2);
	node_data n2=new NodeData(21,p2,1);
	node_data n3=new NodeData(1,p3,3);
	node_data n4=new NodeData(2,p4,10);
	node_data n5=new NodeData(3,p5,1);
	{
		g.addNode(n1);
		g.addNode(n2);
		g.addNode(n3);
		g.addNode(n4);
		g.addNode(n5);
		g.connect(n2.getKey(), n3.getKey(), 4);
		g.connect(n4.getKey(), n3.getKey(), 5);
		g.connect(n3.getKey(), n4.getKey(), 1);
		g.connect(n1.getKey(), n5.getKey(), 5);
		g.connect(n4.getKey(), n2.getKey(), 5);
	}

	//test for running time: 100,000 nodes and 1,000,000 edges
	@Test
	void testDGraphTime() {
		DGraph graph=new DGraph();
		int i=0;
		while(i<100000) {
			Point3D p=new Point3D(i,i,i);
			node_data n=new NodeData(i,p);
			graph.addNode(n);
			i++;
		}
		i=0;
		int j;
		while(i<100000) {
			for (int k = 0; k < 10; k++) {
				j=(int) Math.random()*100000;
				if(i!=j)
					graph.connect(i, j, 5);
				else {
					j++;
					graph.connect(i, j, 5);
				}
			}
			
			i++;
		}

	}

	@Test
	void testGetNode() {
		ACTUAL=g.getNode(21).toString();
		EXPECTED=n2.toString();
		assertEquals(ACTUAL,EXPECTED,"ERR: GetNode failed to compare between the same nodes");
	}

	@Test
	void testGetEdge() {
		ACTUAL=g.getEdge(n2.getKey(), n3.getKey()).toString();
		EXPECTED=new EdgeData(n2.getKey(),n3.getKey(),4).toString();
		assertEquals(ACTUAL,EXPECTED,"ERR: GetEdge failed to compare between the same edges");

	}

	//@Test
	void testAddNode() {
		g.addNode(n3);

	}

	@Test
	void testConnect() {
		g.connect(n1.getKey(), n2.getKey(), 3);
		edge_data e=new EdgeData(n1.getKey(), n2.getKey(), 3);
		EXPECTED=e.toString();
		ACTUAL=g.getEdge(n1.getKey(), n2.getKey()).toString();
		//System.out.println(g.get_Edge_Hash().get(n1.getKey()).containsKey(n2.getKey()));
		assertEquals(ACTUAL,EXPECTED,"ERR: connect failed to compare between the same edges");

	}

	@Test
	void testRemoveNode() {
		

		g.removeNode(n3.getKey());
		boolean flag= true;
		Collection<node_data> actual=g.getV();	
		flag=actual.contains(n3);
		assertFalse(flag, "ERR: failed to remove the node");



	}

	@Test
	void testRemoveEdge() {
		g.removeEdge(21, 1);
		boolean flag=false;
		try {
			ACTUAL=g.getEdge(n2.getKey(), n3.getKey()).toString();
		} catch (Exception runtimeException) {
			flag=true;
		}
		assertTrue(flag, "ERR: failed to remove edge");

	}

	@Test
	void testNodeSize() {
		int ACTUAL=g.nodeSize();
		int EXPECTED=5;
		assertEquals(ACTUAL,EXPECTED,"ERR: falied to return the right size of node graph");
		g.removeNode(n1.getKey());
		ACTUAL=g.nodeSize();
		EXPECTED=4;
		assertEquals(ACTUAL,EXPECTED,"ERR: falied to return the right size of node graph");

	}

	@Test
	void testEdgeSize() {
		int ACTUAL=g.edgeSize();
		int EXPECTED=5;
		assertEquals(ACTUAL,EXPECTED,"ERR: falied to return the right size of edge graph");
		g.removeNode(n4.getKey());
		ACTUAL=g.edgeSize();
		EXPECTED=2;
		assertEquals(ACTUAL,EXPECTED,"ERR: falied to return the right size of edge graph");

	}

	@Test
	void testGetMC() {
		int ACTUAL=g.getMC();
		int EXPECTED=11;
		assertEquals(ACTUAL,EXPECTED,"ERR: falied to return the MC");
	}


}
