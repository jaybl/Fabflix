import java.io.*;
import java.net.*;
import java.sql.*;
import java.text.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class FabflixStars extends HttpServlet
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

        out.println("<HTML><HEAD><TITLE>Fabflix Star</TITLE>");
	out.println("<script src=\"https://ajax.googleapis.com/ajax/libs/jquery/3.2.0/jquery.min.js\"></script>  <script src=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js\"></script><!-- Latest compiled and minified CSS --><link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css\" integrity=\"sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u\" crossorigin=\"anonymous\"><!-- Optional theme --><link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap-theme.min.css\" integrity=\"sha384-rHyoN1iRsVXV4nD0JutlnGaslCJuC7uwjduW9SVrLvRYooPp2bWYgmgJQIXwl/Sp\" crossorigin=\"anonymous\"><!-- Latest compiled and minified JavaScript --><script src=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js\" integrity=\"sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa\" crossorigin=\"anonymous\"></script>");
	out.println("<style>");
	out.println("html{ font-family: Arial;}");
	out.println(".btn-search{ background-color: transparent; float: right; font-weight: bold; color: white; border-radius: 3px; }");
	out.println("</style></HEAD>");
        out.println("<BODY>");
	out.println("<div style=\"position: relative; top: 0; height: 45px; width: 100%; text-align: right; border-bottom: 1px solid #007EE5; background-color: #4f6b9b; padding: 10px; padding-right: 20px; box-shadow: 0 2px 26px 0 rgba(0,0,0,0.18);\">");
	out.println("<form action=\"/Fabflix/main\" method=\"post\"><button type=submit style=\"position: absolute; bottom: 10%; right: 29%;\" class=\"btn btn-search\">FABFLIX HOME</button></form>");
	out.println("<a href=\"/Fabflix/items?movieID=&quantity=0\"> <button style=\"position: absolute; right: 11%; bottom: 10%;\" class=\"btn btn-search\">Shopping Cart</button></a>");
	out.println("<form action=\"/Fabflix/logout\" method=\"get\"><button type=submit style=\"position: absolute; right: 1%; bottom: 10%;\" class=\"btn btn-search\">Logout</button></form>");
	out.println("</div>");


        try
	{
		//Class.forName("org.gjt.mm.mysql.Driver");
		Class.forName("com.mysql.jdbc.Driver").newInstance();

		Connection dbcon = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
		// Declare our statement
		Statement statement = dbcon.createStatement();

		String starID = request.getParameter("starID");
		String starQuery = "SELECT * from stars where id = " + starID;
		String movieQuery = "select * from stars, stars_in_movies, movies where stars.id = star_id" + 
				" and movie_id = movies.id and  stars.id = " + starID;
		ResultSet starSet = statement.executeQuery(starQuery);
		while(starSet.next())
		{
		  String s_ID = starSet.getString("ID");
		  String s_FN = starSet.getString("first_name");
		  String s_LN = starSet.getString("last_name");
		  out.println("<h1 style=\"text-align: center;\">" + s_FN + " " + s_LN + "</h1>");
	out.println("<div class = \"row\" style=\"margin-top: 25px;\">");
	out.println("<div class=\"col-xs-6\" style = \"position: relative;\"> ");
		  String s_DOB = starSet.getString("dob");
		  String imageurl = starSet.getString("photo_url");
		 
		  out.println("<img src=\"" + imageurl + "\" alt=\"" + s_FN + " " + s_LN +"\" style=" +
			      "\"width:333px;height:500px;\">");		  
		  out.println("</div><div class=\"col-xs-6\" style=\"position: relative; text-align: center;\"><p style=\"margin-top: 56px;\"><b>ID:</b> " + s_ID +
			     "<br><b> Name:</b> " + s_FN + " " + s_LN +
			      "<br><b> DOB:</b> " + s_DOB + "</p>");
		}
		starSet.close();
		  
		ResultSet movieSet = statement.executeQuery(movieQuery);
		out.println("<p><b>MOVIES:</b></p>");
		String style = "style=\"border: none; background: transparent; text-decoration: underline; color: #007EE5;\"";
		while(movieSet.next())
		{
			String m_ID = movieSet.getString("movie_id");
			String m_T = movieSet.getString("title");
	
			out.println("<form action=\"/Fabflix/movie\" method=\"get\"><button type=\"submit\" " + style +
				    " name=\"movieID\" value=\"" + m_ID + "\">" + m_T + "</button></form>");
		}
		out.println("</p></div></div>");

		movieSet.close();
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

