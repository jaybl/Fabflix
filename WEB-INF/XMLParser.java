import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.*;
import java.net.*;
import java.sql.*;
import java.text.*;
import java.util.*;

public class XMLParser {
	ArrayList<Movie> movieList;
	ArrayList<Star> starList;
	ArrayList<StarInMovie> starInMovieList;
	Document dom;

	public XMLParser(){
		movieList = new ArrayList<Movie>();//list to hold movie objects
		starList = new ArrayList<Star>();
		starInMovieList = new ArrayList<StarInMovie>();
	}

	public void parseAllData() {
		parseXmlFile("mains243.xml");//parse xml and get dom object
		parseMovieDocument();//get each movie element and create an object for it
		//printData(movieList);
		parseXmlFile("actors63.xml");
		parseStarDocument();
		//printData(starList);
		parseXmlFile("casts124.xml");
		parseCastDocument();
		//printData(starInMovieList);
	}
	
	private void parseXmlFile(String XmlFile){
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try{
			DocumentBuilder db = dbf.newDocumentBuilder();
			dom = db.parse(XmlFile);
		}catch(ParserConfigurationException pce) {
			pce.printStackTrace();
		}catch(SAXException se) {
			se.printStackTrace();
		}catch(IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	private void parseMovieDocument(){
		Element docEle = dom.getDocumentElement();//get root element
		NodeList df = docEle.getElementsByTagName("directorfilms");//node list of <directorfilms> elements
		if(df != null && df.getLength() > 0) {
			for(int i = 0; i < df.getLength(); i++){
				NodeList d = ((Element)df.item(i)).getElementsByTagName("director");
				String director = null;
				if(d != null && d.getLength() > 0)
					director = getTextValue((Element)d.item(0), "dirname");
				if(director == null)
					director = "unknown";
				NodeList films = ((Element)df.item(i)).getElementsByTagName("films");
				if(films == null || films.getLength() == 0)
					continue;
				NodeList film = ((Element)films.item(0)).getElementsByTagName("film");
				if(film == null || film.getLength() == 0)
					continue;
				for(int j = 0; j < film.getLength(); j++){
					addMovie(director, (Element)film.item(j));
				}
			}
		}
	}

	private void parseStarDocument(){
		Element docEle = dom.getDocumentElement();//get root element
		NodeList df = docEle.getElementsByTagName("actor");//node list of <directorfilms> elements
		if(df != null && df.getLength() > 0) {
			for(int i = 0; i < df.getLength(); i++){
				Star s = getStar((Element)df.item(i));
				starList.add(s);
			}
		}
	}

	private void parseCastDocument(){
		Element docEle = dom.getDocumentElement();//get root element
		NodeList df = docEle.getElementsByTagName("dirfilms");//node list of <directorfilms> elements
		if(df != null && df.getLength() > 0) {
			for(int i = 0; i < df.getLength(); i++){
				NodeList filmc = ((Element)df.item(i)).getElementsByTagName("filmc");
				for(int j = 0; j < filmc.getLength(); j++){
					NodeList m = ((Element)filmc.item(j)).getElementsByTagName("m");
					for(int k = 0; k < m.getLength(); k++){
						StarInMovie sim = getStarInMovie((Element)m.item(k));
						starInMovieList.add(sim);
					}
				}
			}
		}
	}
	private void addMovie(String dir, Element movEl) {
		String id = getTextValue(movEl, "fid");
		String title = getTextValue(movEl, "t");
		if(title == null)
			return;
		title = title.replace("'","");
		title = title.replace("\\","");
		int year = getIntValue(movEl, "year");
		NodeList cats = movEl.getElementsByTagName("cats");
		String genre = getTextValue((Element)cats.item(0), "cat");
		if(genre == null)
			return;
		dir = dir.replace("'","");
		genre = genre.replace("'","");
		Movie m = new Movie(id, title, year, dir, genre);
		movieList.add(m);
	}

	private Star getStar(Element starEl) {
		String stagename = getTextValue(starEl, "stagename");
		String[] names = stagename.split(" ");
		String first = names[0];
		String last = names[names.length - 1];
		int year = getIntValue(starEl, "dob");
		first = first.replace("'","");
		first = first.replace("''","");
		first = first.replace("\\","");
		last = last.replace("'","");
		Star s = new Star(stagename, first, last, year);
		return s;
	}

	private StarInMovie getStarInMovie(Element simEl) {
		String fid = getTextValue(simEl, "f");
		String title = getTextValue(simEl, "t");
		String actor = getTextValue(simEl, "a");
		title = title.replace("'","");
		StarInMovie sim = new StarInMovie(fid, title, actor);
		return sim;
	}

	private String getTextValue(Element ele, String tagName){
		if(ele == null)
			return null;
		String textVal = null;
		NodeList nl = ele.getElementsByTagName(tagName);
		if(nl != null && nl.getLength() > 0) {
			Element el = (Element)nl.item(0);
			if(el != null)
				try{
					textVal = el.getFirstChild().getNodeValue();
				}catch(Exception e){
					System.out.println("A value was not found for tag "+tagName+". Replaced with NULL.");
				}
		}
		return textVal;
	}

	private int getIntValue(Element ele, String tagName) {
		String text = getTextValue(ele, tagName);
		if(text == null)
			return 0;
		try{
			return Integer.parseInt(text);
		}catch(Exception e){
			//System.out.println(text + " found in place of integer for tag "+tagName+". Replacing with 0.");
			System.out.println("Error converting value to Int.");
			return 0;
		}
	}
	
	private void printData(ArrayList l){
		Iterator it = l.iterator();
		while(it.hasNext()) {
			System.out.println(it.next().toString());
		}
	}
	
	public void insert(){
		try{
              		Class.forName("com.mysql.jdbc.Driver").newInstance();
        		String loginUrl = "jdbc:mysql:///moviedb?autoReconnect=true&useSSL=false&useServerPrepStmts=false&rewriteBatchedStatements=true";
             		Connection dbcon = DriverManager.getConnection(loginUrl, "root", "rootPassWord");
			dbcon.setAutoCommit(false);
			insertMoviesAndGenres(dbcon);
			insertStars(dbcon);
			insertStarsInMovies(dbcon);
			dbcon.close();
        	}catch (SQLException ex) {
              		while (ex != null) {
                    		System.out.println ("SQL Exception:  " + ex.getMessage ());
                    		ex = ex.getNextException ();
                	}  // end while
            	}  // end catch SQLException
		catch(Exception e){
			e.printStackTrace();
		}
	}

	public void insertMoviesAndGenres(Connection dbcon) throws SQLException{
              	PreparedStatement psm = dbcon.prepareStatement("insert into movies(title, year, director) VALUES(?,?,?)");
              	PreparedStatement psg = dbcon.prepareStatement("insert into genres(name) VALUES(?)");
		Set<String> gSet = new HashSet<String>();
		for(int i = 0; i < movieList.size(); i++){
			psm.setString(1,movieList.get(i).getTitle());                 
			psm.setInt(2,movieList.get(i).getYear());
			String director = movieList.get(i).getDirector();
			psm.setString(3,director);
			psm.addBatch();
			String genre = movieList.get(i).getGenre();
			if(!gSet.contains(genre)){
				gSet.add(genre);
				psg.setString(1,genre);                 
				psg.addBatch();
			}
		}
		psm.executeBatch();
		dbcon.commit();
		psg.executeBatch();
		dbcon.commit();
		psm.close();
		psg.close();
              	PreparedStatement ps = dbcon.prepareStatement("insert into genres_in_movies(genre_id, movie_id) VALUES(?,?)");
		Statement statement = dbcon.createStatement();
		for(int i = 0; i < movieList.size(); i++){
			String movieID = null;
			String genreID = null;
			String movieQuery = "select * from movies where title = '" + movieList.get(i).getTitle() + "'";
			String genreQuery = "select * from genres where name = '" + movieList.get(i).getGenre() + "'";
			ResultSet movieSet = statement.executeQuery(movieQuery);
			while(movieSet.next()){
				movieID = movieSet.getString("id");
			}
			movieSet.close();

			ResultSet genreSet = statement.executeQuery(genreQuery);
			while(genreSet.next()){
				genreID = genreSet.getString("id");
			}
			genreSet.close();
			if(movieID != null && genreID != null){
				ps.setInt(1,Integer.parseInt(genreID));
				ps.setInt(2,Integer.parseInt(movieID));
				ps.addBatch();
			}
		}
		ps.executeBatch();
		dbcon.commit();
		ps.close();
		statement.close();
	}

	public void insertStars(Connection dbcon) throws SQLException{
		PreparedStatement ps = dbcon.prepareStatement("insert into stars(first_name, last_name, dob) VALUES(?,?,?)");
		for(int i = 0; i < starList.size(); i++){
			String first = starList.get(i).getFirstName();
			String last = starList.get(i).getLastName();
			ps.setString(1,first);                 
			ps.setString(2,last);
			ps.setTimestamp(3,new java.sql.Timestamp(starList.get(i).getDOB()));
			ps.addBatch();
		}
		ps.executeBatch();
		dbcon.commit();
		ps.close();
	}

	public void insertStarsInMovies(Connection dbcon) throws SQLException{
              	PreparedStatement ps = dbcon.prepareStatement("insert into stars_in_movies(star_id, movie_id) VALUES(?, ?)");
		Statement statement = dbcon.createStatement();
		int count = 0;
		int max = 1000;
		for(int i = 0; i < starInMovieList.size(); i++){
			String movieID = null;
			String starID = null;
			String movieQuery = "select id from movies where title = '" + starInMovieList.get(i).getTitle() + "'";
			String stagename = starInMovieList.get(i).getActor();
			if(stagename == null)
				continue;
			String[] names = stagename.split(" ");
			String first = names[0];
			String last = names[names.length-1];
			if(first != null && last != null){
				first = first.replace("'","");
				first = first.replace("''","");
				first = first.replace("\\","");
				last = last.replace("'","");
				last = last.replace("\\","");
			}
			String starQuery = "select id from stars where first_name = '" + first + "' and last_name ='" + last + "'";
			ResultSet movieSet = statement.executeQuery(movieQuery);
			while(movieSet.next()){
				movieID = movieSet.getString("id");
			}
			movieSet.close();
			ResultSet starSet = statement.executeQuery(starQuery);
			while(starSet.next()){
				starID = starSet.getString("id");
			}
			starSet.close();
			if(movieID != null && starID != null){
				ps.setInt(1,Integer.parseInt(starID));
				ps.setInt(2,Integer.parseInt(movieID));
				ps.addBatch();
			}
			count++;
			if(count > max){
				ps.executeBatch();
				dbcon.commit();
				count = 0;
			}
		}
		if(count > 0){
			ps.executeBatch();
			dbcon.commit();
		}
		ps.close();
		statement.close();
	}

	public static void main(String[] args){
		XMLParser xmlp = new XMLParser();
		xmlp.parseAllData();
		xmlp.insert();
	}
}
