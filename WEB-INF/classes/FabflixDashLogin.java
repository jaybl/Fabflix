
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
//import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class FabflixDashLogin extends HttpServlet
{
    public String getServletInfo()
    {
       return "Servlet connects to MySQL database and displays result of a SELECT";
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException
	{
		HttpSession session = request.getSession();
		session.setAttribute("empLoggedIn", "false");
        	PrintWriter out = response.getWriter();
		//out.println("<script type=\"text/javascript\">  location = '/Fabflix/'; </script>");
		String loginUser = "root";
        	String loginPasswd = "rootPassWord";
        	String loginUrl = "jdbc:mysql:///moviedb?autoReconnect=true&useSSL=false";
		response.setContentType("text/html");
		out.println("<HTML><HEAD><TITLE>Fabflix Dashboard</TITLE></HEAD> " +
			    "<H1>Fabflix Dashboard</H1><FORM ACTION=\"/Fabflix/dashboard\" METHOD=\"POST\">" +
			    "<b>Email:</b><input type=\"TEXT\" name=\"email\"><BR>" +
			    "<b>Password:</b><input type=\"PASSWORD\" name=\"password\"><BR>" +
			    "<input class=\"button\" type=\"submit\" value=\"login\"></FORM></HTML>");
	}

   /*public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException
    {
	// Output stream to STDOUT
        PrintWriter out = response.getWriter();
	

        String loginUser = "root";
        String loginPasswd = "rootPassWord";
        String loginUrl = "jdbc:mysql:///moviedb?autoReconnect=true&useSSL=false";

        response.setContentType("text/html");    // Response mime type      

	out.println("<script src=\"https://ajax.googleapis.com/ajax/libs/jquery/3.2.0/jquery.min.js\"></script>  <script src=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js\"></script><!-- Latest compiled and minified CSS --><link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css\" integrity=\"sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u\" crossorigin=\"anonymous\"><!-- Optional theme --><link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap-theme.min.css\" integrity=\"sha384-rHyoN1iRsVXV4nD0JutlnGaslCJuC7uwjduW9SVrLvRYooPp2bWYgmgJQIXwl/Sp\" crossorigin=\"anonymous\"><!-- Latest compiled and minified JavaScript --><script src=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js\" integrity=\"sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa\" crossorigin=\"anonymous\"></script>");
	out.println("<style>");
	out.println(" html{ ");
	out.println("font-family: Arial; overflow-x: hidden;}");
	out.println("form{ text-align: center; }");
	out.println("input{ margin-bottom: 10px; }");
	out.println("h1{ text-align: center; }");
	out.println("input{ border: none; border-bottom: 1px solid #007EE5; margin-left: 10px; padding-left: 5px;}");
	out.println(".btn-search{ border: 1px solid #007EE5 !important; border-radius: 3px; font-weight: bold; background: transparent; padding: 5px; padding-left: 15px; padding-right: 15px; margin-top: 25px;}");
	out.println("</style>");
	out.println("</HEAD>");
        out.println("<BODY>");
	out.println("</div><H1 style=\"font-weight: bold;\">Fabflix</H1>");


        try{
              	//Class.forName("org.gjt.mm.mysql.Driver");
              	Class.forName("com.mysql.jdbc.Driver").newInstance();
             	Connection dbcon = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
              	Statement statement = dbcon.createStatement();

		//SET LOGIN BOOL IF ITS THE FIRST TIME
		HttpSession session = request.getSession();
    		String loggedIn = (String)session.getAttribute("loggedIn");
    		if (loggedIn == null){
			loggedIn = "false";
      			session.setAttribute("loggedIn", loggedIn);
		}

		//IF LOGGED IN, DONT CHECK LOGIN INFO AGAIN
	      	boolean validInfo = false;
	      	if(loggedIn.equals("false")){
	      		String email = request.getParameter("email");
	      		String password = request.getParameter("password");
              		String query = "SELECT * from customers where email = '" + email + "' and password = '" + password + "'";

              		ResultSet rs = statement.executeQuery(query);

              		while (rs.next()){
				validInfo = true;
				session.setAttribute("loggedIn", "true");
              		}
			rs.close();
	      	}
		else
			validInfo = true;

		if(!validInfo)
			out.println("<script type=\"text/javascript\"> alert('User or password incorrect'); location = '/Fabflix/'; </script>");
		else{


		String style = "style=\"border: none; background: transparent; text-decoration: underline; color: #007EE5;\"";


		out.println("testing deez nuts");
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
    }*/
    
}


