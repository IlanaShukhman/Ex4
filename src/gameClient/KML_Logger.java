
package gameClient;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.List;

import Server.game_service;
import dataStructure.DGraph;


/**
 * The purpose of this class is to make a KML file of a running game. 
 * It will run alongside with the game, and save the robots and fruits' coordinates every second. 
 * @author Ilana & Michael
 *
 */

public class KML_Logger implements Runnable {

	private int level;
	private DGraph graph;
	private List<Robot_Client> robots;
	private List<Fruit_Client> fruits;
	private game_service game;
	private int timeOfGame;
	private String kmlfile;

	/**
	 * Constructor.
	 */
	public KML_Logger(int level, DGraph graph, List<Robot_Client> robots, List<Fruit_Client> fruits , game_service game) {
		this.graph=new DGraph(graph);
		this.robots=robots;
		this.fruits=fruits;
		this.game=game;
		this.level=level;
		if(game.timeToEnd()>30000)
			this.timeOfGame=60000;
		else
			this.timeOfGame=30000;

		kmlfile="";


		Thread thr=new Thread(this);
		thr.start();



	}

	/**
	 * This method creates the content of the kml file, and sends it to the saveToFile function.
	 * @return
	 */

	private String createKMLfile() {
		String file=
				"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" + 
						"<kml xmlns=\"http://www.opengis.net/kml/2.2\" xmlns:gx=\"http://www.google.com/kml/ext/2.2\" xmlns:kml=\"http://www.opengis.net/kml/2.2\" xmlns:atom=\"http://www.w3.org/2005/Atom\">\r\n"
						+ "<Document>\n" + 
						"<name>"+ this.level + ".kml</name>\r\n";

		file+=addPoints();

		while(game.isRunning()) {
			file+=updateFruits();
			file+=updateRobots();
		}

		file+=	"<Style id=\"orange-5px\">" +
				"<LineStyle>" + 
				//color
				"<color>ff00aaff</color>"
				//width
				+ "<width>2</width>"+ 
				"</LineStyle>" +
				"</Style>" + 


				"<Placemark>\r\n" + 
				"<name>"+ this.level +"</name>\r\n" + 

				"<styleUrl>#orange-5px</styleUrl>\r\n" + 
				"<LineString>\r\n" + 
				"<tessellate>1</tessellate>\r\n" + 
				"<coordinates>\n";
		for( Integer v : graph.get_Edge_Hash().keySet() ) {
			for ( Integer u : graph.get_Edge_Hash().get(v).keySet() ) {		
				file+=graph.get_Node_Hash().get(v).getLocation();
				file+="\r\n";
				file+=graph.get_Node_Hash().get(u).getLocation();
				file+="\r\n";
			}
		}

		file+="</coordinates>\r\n" +  
				"</LineString>\r\n" + 
				"</Placemark>\r\n" +  
				"</Document>\r\n" + 
				"</kml>\r\n";

		kmlfile=file;


		try {
			saveToFile(file);
		} catch (Exception e) {
			e.printStackTrace();
		}


		return file;
	}

	/**
	 * @return the content of the KML file.
	 */
	public String getKMLFile() {
		return kmlfile;
	}

	/**
	 * This method returns the position of the fruits each second.
	 * If the fruit's type is -1, it will show a mountain. If the fruit's type is 1, it will show a boat.
	 * This function is called every second. 
	 * @return String str which contains the coordinates of the fruits. 
	 */
	private String updateFruits() {
		String str="";
		long time=(timeOfGame - game.timeToEnd())/100;



		for(Fruit_Client fruit: fruits) {
			str+=	"<Placemark>\r\n"+

					"<TimeSpan>\r\n"+
					"<begin>"+LocalDateTime.now()+"</begin>\r\n" + 
					"<end>" + (LocalDateTime.now()) + "</end>\r\n"+
					"</TimeSpan>\r\n"+

					"<Style id=\"mycustommarker\">\r\n" + 
					"<IconStyle>\r\n" + 
					"<Icon>\r\n" ; 

			if(fruit.getType()==-1) {
				str+=	"<href>http://maps.google.com/mapfiles/kml/shapes/mountains.png</href>";
			}
			else
				str+=	"<href>http://maps.google.com/mapfiles/kml/shapes/sailing.png</href>";

			str+=	"</Icon>\r\n" + 
					"</IconStyle>\r\n" + 
					"</Style>\r\n"+
					"<Point>\r\n"+  
					"<coordinates>"+ fruit.getLocation() + "</coordinates>\r\n"+  
					"</Point>\r\n"+
					"</Placemark>\r\n";
		}
		return str;
	}

	/**
	 * This method returns the position of the robots each second.
	 * This function is called every second. 
	 * @return String str which contains the coordinates of the robots. 
	 */
	private String updateRobots() {

		String str="";
		long time=(timeOfGame - game.timeToEnd())/100;

		String[] color= {"ff0000ff","ffff0000","ff800080","ff00ffff","ffff00ff"};
		int i=0;

		for(Robot_Client robot: robots) {
			str+=	"<Placemark>\r\n"+

					"<TimeSpan>\r\n"+
					"<begin>"+LocalDateTime.now()+"</begin>\r\n" + 
					"<end>" + LocalDateTime.now() + "</end>\r\n"+
					"</TimeSpan>\r\n"+

					"<color>"+color[i]+"</color>"+

					"<Style id=\"mycustommarker\">\r\n" + 
					"<IconStyle>\r\n" + 
					"<Icon>\r\n" + 
					"<href>http://maps.google.com/mapfiles/ms/icons/hiker.png</href>" + 
					"</Icon>\r\n" + 
					"</IconStyle>\r\n" + 
					"</Style>\r\n"+
					"<Point>\r\n"+  
					"<coordinates>"+ robot.get_pos() + "</coordinates>\r\n"+  
					"</Point>\r\n"+
					"</Placemark>\r\n";

			i++;
		}
		return str;
	}

	/**
	 * this method adds the vertices in the graph to the kml file.
	 * this function is called only once.
	 * @return the position to the vertices in a kml format.
	 */
	private String addPoints() {
		String str="";
		for(Integer node : graph.get_Node_Hash().keySet()) {
			str+=	"<Placemark>\r\n"+
					"<color>ff00aaff</color>"+
					"<Point>\r\n"+  
					"<coordinates>"+ graph.get_Node_Hash().get(node).getLocation() + "</coordinates>\r\n"+  
					"</Point>\r\n"+
					"</Placemark>\r\n";
		}//for


		return str;
	}//addPoints
	/**
	 * This method receives a text in kml format, and saves it as a file.
	 * The file's name is the level and a .kml
	 * @param file
	 * @throws IOException
	 */
	private void saveToFile(String file) throws IOException {
		try {
			File f=new File(this.level+".kml");
			PrintWriter print=new PrintWriter(f);

			print.write(file);
			print.close();


		} catch (Exception e) {
			System.out.println("ERR: failing to save this list.");
		}
	}//SaveToFile

	@Override
	public void run() {
		createKMLfile();
	}

}
