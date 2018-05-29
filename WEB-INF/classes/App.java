
/* A servlet to display the contents of the MySQL movieDB database */

import java.io.*;
import java.net.*;
import java.sql.*;
import java.text.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class App extends HttpServlet
{
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException
    {
        PrintWriter out = response.getWriter();

        String loginUser = "root";
        String loginPasswd = "rootPassWord";
        String loginUrl = "jdbc:mysql:///moviedb?autoReconnect=true&useSSL=false";

        try{
              	Class.forName("com.mysql.jdbc.Driver").newInstance();
             	Connection dbcon = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
              	Statement statement = dbcon.createStatement();

		String email = request.getParameter("email");
		String password = request.getParameter("password");
		String search = request.getParameter("search");
		if(email != null && password != null){
			String query = "select * from customers where email = '"+email+"' and password = '"+password+"'";
			ResultSet rs = statement.executeQuery(query);
			boolean valid = false;
			while(rs.next())
				valid = true;
			if(valid)
				out.println("Y");
			else
				out.println("N");
		}
		else if(search != null){
			String query = "select title from movies where title like '%"+search+"%'";
			ResultSet rs = statement.executeQuery(query);
			boolean valid = false;
			while(rs.next()){
				valid = true;
				out.println(rs.getString("title"));
			}
			if(!valid)
				out.println("No Results found.");
		}

              	statement.close();
             	dbcon.close();
        }
        catch (SQLException ex) {
              while (ex != null) {
                    System.out.println ("SQL Exception:  " + ex.getMessage ());
                    ex = ex.getNextException ();
              }  // end while
        }  // end catch SQLException
        catch(java.lang.Exception ex){
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
}

