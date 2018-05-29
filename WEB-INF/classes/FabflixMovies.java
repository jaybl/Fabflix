/* A servlet to display the contents of the MySQL movieDB database */

import java.io.*;
import java.net.*;
import java.sql.*;
import java.text.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class FabflixMovies extends HttpServlet
{
    public String getServletInfo()
    {
       return "Servlet connects to MySQL database and displays result of a SELECT";
    }

    // Use http GET

    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException
    {
        String loginUser = "root";
        String loginPasswd = "rootPassWord";
        String loginUrl = "jdbc:mysql:///moviedb?autoReconnect=true&useSSL=false";

        response.setContentType("text/html");    // Response mime type

        // Output stream to STDOUT
        PrintWriter out = response.getWriter();

        out.println("<HTML><HEAD><TITLE>Fabflix Movies: Found Records</TITLE></HEAD>");
	out.println("<script src=\"https://ajax.googleapis.com/ajax/libs/jquery/3.2.0/jquery.min.js\"></script>  <script src=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js\"></script><!-- Latest compiled and minified CSS --><link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css\" integrity=\"sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u\" crossorigin=\"anonymous\"><!-- Optional theme --><link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap-theme.min.css\" integrity=\"sha384-rHyoN1iRsVXV4nD0JutlnGaslCJuC7uwjduW9SVrLvRYooPp2bWYgmgJQIXwl/Sp\" crossorigin=\"anonymous\"><!-- Latest compiled and minified JavaScript --><script src=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js\" integrity=\"sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa\" crossorigin=\"anonymous\"></script>");
        out.println("<style>");
	out.println("form{ height: auto; overflow: visible; }");
	out.println("html{font-family: Arial;}");
	out.println(".movieInfo{ text-align: center; vertical-align: middle; }");

	out.println(".btn-search{ background-color: transparent; float: right; font-weight: bold; color: white; border-radius: 3px; }");
	out.println("</style>");
        out.println("</style>");
	out.println("<script>");

	out.println("$(function() {  $('#shoppingForm').submit(function() {        $.ajax({            type: 'GET',            url: '/Fabflix/items'        });        return false;    }); })");

	out.println("</script>");
        out.println("<BODY>");
	out.println("<div style=\"position: relative; top: 0; height: 45px; width: 100%; text-align: right; border-bottom: 1px solid #007EE5; background-color: #4f6b9b; padding: 10px; padding-right: 20px; box-shadow: 0 2px 26px 0 rgba(0,0,0,0.18);\">");
	out.println("<form action=\"/Fabflix/main\" method=\"post\"><button type=submit style=\"position: absolute; bottom: 10%; right: 29%;\" class=\"btn btn-search\">FABFLIX HOME</button></form>");
	out.println("<a href=\"/Fabflix/items?movieID=&quantity=0\"> <button style=\"position: absolute; right: 11%; bottom: 10%;\" class=\"btn btn-search\">Shopping Cart</button></a>");
	out.println("<form action=\"/Fabflix/logout\" method=\"get\"><button type=submit style=\"position: absolute; right: 1%; bottom: 10%;\" class=\"btn btn-search\">Logout</button></form>");
	out.println("</div>");
//	out.println("<H1 id=\"changeMe\">Fabflix Movies: Found Records</H1><div class=\"row\">");
        out.println("<script> function clickAlert(){ alert(\"Processing...\");} </script>");
	try
	{
		//Class.forName("org.gjt.mm.mysql.Driver");
		Class.forName("com.mysql.jdbc.Driver").newInstance();

		Connection dbcon = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
		// Declare our statement
		Statement statement = dbcon.createStatement();

		String movieID = request.getParameter("movieID");
		String query = "SELECT * from movies where movies.id = " + movieID;
		String stars = "select * from stars, stars_in_movies, movies where stars.id = star_id" + 
				" and movie_id = movies.id and  movies.id = " + movieID;
		String genres = "select * from genres, genres_in_movies, movies where " +
				"genres.id = genre_id and movie_id = movies.id and movies.id = " + movieID; 
		String formStar = "<FORM ACTION=\"/Fabflix/star\" METHOD=\"GET\">";
	        String formGenre = "<FORM ACTION=\"/Fabflix/search\" METHOD=\"GET\">";
		String formItems = "<FORM  ACTION=\"/Fabflix/items\" METHOD=\"GET\" >";
		// Perform the query
		ResultSet rs = statement.executeQuery(query);
		String style = "style=\"border: none; background: transparent; text-decoration: underline; color: #007EE5; height: auto; overflow: visible; margin: 0 auto;\"";
		//list movie info
		while(rs.next())
		{
		  String m_ID = rs.getString("ID");
		  String m_T = rs.getString("title");
       		 out.println("<h1 style=\"text-align: center;\">" + m_T + "</h1><br>");
	out.println("<div class=\"col-xs-6\" style = \"position: relative;\"> ");
		  String m_Y = rs.getString("year");
		  String m_D = rs.getString("director");
		  String imageurl = rs.getString("banner_url");
		  String trailerurl = rs.getString("trailer_url");
		  out.println("<img src=\"" + imageurl + "\" alt=\"" + m_T +"\" style=" +
			      "\"position:absolute;width:333px;height:500px;vertical-align: middle; margin-left: 10%;\"><br>");		  
		
		  out.println("</div><div class=\"col-xs-6 movieInfo\">");
		  out.println("<p style=\"margin-top: 126px;\">" +
			  "<b>ID:</b> " + m_ID +
			  "<br> <b>Title:</b> " + m_T +
			  "<br> <b>Year:</b> " + m_Y +
			  "<br> <b>Director:</b> " + m_D + "</p>");
		  rs.close();
		  //list genres for movie
		  ResultSet genreSet = statement.executeQuery(genres);
		  out.println("<p style=\"\"> <b>Genres:</b> ");
		  while(genreSet.next())
		  {
		    String m_G = genreSet.getString("name");
		    out.println(formGenre + "<button type=\"SUBMIT\" " + style + "value=\"" + m_G + 
				"\" name=\"genre\">" + m_G + "</button></form>");
		  }
		  out.println("</p>");
		  genreSet.close();
		  //list actor for movie
		  ResultSet actorSet = statement.executeQuery(stars);
		  out.println("<p><b> Stars:</b> ");
		  while(actorSet.next())
		  {
		    String m_S = actorSet.getString("first_name") + " " + actorSet.getString("last_name");
		    out.println(formStar + "<button type=\"SUBMIT\" " + style + "value=\"" + actorSet.getString("star_id") + 
				"\" name=\"starID\">" + m_S + "</button></form>");
		  }  
		  out.println("</p>");
		  actorSet.close();
		  out.println("<a href=" + trailerurl + ">Trailer Link</a><br><br>");
		  out.println(formItems + "<button type=\"SUBMIT\" onclick=\"clickAlert()\" value=\"" + m_ID + "\" name=\"movieID\" style=\"font-weight: bold; background-color: transparent; border: 1px solid #007EE5; padding: 10px; padding-left: 15px; padding-right: 15px; border-radius: 3px; margin-left: 10px;\">Add to Shopping Cart</button>");
		  out.println("<INPUT  type\"TEXT\" value=\"\" name=\"quantity\" style=\"width: 40px; height: 40px; margin-left: 5px; padding-left: 5px;\"></form><br>");
		  out.println("</div></div>");
		  out.println("Add 0 to remove all copies of this movie from your Shopping Cart.");
		}   
		statement.close();
		dbcon.close();
	}
        catch(SQLException ex){
		while (ex != null){
                    System.out.println ("SQL Exception:  " + ex.getMessage ());
                    ex = ex.getNextException ();
                }  // end while
            }  // end catch SQLException

        catch(java.lang.Exception ex)
        {
		out.println("<HTML>" +
                            "<HEAD><TITLE>" +
                            "MovieDB: Error" +
                            "</TITLE></HEAD>\n<BODY>" +
                            "<P>SQL error in doGet: " +
                            ex.getMessage() + "</P></BODY></HTML>");
                return;
        }
        out.close();
    }
    
    /* public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException
    {
	doGet(request, response);
	} */
}
