package gameClient;

import org.json.JSONObject;

/**
 * This class represent the same Game Server information that give us the server.
 * In this class we simply parsing information from the server in Json format and build Robot object
 * This class have the same variables and methods such as:
 * Constructors, Getters, Setters,init from Json ,toString
 * @author Michael & Ilana
 *
 */
public class GameServer_Client {

	private String _data;
	private int _fruits_number;
	private double _grade;
	private int _number_of_moves;
	private int _robots_number;
	/**
	 * Constructors:
	 */
	public GameServer_Client()
	{

	}
	public GameServer_Client(String _data, int _fruits_number, double _grade, int _number_of_moves, int _robots_number) {
		super();
		this._data = _data;
		this._fruits_number = _fruits_number;
		this._grade = _grade;
		this._number_of_moves = _number_of_moves;
		this._robots_number = _robots_number;
	}
	/**
	 * From Json format to this
	 * @param json
	 */
	public void initFromJson(String json)
	{
		try {
			JSONObject text = new JSONObject(json);
			this._data = text.getJSONObject("GameServer").getString("graph");
			this._fruits_number = text.getJSONObject("GameServer").getInt("fruits");
			this._grade = text.getJSONObject("GameServer").getDouble("grade");
			this._number_of_moves = text.getJSONObject("GameServer").getInt("moves");
			this._robots_number = text.getJSONObject("GameServer").getInt("robots");

		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}//initFromJson
/**
 * Getters & Setters:
 * @return
 */
	public String get_data() {
		return _data;
	}
	public void set_data(String _data) {
		this._data = _data;
	}
	public int get_fruits_number() {
		return _fruits_number;
	}
	public void set_fruits_number(int _fruits_number) {
		this._fruits_number = _fruits_number;
	}
	public double get_grade() {
		return _grade;
	}
	public void set_grade(double _grade) {
		this._grade = _grade;
	}
	public int get_number_of_moves() {
		return _number_of_moves;
	}
	public void set_number_of_moves(int _number_of_moves) {
		this._number_of_moves = _number_of_moves;
	}
	public int get_robots_number() {
		return _robots_number;
	}
	public void set_robots_number(int _robots_number) {
		this._robots_number = _robots_number;
	}
	/**
	 * Presentation in Json format:
	 * @return
	 */
	public String toJSON1() {
		String ans = "{\"GameServer\":{\"graph\":\"" + this._data + "\"," + "\"fruits\":" + this._fruits_number + "," + "\"grade\":" + this._grade + "," + "\"moves\":" + this._number_of_moves + "," + "\"robots\":" + this._robots_number + "}" + "}";
		return ans;
	}
	
	/**
	 * toString using Json format:
	 * @return
	 */
	public String toString()
	{
		return toJSON1();
	}
	public static void main(String[] a) {
		String sample="{\"GameServer\":{\"fruits\":3,\"moves\":0,\"grade\":0,\"robots\":1,\"graph\":\"data/A0\"}}";
		System.out.println(sample+"\n");
		GameServer_Client g=new GameServer_Client();
		g.initFromJson(sample);
		System.out.println(g);
	}

}
