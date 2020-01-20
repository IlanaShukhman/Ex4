package dataStructure;

import java.io.Serializable;

public class EdgeData implements edge_data,Serializable{
	private int SRC;
	private int DST;
	private double WEIGHT;
	private String INFO;
	private int TAG;
	
	
	/**
	 * Constructors:
	 * @param src - SRC node
	 * @param dst - Dest node
	 * @param weight - Length fron src to dst
	 * @param info - Algorithms indicator
	 * @param tag - Algorithms indicator
	 */
	public EdgeData(int src, int dst, double weight, String info, int tag) {
		if(src==dst)
			throw new RuntimeException("ERR: This isn't multy graph");
		SRC = src;
		DST = dst;
		WEIGHT = weight;
		INFO = info;
		TAG = tag;
		
	}
	
	/**
	 * Constructor:
	 * @param src - SRC node
	 * @param dst - Dest node
	 * @param weight - Length fron src to dst
	 */
	public EdgeData(int src, int dst, double weight) {
		super();
		SRC = src;
		DST = dst;
		WEIGHT = weight;
	}

	@Override
	public int getSrc() {
		return this.SRC;
	}

	@Override
	public int getDest() {
		return this.DST;
	}

	@Override
	public double getWeight() {
		return this.WEIGHT;
	}

	@Override
	public String getInfo() {
		return this.INFO;
	}

	@Override
	public void setInfo(String s) {
		this.INFO=s;
		
	}

	@Override
	public int getTag() {
	    return this.TAG;
	}

	@Override
	public void setTag(int t) {
		this.TAG=t;
		
	}
	@Override
	public String toString() {
		String str="";
		str="Source: "+SRC+", Destination: "+DST+", Weight: "+WEIGHT;
		return str;
	}
	
	
	/**
	 * Compare Edges
	 */
	@Override
	public boolean equals(Object edge)
	{
		if(edge instanceof edge_data)
		{
			if(((edge_data) edge).getDest()==this.DST && 
					((edge_data) edge).getInfo()==this.INFO &&
					((edge_data) edge).getSrc()==this.SRC &&
					((edge_data) edge).getTag()==this.TAG &&
					((edge_data) edge).getWeight()==this.WEIGHT)
				return true;
			return false;
		}//if
		else
			return false;
	}//equals
	

}
