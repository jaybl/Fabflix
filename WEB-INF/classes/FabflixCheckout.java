import java.io.*;
import java.net.*;
import java.sql.*;
import java.text.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class FabflixCheckout extends HttpServlet { 
   public void doGet(HttpServletRequest request,
                    HttpServletResponse response)
      throws ServletException, IOException {

    HttpSession session = request.getSession();
    String loginUser = "root";
    String loginPasswd = "rootPassWord";
    String loginUrl = "jdbc:mysql:///moviedb?autoReconnect=true&useSSL=false";
    
    Map<String, String> items = (HashMap)session.getAttribute("items");
    
    try{
      //Class.forName("org.gjt.mm.mysql.Driver");
      Class.forName("com.mysql.jdbc.Driver").newInstance();

      Connection dbcon = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);

      response.setContentType("text/html");
      PrintWriter out = response.getWriter();
      String updateString = "select * from movies where movies.id = ?";
      PreparedStatement updateID = dbcon.prepareStatement(updateString);
      
      String title = "Shopping Cart";
      String docType =
        "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 " +
        "Transitional//EN\">\n";
      out.println(docType +
                "<HTML>\n" +
                "<HEAD><TITLE>" + title + "</TITLE>");       
	out.println("<style>");

	out.println("html{ font-family: Arial; overflow-x: hidden;}");
	out.println(".btn-search{ background-color: transparent; float: right; font-weight: bold; color: white; border-radius: 3px; }");
	out.println("input{ border: none; border-bottom: 1px solid #007EE5; margin-left: 20px; padding-left: 5px; margin-bottom: 20px;}");

	out.println("</style>");
	out.println("<script src=\"https://ajax.googleapis.com/ajax/libs/jquery/3.2.0/jquery.min.js\"></script>  <script src=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js\"></script><!-- Latest compiled and minified CSS --><link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css\" integrity=\"sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u\" crossorigin=\"anonymous\"><!-- Optional theme --><link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap-theme.min.css\" integrity=\"sha384-rHyoN1iRsVXV4nD0JutlnGaslCJuC7uwjduW9SVrLvRYooPp2bWYgmgJQIXwl/Sp\" crossorigin=\"anonymous\"><!-- Latest compiled and minified JavaScript --><script src=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js\" integrity=\"sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa\" crossorigin=\"anonymous\"></script>");
	out.println("</HEAD>\n" +
                "<BODY BGCOLOR=\"#FDF5E6\">\n");

	out.println("<div style=\"position: relative; top: 0; height: 45px; width: 100%; text-align: right; border-bottom: 1px solid #007EE5; background-color: #4f6b9b; padding: 10px; padding-right: 20px; box-shadow: 0 2px 26px 0 rgba(0,0,0,0.18); font-weight: bold;\">");
	out.println("<form action=\"/Fabflix/main\" method=\"post\"><button type=submit style=\"position: absolute; bottom: 10%; right: 11%;\" class=\"btn btn-search\"><b>FABFLIX HOME</b></button></form>");
	out.println("<form action=\"/Fabflix/logout\" method=\"get\"><button type=submit style=\"position: absolute; right: 1%; bottom: 10%;\" class=\"btn btn-search\"><b>Logout</b></button></form>");
	out.println("</div>");
        out.println( "<H1 style=\"font-weight: bold; text-align: center; margin-bottom: 25px;\">" + title + "</H1>");
 
      synchronized(items) {        
	out.println("<div class=\"row\" style=\"text-align: center; padding: 15px;\">");
        if (items.isEmpty()) {
          out.println("<I>No items</I>");
        } 
        else {
          out.println("<UL style=\"list-style-type: none;\">");
          for(String key: items.keySet()){
            updateID.setString(1,key);
	    ResultSet rs = updateID.executeQuery();
            while(rs.next()){
	      String m_T = rs.getString("title");
	      String imageurl = rs.getString("banner_url");
		out.println("<div style=\"box-shadow: 0 2px 26px 0 rgba(0,0,0,0.18);\">");
	      out.println(key + "<br><LI style=\"list-style-type: none;\"> <img src=\"" + imageurl + "\" alt=\"" + m_T + "\" style=\"width:66px;height:86px;\">" 
                          + "<br><BR><b> " 
                          + m_T + ":</b> " + items.get(key) + "<br><br><br></div>");
            }
	    rs.close();
          }
          out.println("</UL><br><br>");
        }
	out.println("</div>");
	out.println("<FORM ACTION=\"/Fabflix/transaction\" METHOD=\"POST\" style=\"text-align: center;\">" + 
		    "<b style=\"margin-left: 60px;\"> Credit Card Number:</b><input type=\"PASSWORD\" name=\"CCN\"><br>" +
		    "<b style=\"margin-left: 55px;\">Expiration Date (yyyy-mm-dd):</b><input type=\"TEXT\" name=\"expDate\" style=\"width: 100px;\"><br>" +
                    "<b>First Name:</b><input type=\"TEXT\" name=\"firstName\"><br>" +
                    "<b>Last Name:</b><input type=\"TEXT\" name=\"lastName\"><br>" +
                    "<input class=\"BUTTON\" type=\"SUBMIT\" value=\"Place Order\" style=\" border: 1px solid #007EE5; border-radius: 3px; font-weight: bold; background-color: white; padding: 10px; padding-left: 15px; padding-right: 15px;  margin-top: 20px;\"></form>");
      }
      out.println("</BODY></HTML>");
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
	response.getWriter().println("<HTML>" +
                    "<HEAD><TITLE>" +
                    "MovieDB: Error" +
                    "</TITLE></HEAD>\n<BODY>" +
                    "<P>SQL error in doGet: " +
                    ex.getMessage() + "</P></BODY></HTML>");
        return;
    }
   // The following two statements show how this thread can access an
   // object created by a thread of the ShowSession servlet
   // Integer accessCount = (Integer)session.getAttribute("accessCount");
   // out.println("<p>accessCount = " + accessCount);
  }
}

