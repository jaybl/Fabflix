import java.io.*;
import java.net.*;
import java.sql.*;
import java.text.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class FabflixDashboard extends HttpServlet
{
    public String getServletInfo()
    {
       return "Servlet connects to MySQL database and displays result of a SELECT";
    }

    // Use http GET
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException
    {
        doPost(request, response);
    }


    public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException
    {
        String loginUser = "root";
        String loginPasswd = "rootPassWord";
        String loginUrl = "jdbc:mysql:///moviedb?autoReconnect=true&useSSL=false";
        response.setContentType("text/html");    // Response mime type

        // Output stream to STDOUT
        PrintWriter out = response.getWriter();

       /* out.println("<HTML><HEAD><TITLE>Fabflix Star</TITLE>");
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
	out.println("</div>");*/


        try
	{
		//Class.forName("org.gjt.mm.mysql.Driver");
		Class.forName("com.mysql.jdbc.Driver").newInstance();

		Connection dbcon = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
		// Declare our statement
		Statement statement = dbcon.createStatement();

		HttpSession session = request.getSession();
    		String loggedIn = (String)session.getAttribute("empLoggedIn");
    		if (loggedIn == null){
			loggedIn = "false";
      			session.setAttribute("empLoggedIn", loggedIn);
		}

		//IF LOGGED IN, DONT CHECK LOGIN INFO AGAIN
	      	boolean validInfo = false;
	      	if(loggedIn.equals("false")){
	      		String email = request.getParameter("email");
	      		String password = request.getParameter("password");
              		String query = "SELECT * from employees where email = '" + email + "' and password = '" + password + "'";

              		ResultSet rs = statement.executeQuery(query);

              		while (rs.next()){
				validInfo = true;
				session.setAttribute("empLoggedIn", "true");
              		}
			rs.close();
	      	}
		else
			validInfo = true;

		if(!validInfo)
			out.println("<script type=\"text/javascript\"> alert('User or password incorrect'); location = '/Fabflix/_dashboard'; </script>");
		else{
			out.println("<form action=\"/Fabflix/newStar\" method=\"GET\">" +
				    "<b>Insert new Star:</b><input type=\"TEXT\" name=\"starName\">" +
				    "<input class=\"button\" type=\"submit\" value=\"Insert Star\"></form>");

			out.println("<form action=\"/Fabflix/newMovie\" method=\"GET\">" +
				    "<b>Add new movie:</b><br>" +
				    "<b>title:</b><input type=\"TEXT\" name=\"movieTitle\"><br>" +
				    "<b>year:</b><input type=\"TEXT\" name=\"movieYear\"><br>" +
				    "<b>director:</b><input type=\"TEXT\" name=\"movieDir\"><br>" +
				    "<b>star name:</b><input type=\"TEXT\" name=\"starName\"><br>" +
				    "<b>genre:</b><input type=\"TEXT\" name=\"movieGenre\"><br>" +
				    "<input class=\"button\" type=\"submit\" value=\"Add Movie\"></form>");		
			out.println("<form action=\"/Fabflix/metadata\" method=\"GET\">" +
				    "<input class=\"button\" type=\"submit\" value=\"Get Metadata\"></form>");
			
			statement.close();
		/*
		String insert_str = "INSERT INTO stars (first_name, last_name) VALUES (?, ?)";
		PreparedStatement state = dbcon.prepareStatement(insert_str);
		if(name.contains(" ")){
			String[] names = name.split(" ");
			state.setString(1, names[0]);
			state.setString(2, names[1]);
		}
		else{
			state.setString(1, "");
			state.setString(2, name);
		}
		state.execute();
			out.println("<script type=\"text/javascript\"> alert('Insertion successful!'); location = '/Fabflix/_dashboard'; </script>");
			state.close();
		*/
			dbcon.close();
		}
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

