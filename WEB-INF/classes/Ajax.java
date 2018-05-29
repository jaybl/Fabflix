import java.io.*;
import java.net.*;
import java.sql.*;
import java.text.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class Ajax extends HttpServlet
{
	@Override
	 public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
	{
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		String title = request.getParameter("val");
		String[] array  = title.split("\\s+");
		String[] stopwords = {"a", "able", "about",
        "across", "after", "all", "almost", "also", "am", "among", "an",
        "and", "any", "are", "as", "at", "b", "be", "because", "been",
        "but", "by", "c", "can", "cannot", "could", "d", "dear", "did",
        "do", "does", "e", "either", "else", "ever", "every", "f", "for",
        "from", "g", "get", "got", "h", "had", "has", "have", "he", "her",
        "hers", "him", "his", "how", "however", "i", "if", "in", "into",
        "is", "it", "its", "j", "just", "k", "l", "least", "let", "like",
        "likely", "m", "may", "me", "might", "most", "must", "my",
        "neither", "n", "no", "nor", "not", "o", "of", "off", "often",
        "on", "only", "or", "other", "our", "own", "p", "q", "r", "rather",
        "s", "said", "say", "says", "she", "should", "since", "so", "some",
        "t", "than", "that", "the", "their", "them", "then", "there",
        "these", "they", "this", "tis", "to", "too", "twas", "u", "us",
        "v", "w", "wants", "was", "we", "were", "what", "when", "where",
        "which", "while", "who", "whom", "why", "will", "with", "would",
        "x", "y", "yet", "you", "your", "z"};
		String addMe = "";
		for (int i = 0; i < array.length; i++){
			String token = array[i];
			if (token.length() != 0){
				 if ((Arrays.asList(stopwords).contains(token) )			)	{
					if (i == array.length-1)
					{
						addMe += token;
					}
					else
						continue;
				}
				else{
					addMe += token;
					if (i < array.length-1 && array.length != 1)
					{
						addMe += "\')";
						addMe += " and match(title) against (\'";
					}
					
				}
				
		}
		}
		title = addMe;
		String url;
		//JSONObject json = new JSONObject();
		String loginUrl = "jdbc:mysql:///moviedb?autoReconnect=true&useSSL=false";
		String loginUser = "root";
	        String loginPasswd = "rootPassWord";
		//NEED TO FIX THIS. TOKENIZE title INTO KEYWORDS AND MATCH ALL. SEE WRITEUP.
		String query = "select distinct title, id from movies where match(title) against ( \'" + addMe + "* \'in boolean mode) group by(title);";
		//out.println(query);
		if (title==null || title.trim().equals("")){
		out.println("");
		return;
		}
		try{              		
			Class.forName("com.mysql.jdbc.Driver").newInstance();		              Connection dbcon = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
	              Statement statement = dbcon.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			ResultSet rs = statement.executeQuery(query);		
			//Limit to only a certain number??? rn it prints a ton
			int count = 0;
			while (rs.next() && count < 10)
			{
				String t = rs.getString("title");
				String id = rs.getString("id");
				//json.put(t, 0);
				out.println("<a href = \"/Fabflix/movie?movieID=" + id + "\">" + t + "</a><br>");
				count++;
			}
			//out.println(json.toString());
			dbcon.close();
		}
		catch (Exception e){
			out.println(e);
		}
	}
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
		return;
    }
}

