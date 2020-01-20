package dataStructure;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;
import utils.Point3D;

/**
 * This class represents a directional weighted graph.
 * The interface has a road-system or communication network in mind - and should support a large number of nodes (over 100,000).
 * The implementation is based on HashMap, which allows most of the functions to run in O(1) time.
 *
 */
public class DGraph implements graph,Serializable{
	private HashMap<Integer, node_data> Nodes;
	private HashMap<Integer, HashMap<Integer, edge_data>> Edges;
	private int ModeCount;
	private int EdgeHashSize;
	
	boolean IOException;
	boolean ClassNotFoundException;

	/**
	 * Empty Constructor
	 */
	public DGraph() {
		this.Nodes=new HashMap<Integer, node_data>();
		this.Edges=new HashMap<Integer, HashMap<Integer, edge_data>>();
		this.ModeCount=0;
		this.EdgeHashSize=0;
	}

	/**
	 * Shallow copy of given vertices and edges
	 * @param Node_Hash
	 * @param Edge_Hash
	 */
	public DGraph(HashMap<Integer, node_data> Node_Hash, HashMap<Integer, HashMap<Integer, edge_data>>Edge_Hash) {
		this.Nodes=Node_Hash;
		this.Edges = Edge_Hash;


	}//DGraph

	/**
	 * Shallow copy of given Graph
	 * @param g
	 */
	public DGraph(DGraph g) {

		this.Nodes=g.get_Node_Hash(); 
		this.Edges=g.get_Edge_Hash();
		this.ModeCount = g.getMC();
		this.EdgeHashSize = g.edgeSize();
	}//DGraph
	/**
	 * Init a graph from file
	 * @param file_name
	 */
	public void init(String data) {
		 try {
			 	JSONObject graph = new JSONObject(data);
	            JSONArray nodes = graph.getJSONArray("Nodes");
	            JSONArray edges = graph.getJSONArray("Edges");

	            int i;
	            int s;
	            for(i = 0; i < nodes.length(); ++i) {
	                s = nodes.getJSONObject(i).getInt("id");
	                String pos = nodes.getJSONObject(i).getString("pos");
	                Point3D p = new Point3D(pos);
	                this.addNode(new NodeData(s, p));
	            }

	            for(i = 0; i < edges.length(); ++i) {
	                s = edges.getJSONObject(i).getInt("src");
	                int d = edges.getJSONObject(i).getInt("dest");
	                double w = edges.getJSONObject(i).getDouble("w");
	                this.connect(s, d, w);
	            }
	        } catch (Exception var12) {
	            var12.printStackTrace();
	        }

	}//init
	/**
	 * Getters.
	 */
	public HashMap<Integer, node_data> get_Node_Hash(){
		return this.Nodes;
	}
	public HashMap<Integer, HashMap<Integer, edge_data>> get_Edge_Hash(){
		return this.Edges;
	}


	/**
	 * Return the node_data by the node_id,
	 * @param key - the node_id
	 * @return the node_data by the node_id, null if none.
	 */
	public node_data getNode(int key) {
		if(!Nodes.containsKey(key))
			return null;
		return Nodes.get(key);
	}

	/**
	 * return the data of the edge (src,dest), null if none.
	 * Note: this method should run in O(1) time.
	 * @param src
	 * @param dest
	 * @return the data of the edge (src,dest), null if none.
	 */
	public edge_data getEdge(int src, int dest) {
		if(!Nodes.containsKey(src)||!Nodes.containsKey(dest))
			return null;

		if(this.Edges.containsKey(src)) {
			HashMap<Integer,edge_data> h=Edges.get(src);
			if(h.containsKey(dest)) {
				return h.get(dest);
			}//if
		}//if	
		return null;
	}//getEdge

	/**
	 * add a new node to the graph with the given node_data.
	 * Note: this method should run in O(1) time.
	 * @param n
	 */
	public void addNode(node_data n) {
		int key=n.getKey();
		if(n.getWeight()<0)
			throw new RuntimeException("ERR: Weight cannot be negative");
		if(Nodes.containsKey(key)) {
			//throw new RuntimeException("ERR: This node already exist");
			throw new RuntimeException("ERR:Cannot connect between unknown node");
		}
		Nodes.put(key,n);
		this.ModeCount++;
	}//addNode

	/**
	 * Connect an edge with weight w between node src to node dest.
	 * * Note: this method should run in O(1) time.
	 * @param src - the source of the edge.
	 * @param dest - the destination of the edge.
	 * @param w - positive weight representing the cost (aka time, price, etc) between src-->dest.
	 */
	public void connect(int src, int dest, double w) {
		//Wrong inputs
		if(src==dest)
			throw new RuntimeException("ERR: This isn't multy graph");
		if(w<=0)
			throw new RuntimeException("ERR: Weight cannot be 0 or negetive");

		//if the nodes don't exist
		if(!Nodes.containsKey(dest) || !Nodes.containsKey(src))
			throw new RuntimeException("ERR:Cannot connect between unknown node");

		EdgeData edge=new EdgeData(src,dest,w);

		//if the src exist in the Edge_Hash
		if(this.Edges.containsKey(src)) {
			//if we want to change an existing edge, remove it and add it later
			if(getEdge(src,dest)!=null){
				this.Edges.get(src).remove(dest);
			}//if

			this.Edges.get(src).put(dest, edge);
			this.ModeCount++;
		}//if

		else {//if the src doesn't exist in the Edge_Hash
			HashMap<Integer, edge_data> h=new HashMap<Integer,edge_data>();
			h.put(dest, edge);
			this.Edges.put(src, h);			
		}
		this.EdgeHashSize++;
		this.ModeCount++;
	}//connect

	/**
	 * This method return a pointer (shallow copy) for the
	 * collection representing all the nodes in the graph. 
	 * Note: this method should run in O(1) time.
	 * @return Collection<node_data>
	 */
	public Collection<node_data> getV() {
		return this.Nodes.values();
	}//getV

	/**
	 * This method return a pointer (shallow copy) for the
	 * collection representing all the edges getting out of 
	 * the given node (all the edges starting (source) at the given node). 
	 * Note: this method should run in O(1) time.
	 * @return Collection<edge_data>
	 */
	public Collection<edge_data> getE(int node_id) {
		Collection<edge_data> c=null;
		if(this.Edges.get(node_id)!=null)
			c=this.Edges.get(node_id).values();
		return c;
	}//getE

	/**
	 * Delete the node (with the given ID) from the graph -
	 * and removes all edges which starts or ends at this node.
	 * This method should run in O(n), |V|=n, as all the edges should be removed.
	 * @return the data of the removed node (null if none). 
	 * @param key
	 */
	public node_data removeNode(int key) {
		if(!this.Edges.isEmpty()) {
			//remove all the edges that key is the source:
			this.EdgeHashSize-=this.Edges.get(key).size();
			this.Edges.remove(key);

			//remove all the edges that key is the destination:
			Iterator<Integer> it = this.Edges.keySet().iterator();
			while (it.hasNext()) {
				Integer src = it.next();
				if(this.Edges.get(src).get(key)!=null) {
					this.Edges.get(src).remove(key);
					this.EdgeHashSize--;
				}//if
			}//while
		}//if
		this.ModeCount++;
		return this.Nodes.remove(key);
	}//removeNode


	/**
	 * Delete the edge from the graph, 
	 * Note: this method should run in O(1) time.
	 * @param src
	 * @param dest
	 * @return the data of the removed edge (null if none).
	 */
	public edge_data removeEdge(int src, int dest) {
		this.ModeCount++;
		this.EdgeHashSize--;
		return this.Edges.get(src).remove(dest);
	}//removeEdge

	/** return the number of vertices (nodes) in the graph.
	 * Note: this method should run in O(1) time. 
	 * @return
	 */
	public int nodeSize() {
		return Nodes.size();
	}//nodeSize

	/** 
	 * return the number of edges (assume directional graph).
	 * Note: this method should run in O(1) time.
	 * @return
	 */
	public int edgeSize() {
		return this.EdgeHashSize;
	}//edgeSize


	/**
	 * return the Mode Count - for testing changes in the graph.
	 * @return
	 */
	public int getMC() {
		return this.ModeCount;
	}//getMC

	/**
	 * Compare Graphs.
	 */
	@Override
	public boolean equals(Object a)
	{
		if(a instanceof DGraph)
		{
			if(((DGraph) a).get_Edge_Hash().equals(this.Edges) && ((DGraph) a).get_Node_Hash().equals(this.Nodes))
				return true;
			return false;
		}//if
		return false;
	}//equals
	
	@Override
	public String toString() {
		return "DGraph [Node_Hash=" + Nodes + "\nEdge_Hash=" + Edges + "\nModeCount=" + ModeCount
				+ "\nEdgeHashSize=" + EdgeHashSize + "]";
	}
}
