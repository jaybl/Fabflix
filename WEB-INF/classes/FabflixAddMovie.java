import java.io.*;
import java.net.*;
import java.sql.*;
import java.text.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class FabflixAddMovie extends HttpServlet
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

		//adding a movie
		String title = request.getParameter("movieTitle").toString();
		String year = request.getParameter("movieYear").toString();
		String director = request.getParameter("movieDir").toString();
		String starName = request.getParameter("starName").toString();
		String genre = request.getParameter("movieGenre").toString();			
		String starFirst = "";
		String starLast = "";
		if(starName.contains(" ")){
			String[] names = starName.split(" ");
			starFirst = names[0];
			starLast = names[1];
		}
		else{
			starFirst = "\'\'";
			starLast = starName;
		}
		//out.println("Working on movie insertion...");
		CallableStatement cs;
		if(starFirst.equals("\'\'")){
			cs = dbcon.prepareCall("{call add_movie('"+title+"',"
					      +year+",'"+director+"',"+starFirst+",'"
					      +starLast+"','"+genre+"')}");
		}
		else{
			cs = dbcon.prepareCall("{call add_movie('"+title+"',"
					      +year+",'"+director+"','"+starFirst+"','"
					      +starLast+"','"+genre+"')}");
		}
		/*cs.setString(1, title);
		int yearInt = Integer.parseInt(year);
		cs.setInt(2, yearInt);	
		cs.setString(3, director);
		cs.setString(4, starFirst);
		cs.setString(5, starLast);
		cs.setString(6, genre);*/
		boolean exists = false;
		boolean bool = cs.execute();
		int i = 1;
		for( int x = 0; x < 5; x++){
			ResultSet rs = cs.getResultSet();
			while(rs.next())
				if (rs.getString(i).equals("movie already exists.")){
					exists = true;
				}
				//rs.getString(i++);
			bool = cs.getMoreResults();
		}
		while(bool){
			ResultSet rs = cs.getResultSet();
			while(rs.next())
				out.println(rs.getString(i));
				//rs.getString(i++);
				i++;
			bool = cs.getMoreResults();
		}
		cs.close();
		dbcon.close();
		if (exists)
		out.println("<script type=\"text/javascript\"> alert('Movie already exists'); location=\"/Fabflix/dashboard\"; </script>");
		else
		out.println("<script type=\"text/javascript\"> alert('Insert Successful'); location=\"/Fabflix/dashboard\"; </script>");
	}
        catch(SQLException ex){
		while (ex != null){
                    out.println ("SQL Exception:  " + ex.getMessage ());
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

