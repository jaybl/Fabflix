import java.io.*;
import java.net.*;
import java.sql.*;
import java.text.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class README extends HttpServlet
{
    public String getServletInfo()
    {
       return "Servlet connects to MySQL database and displays result of a SELECT";
    }

    // Use http GET

    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException
    {
        //String loginUser = "root";
        //String loginPasswd = "rootPassWord";
        //String loginUrl = "jdbc:mysql:///moviedb?autoReconnect=true&useSSL=false";

        response.setContentType("text/html");    // Response mime type

        // Output stream to STDOUT
        PrintWriter out = response.getWriter();

        out.println("<HTML><HEAD><TITLE>README</TITLE></HEAD>");
        out.println("<BODY><H1>README</H1>");


        try
           {
              //Class.forName("org.gjt.mm.mysql.Driver");
              //Class.forName("com.mysql.jdbc.Driver").newInstance();
              //Connection dbcon = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
              // Declare our statement
              //Statement statement = dbcon.createStatement();
		
		out.println("1. Compile all java files with the following command:<br>");
		out.println("javac -classpath ../lib/servlet-api.jar:../lib/mysql-connector-java-5.0.8-bin.jar FabflixSearch.java<br>");
		out.println("With all java files in the place of FabflixSearch.java<br>");
		out.println("If there is a problem with this command, try:<br>");
		out.println("javac -classpath ../lib/servlet-api.jar:../lib/mysql-connector-java-5.14.0-bin.jar FabflixSearch.java<br><br>");
		out.println("2. Place all class files into /WEB-INF/classes"); 
            }

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

