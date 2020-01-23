
package gameClient;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import Server.robot;
import dataStructure.edge_data;
import dataStructure.node_data;
import utils.Point3D;

/**
 * This class represent the same robot information that gives us the server.
 * In this class we simply parsing information from the server in Json format and build Robot object
 * This class have the same variables and methods such as:
 * Constructors, Getters, Setters,init from Json, Equals ,toString
 * @author Michael & Ilana
 *
 */
public class Robot_Client {
	private int _id;
	private Point3D _pos;
	private double _value;
	private int _src;
	private int _dest;
	private double _speed;
	private Fruit_Client target;
	private double pathLength;
	private List<node_data> path;
	/**
	 * Constructors:
	 */
	public Robot_Client(){
		this.pathLength=0;
		this.path=new ArrayList<node_data>();
	}

	

	public Robot_Client(Point3D _pos, double _value, int _src, int _dest, double _speed) {
		super();
		this._pos = _pos;
		this._value = _value;
		this._src = _src;
		this._dest = _dest;
		this._speed = _speed;
	}
	/**
	 * Updating the necessary things
	 * @param is
	 * @param pos
	 * @param value
	 * @param speed
	 */
	public void update(Point3D pos,double value,double speed,int src)
	{
		this._src=src;
		this._pos=pos;
		this._value=value;
		this._speed=speed;
	}//update
	/**
	 * Parsing all the information from json
	 * @param json
	 */
	public void initFromJson(String json)
	{
		try {
			JSONObject text = new JSONObject(json);
			this._id = text.getJSONObject("Robot").getInt("id");
			this._src = text.getJSONObject("Robot").getInt("src");
			this._dest = text.getJSONObject("Robot").getInt("dest");
			String pos= text.getJSONObject("Robot").getString("pos");
			this._pos= new Point3D(pos);
			this._speed = text.getJSONObject("Robot").getDouble("speed");
			this._value = text.getJSONObject("Robot").getDouble("value");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}//initFromJson



	/**
	 * Getters & Setters:
	 * @return
	 */
	


	public double getPathLength() {
		return pathLength;
	}

	public List<node_data> getPath() {
		return path;
	}



	public void setPath(List<node_data> path) {
		this.path.clear();
		this.path.addAll(path);
	}



	public void setPathLength(double pathLength) {
		this.pathLength = pathLength;
	}
	public int get_id() {
		return _id;
	}

	public Fruit_Client getTarget() {
		return target;
	}


	public void set_id(int _id) {
		this._id = _id;
	}




	public Point3D get_pos() {
		return _pos;
	}


	public void set_pos(Point3D _pos) {
		this._pos = _pos;
	}
	
	public void setTarget(Fruit_Client target) {
		this.target = target;
	}


	/**
	 * Parsing from String to Point3D Object format
	 * @param _pos
	 */
	public void set_pos(String _pos) {
		int count=0;
		String _x = "";
		String _y = "";
		
		for (int i = 0; i < _pos.length(); i++) {
			if(_pos.charAt(i)==',' && count==0) {
				_x = _pos.substring(0,i-1);
				count=i;
			}
			else if(_pos.charAt(i)==',' && count!=0) {
				_y=_pos.substring(count+1, i-1);
			}
		}
		double x=Double.parseDouble(_x);
		double y=Double.parseDouble(_y);
		Point3D p=new Point3D(x,y);
		
		this._pos=p;
	}

	public double get_value() {
		return _value;
	}




	public void set_value(double _value) {
		this._value = _value;
	}




	public int get_src() {
		return _src;
	}




	public void set_src(int _src) {
		this._src = _src;
	}




	public int get_dest() {
		return _dest;
	}


	public void set_dest(int _dest) {
		this._dest = _dest;
	}

	public double get_speed() {
		return _speed;
	}


	public void set_speed(double _speed) {
		this._speed = _speed;
	}
	
	/**
	 * Parsing to Json format
	 * @return
	 */
	public String toJSON() {
		String ans = "{\"Robot\":{\"id\":" + this._id + "," + "\"value\":" + this._value + "," + "\"src\":" + this._src + "," + "\"dest\":" + this._dest + "," + "\"speed\":" + this._speed + "," + "\"pos\":\"" + this._pos.toString() + "\"" + "}" + "}";
		return ans;
	}
	/**
	 * toString in Json Format
	 * @return
	 */
	@Override
	public String toString() {
		return toJSON();
	}
	/**
	 * Equals Robots by their Id
	 * We assume that they have different id's
	 * @param r
	 * @return
	 */
	public boolean equals(robot r){
		return (r.getID()==this._id);
	}//equals
	/**
	 * toString in Json Format
	 * @return
	 */
	public static void main(String[] a) {
		String s1="{\"Robot\":{\"id\":0,\"value\":0.0,\"src\":0,\"dest\":-1,\"speed\":1.0,\"pos\":\"35.18753053591606,32.10378225882353,0.0\"}}";
		System.out.println(s1+"\n");
		Robot_Client r=new Robot_Client();
		r.initFromJson(s1);
		System.out.println(r);
	}
	
}