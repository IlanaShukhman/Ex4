package gameClient;

import org.json.JSONObject;

import dataStructure.edge_data;
import utils.Point3D;

/**
 * This class represent the same robot information that give us the server.
 * In this class we simply parsing information from the server in Json format and build Robot object
 * This class have the same variables and methods such as:
 * Constructors, Getters, Setters,init from Json, Equals ,toString
 * @author Michael & Ilana
 *
 */

public class Fruit_Client {
	private Point3D _pos;
	private double _value;
	private int _type;
	private edge_data edge;

	/**
	 * Constructors
	 */
	public Fruit_Client() {
	}

	public Fruit_Client(double v, Point3D p, int t) {
		this._value = v;
		this._pos = new Point3D(p);
		this._type = t;
	}

	/**
	 * Getters and setters
	 * @return
	 */
	public edge_data getEdge() {
		return edge;
	}

	public void setEdge(edge_data edge) {
		this.edge = edge;
	}

	public int getType() {
		return this._type;
	}

	public Point3D getLocation() {
		return this._pos;
	}

	public void set_pos(Point3D _pos) {
		this._pos = _pos;
	}

	public double getValue() {
		return this._value;
	}

	/**
	 * This method receives a string in the JSON format, and initializes the fruit from the string.
	 * @param json 
	 */
	public void initFromJson(String json)
	{
		try {
			JSONObject text = new JSONObject(json);
			//JSONArray fruits = text.getJSONArray("Fruit");

			this._value = text.getJSONObject("Fruit").getDouble("value");
			String pos= text.getJSONObject("Fruit").getString("pos");
			Point3D p = new Point3D(pos);
			this._pos=p;
			this._type=text.getJSONObject("Fruit").getInt("type");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}//initFromJson

	/**
	 * This method returns the String of a fruit.
	 */
	public String toString() {
		return toJSON();
	}//toString

	/**
	 * This method returns this fruit as a string in a JSON format
	 * @return
	 */
	public String toJSON() {
		String ans = "{\"Fruit\":{\"value\":" + this._value + "," + "\"type\":" + this._type + "," + "\"pos\":\"" + this._pos.toString() + "\"" + "}" + "}";
		return ans;
	}
	
	/**
	 * This function returns true if the fruits are the same - 
	 * equals by location.
	 * @param f is the other fruit
	 * @return true if this and f are on the same location
	 */
	public boolean equals(Fruit_Client f){
		if(f.getLocation().equals(this._pos))
			return true;
		return false;
	}


	public static void main(String[] a) {
		double v = 10.0D;
		Point3D p = new Point3D(1.0D, 2.0D, 3.0D);
		Fruit_Client f = new Fruit_Client(v, p, -1);
		String s = f.toJSON();
		System.out.println(s+"\n");
		f.initFromJson(s);
		System.out.println(f);
	}//main


}
