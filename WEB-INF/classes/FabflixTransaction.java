import java.time.LocalDateTime;
import java.io.*;
import java.net.*;
import java.sql.*;
import java.text.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class FabflixTransaction extends HttpServlet
{
    public String getServletInfo()
    {
       return "Servlet connects to MySQL database and displays result of a SELECT";
    }

    // Use http GET

    public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException
    {
        String loginUser = "root";
        String loginPasswd = "rootPassWord";
        String loginUrl = "jdbc:mysql:///moviedb?autoReconnect=true&useSSL=false";

        response.setContentType("text/html");    // Response mime type
	HttpSession session = request.getSession();
	Map<String, String> items = (HashMap)session.getAttribute("items");
        // Output stream to STDOUT
        PrintWriter out = response.getWriter();

      out.println("<HTML>\n" +
                "<HEAD><TITLE>Fabflix: Transaction</TITLE>");       
	out.println("<style>");

	out.println("html{ font-family: Arial; overflow-x: hidden;}");
	out.println(".btn-search{ background-color: transparent; float: right; font-weight: bold; color: white; border-radius: 3px; }");
	out.println("input{ border: none; border-bottom: 1px solid #007EE5; margin-left: 20px; padding-left: 5px; margin-bottom: 20px;}");

	out.println("</style>");
	out.println("<script src=\"https://ajax.googleapis.com/ajax/libs/jquery/3.2.0/jquery.min.js\"></script>  <script src=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js\"></script><!-- Latest compiled and minified CSS --><link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css\" integrity=\"sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u\" crossorigin=\"anonymous\"><!-- Optional theme --><link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap-theme.min.css\" integrity=\"sha384-rHyoN1iRsVXV4nD0JutlnGaslCJuC7uwjduW9SVrLvRYooPp2bWYgmgJQIXwl/Sp\" crossorigin=\"anonymous\"><!-- Latest compiled and minified JavaScript --><script src=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js\" integrity=\"sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa\" crossorigin=\"anonymous\"></script>");
	out.println("</HEAD><body>\n");
	out.println("<div style=\"position: relative; top: 0; height: 45px; width: 100%; text-align: right; border-bottom: 1px solid #007EE5; background-color: #4f6b9b; padding: 10px; padding-right: 20px; box-shadow: 0 2px 26px 0 rgba(0,0,0,0.18); font-weight: bold;\">");
	out.println("<form action=\"/Fabflix/logout\" method=\"get\"><button type=submit style=\"position: absolute; right: 1%; bottom: 10%;\" class=\"btn btn-search\"><b>Logout</b></button></form>");
	out.println("</div>");
        out.println( "<H1 style=\"font-weight: bold; text-align: center; margin-bottom: 25px;\">Fabflix: Transaction</H1>");


          try
          {
              //Class.forName("org.gjt.mm.mysql.Driver");
              Class.forName("com.mysql.jdbc.Driver").newInstance();

              Connection dbcon = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
              // Declare our statement
              Statement statement = dbcon.createStatement();

	      boolean valid = false;
              String ccn = request.getParameter("CCN");
	      String date = request.getParameter("expDate");
	      String firstName = request.getParameter("firstName");
	      String lastName = request.getParameter("lastName");
              String query = "SELECT * from creditcards where creditcards.id = " + ccn + 
			     " and creditcards.first_name = '" + firstName +
			     "' and creditcards.last_name = '" + lastName +
			     "' and creditcards.expiration = '" + date + "'";
              if(ccn.equals("") || date.equals("") || firstName.equals("") || lastName.equals(""))
	      {
	         out.println("<p style=\"text-align: center;\">Sorry, the credit card information is incorrect.</p><br>");
		 out.println("<form action=\"/Fabflix/checkout\" method=\"GET\"><button type=\"SUBMIT\" style=\"padding: 10px 15px; font-weight: bold; background-color: white; border-radius: 3px; border: 1px solid #007EE5; margin-left: 45vw;\">Try Again</button></form>");
	      }
	      // Perform the query
              ResultSet rs = statement.executeQuery(query);
	      out.println("<p style=\"text-align: center;\">Checking transaction...</p><br>");
              if(rs.next())
		  valid = true;
	      if(!valid)
	      {
	         out.println("<p style=\"text-align: center;\">Sorry, the credit card information is incorrect.</p><br>");
		 out.println("<form action=\"/Fabflix/checkout\" method=\"GET\"><button type=\"SUBMIT\" style=\"padding: 10px 15px; font-weight: bold; background-color: white; border-radius: 3px; border: 1px solid #007EE5; margin-left: 45vw;\">Try Again</button></form>");
	      }
	      else{
		rs.close();
	        String customer = "select customers.id from creditcards, customers where creditcards.id = cc_id" +
				  " and creditcards.id = " + ccn;
	        ResultSet customerInfo = statement.executeQuery(customer);
	        LocalDateTime now = LocalDateTime.now();
		String c_ID = "";
	        while(customerInfo.next()){
		   c_ID = customerInfo.getString("id");
		}
		   for(String key: items.keySet()){
		     String insert = "INSERT INTO sales (customer_id, movie_id, sale_date)" +
				     " VALUES (" + c_ID + ", " + key + ", '" + now.getYear() + "-" + 
 	                             now.getMonthValue() + "-" + now.getDayOfMonth() + "')";
 		     statement.executeUpdate(insert);
                }
		customerInfo.close();
	      }
	      session.removeAttribute("items");
	      out.println("<p style=\" font-weight: bold; text-align: center;\">Transaction complete.</p><br><form action=\"/Fabflix/main\" method=\"post\">" +
			  "<button type=\"submit\" style=\"padding: 10px 15px; font-weight: bold; background-color: white; border-radius: 3px; border: 1px solid #007EE5; margin-left: 45vw;\">FABFLIX HOME</button></form>");
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
    }
    
    /* public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException
    {
	doGet(request, response);
	} */
}
