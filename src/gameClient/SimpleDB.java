package gameClient;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
/**
 * This class represents a simple example of using MySQL Data-Base.
 * Use this example for writing solution. 
 * @author boaz.benmoshe
 *
 */
public class SimpleDB {
	public static final String jdbcUrl="jdbc:mysql://db-mysql-ams3-67328-do-user-4468260-0.db.ondigitalocean.com:25060/oop?useUnicode=yes&characterEncoding=UTF-8&useSSL=false";
	public static final String jdbcUser="student";
	public static final String jdbcUserPassword="OOP2020student";
	public static HashMap<Integer, HashMap<Integer, Integer>> log;

	/**
	 * Simple main for demonstrating the use of the Data-base
	 * @param args
	 */
	public static void main(String[] args) {
		int id1 = 206333650;  
		int level = 0;
		//allUsers();
		printLog();
		String kml = getKML(id1,level);
		System.out.println("***** KML file example: ******");
		System.out.println(kml);
	}
	/** 
	 * simply prints all the games as played by the users (in the database).
	 */
	public static void printLog() {
		log = new HashMap<>();
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection connection = 
					DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcUserPassword);
			Statement statement = connection.createStatement();
			String allCustomersQuery = "SELECT * FROM Logs;";
			ResultSet resultSet = statement.executeQuery(allCustomersQuery);

			while(resultSet.next()){
				int id = resultSet.getInt("UserID");
				int level = resultSet.getInt("levelID");
				if(level < 0)
					continue;
				int moves = resultSet.getInt("moves");
				int score = resultSet.getInt("score");
				//System.out.println("Id: " + id +", level: "+ level +", moves: "+ moves +", score: "+score);


				int movesToPass;
				if(level == 0)
					movesToPass = 290;
				else if(level == 1) {
					movesToPass=580;
				}
				else if(level == 3) {
					movesToPass=580;
				}
				else if(level == 5) {
					movesToPass=500;
				}
				else if(level == 9) {
					movesToPass=580;
				}
				else if(level == 11) {
					movesToPass=580;
				}
				else if(level == 13) {
					movesToPass=580;
				}
				else if(level == 16) {
					movesToPass=290;
				}
				else if(level == 19) {
					movesToPass=580;
				}
				else if(level == 20) {
					movesToPass=290;
				}
				else if(level == 23) {
					movesToPass=1140;
				}
				else
					movesToPass=Integer.MAX_VALUE;


				//if the id doesn't exist
				if(log.isEmpty() || !log.containsKey(id)) {
					HashMap<Integer, Integer> levels = new HashMap<>();
					for(int i=0; i<24; i++) {
						levels.put(i, 0);
					}
					log.put(id, levels);		
				}
				
				//if the id exists in the HashMap
				else {
					int currentScore = log.get(id).get(level);
					if(score>currentScore && moves<= movesToPass) {
						log.get(id).replace(level, score);
					}
				}



			}
			resultSet.close();
			statement.close();		
			connection.close();		
		}

		catch (SQLException sqle) {
			System.out.println("SQLException: " + sqle.getMessage());
			System.out.println("Vendor Error: " + sqle.getErrorCode());
		}
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static HashMap<Integer, HashMap<Integer, Integer>> getLog(){
		printLog();
		return log;
	}
	/**
	 * this function returns the KML string as stored in the database (userID, level);
	 * @param id
	 * @param level
	 * @return
	 */
	public static String getKML(int id, int level) {
		String ans = null;
		String allCustomersQuery = "SELECT * FROM Users where userID="+id+";";
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection connection = 
					DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcUserPassword);		
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(allCustomersQuery);
			if(resultSet!=null && resultSet.next()) {
				ans = resultSet.getString("kml_"+level);
				System.out.println(ans);
			}
		}

		catch (SQLException sqle) {
			System.out.println("SQLException: " + sqle.getMessage());
			System.out.println("Vendor Error: " + sqle.getErrorCode());
		}

		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return ans;
	}


	public static int allUsers() {
		int ans = 0;
		String allCustomersQuery = "SELECT * FROM Users;";
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection connection = 
					DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcUserPassword);		
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(allCustomersQuery);
			while(resultSet.next()) {
				System.out.println("Id: " + resultSet.getInt("UserID") + ", max level: "+ resultSet.getInt("levelNum"));
				ans++;
			}
			resultSet.close();
			statement.close();		
			connection.close();
		}
		catch (SQLException sqle) {
			System.out.println("SQLException: " + sqle.getMessage());
			System.out.println("Vendor Error: " + sqle.getErrorCode());
		}

		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return ans;
	}

	public static void findUserName(){
		try {
			Class.forName("com.mysql.jdbc.Driver");

			Connection connection = 
					DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcUserPassword);		

			Statement stmt = connection.createStatement();
			int id = 206333650;
			String query = "SELECT * FROM Users where userID="+id+";";


			ResultSet rs = stmt.executeQuery(query);
			System.out.println("Names for query "+query+" are");

			while (rs.next()) {
				String name = rs.getString("userID");
				System.out.print(name+"  ");
			}
			System.out.println();

		}

		catch (SQLException sqle) {
			System.out.println("SQLException: " + sqle.getMessage());
			System.out.println("Vendor Error: " + sqle.getErrorCode());
			sqle.printStackTrace();
		}

		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}

