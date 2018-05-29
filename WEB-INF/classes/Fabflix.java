
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

public class Fabflix extends HttpServlet
{
    public String getServletInfo()
    {
       return "Servlet connects to MySQL database and displays result of a SELECT";
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException
	{
        	PrintWriter out = response.getWriter();
		out.println("<script type=\"text/javascript\">  location = '/Fabflix/'; </script>");
	}

    public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException
    {
	// Output stream to STDOUT
        PrintWriter out = response.getWriter();
	
	/*String gRecaptchaResponse = request.getParameter("g-recaptcha-response");
	//Verify CAPTCHA
	boolean valid = VerifyUtils.verify(gRecaptchaResponse);
	if(!valid){
	  //errorString = "Captcha invalid!";
	  out.println("<script type=\"text/javascript\"> alert('Failed to do ReCaptcha.'); location = '/Fabflix/'; </script>");
	  //return;
	}*/

        String loginUser = "root";
        String loginPasswd = "rootPassWord";
        String loginUrl = "jdbc:mysql:///moviedb?autoReconnect=true&useSSL=false";

        response.setContentType("text/html");    // Response mime type      

	out.println("<script src=\"https://ajax.googleapis.com/ajax/libs/jquery/3.2.0/jquery.min.js\"></script>  <script src=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js\"></script><!-- Latest compiled and minified CSS --><link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css\" integrity=\"sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u\" crossorigin=\"anonymous\"><!-- Optional theme --><link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap-theme.min.css\" integrity=\"sha384-rHyoN1iRsVXV4nD0JutlnGaslCJuC7uwjduW9SVrLvRYooPp2bWYgmgJQIXwl/Sp\" crossorigin=\"anonymous\"><!-- Latest compiled and minified JavaScript --><script src=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js\" integrity=\"sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa\" crossorigin=\"anonymous\"></script><script src=\"https://code.jquery.com/jquery-latest.min.js\"></script>");
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
	out.println("<div style=\"position: relative; top: 0; height: 45px; width: 100%; text-align: right; border-bottom: 1px solid #007EE5; background-color: #4f6b9b; padding: 10px; padding-right: 20px; box-shadow: 0 2px 26px 0 rgba(0,0,0,0.18);\">");
	out.println("<a href=\"/Fabflix/items?movieID=&quantity=0\"> <button style=\"position: absolute; right: 12%; background-color: transparent; font-weight: bold; color: white; border-radius: 3px; position: absolute; bottom: 0;\" class=\"btn\">Shopping Cart</button></a>");
	out.println("<form action=\"/Fabflix/logout\" method=\"get\"><button type=submit style=\"background-color: transparent; float: right; font-weight: bold; color: white; border-radius: 3px; bottom: 10%;\" class = \"btn\">Logout</button></form>");
	

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
		     if(loggedIn.equals("false")){
			String gRecaptchaResponse = request.getParameter("g-recaptcha-response");
			//Verify CAPTCHA
			boolean valid = VerifyUtils.verify(gRecaptchaResponse);
			if(!valid){
	  			//errorString = "Captcha invalid!";
	  			out.println("<script type=\"text/javascript\"> alert('Failed to do ReCaptcha.'); location = '/Fabflix/'; </script>");
			}
		     }
		//SEARCH BAR
	      	out.println("<FORM ACTION = \"/Fabflix/search\" METHOD = \"GET\">" +
				"  <b> Title:</b> <INPUT TYPE=\"TEXT\" NAME=\"title\" onkeyup=\"ajaxFunction()\" id = \"titleForm\" autocomplete=\"off\"><br>" +
		"<div id=\"somediv\" style=\"box-shadow: 0 2px 26px 0 rgba(0,0,0,0.18); z-index=9999; max-width: 151px; margin: 0 auto; \"></div>" + 
		    		"  <b>  Year:</b> <INPUT TYPE=\"TEXT\" NAME=\"year\"><br>" +
				"<b>Director:</b> <INPUT TYPE=\"TEXT\" NAME=\"director\"><br>" +
				"    <b>Star:</b> <INPUT TYPE=\"TEXT\" NAME=\"star\"><br>" +
				"<INPUT class=\"btn-search\" TYPE=\"SUBMIT\" VALUE=\"Search\"></FORM>");


		String style = "style=\"border: none; background: transparent; text-decoration: underline; color: #007EE5;\"";
		//Ajax 
		out.println("<script>");
                //out.println("$(document).on(\"click\", \"#somebutton\", function() { ");
		out.println("var ajaxRequest;");
		out.println("function ajaxFunction(){");
		out.println("var v = document.getElementById(\"titleForm\").value;");
		out.println("var url = \"/Fabflix/AjaxServlet?val=\" + v;");
		out.println("try{	\n	ajaxRequest = new XMLHttpRequest();	\n} catch (e){\n		try{		\n	ajaxRequest = new ActiveXObject(\"Msxml2.XMLHTTP\");	\n	} catch (e) {\n			try{\n				ajaxRequest = new ActiveXObject(\"Microsoft.XMLHTTP\");		\n	} catch (e){\n				alert(\"Your browser broke!\");	\n		}		}	}");
		out.println("ajaxRequest.onreadystatechange = getInfo;\n");
		out.println("ajaxRequest.open(\"GET\", url, true);\n ajaxRequest.send()\n");
		out.println("function getInfo() { \n if(ajaxRequest.readyState == 4){			\n var v = ajaxRequest.responseText; \n ");
		out.println("console.log(v); document.getElementById(\"somediv\").innerHTML = v;	\n	}}	");

                //out.println("$.get(\"AjaxServlet\", function(responseText) { "); 
                //out.println("    $(\"#somediv\").text(responseText);");
		//out.println("};");
                out.println("}        </script>");
		out.println("<div class =\"row\" style=\"margin-top: 20px;\">");
		out.println("<div class =\"col-xs-6\">");

		//BROWSE BY GENRE LINKS
		out.println("<p style=\"text-align: center; font-weight: bold; font-size: 20px;\">Browse by Genre</p>");
		String genreQuery = "SELECT name FROM genres";
		ResultSet genreSet = statement.executeQuery(genreQuery);
		out.println("<FORM ACTION = \"/Fabflix/search\" METHOD=\"GET\">");
		while(genreSet.next()){
			out.println("<BUTTON TYPE=\"SUBMIT\" " + style + " VALUE=\""+ genreSet.getString("name") + "\" NAME =\""
			+ "genre\">" + genreSet.getString("name") + "</BUTTON><br>");
		}
		out.println("</FORM>");
	
		//BROWSE BY TITLE LINKS
		out.println("</div>");
		out.println("<div class =\"col-xs-6\">");
		out.println("<p style=\"text-align: center; font-weight: bold; font-size: 20px;\">Browse by Title</p>");
		String letters = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		out.println("<FORM ACTION = \"/Fabflix/search\" METHOD=\"GET\">");
		for(int i = 0; i < 36; i++){
			out.println("<BUTTON TYPE=\"SUBMIT\" " + style + " VALUE=\"" + letters.charAt(i) + "\" NAME = \"letter\">"
				+ letters.charAt(i) + "</BUTTON>");
			if (i%4==0 && i != 0){
				out.println("<br>");
			}
		}
		out.println("</FORM>");
		out.println("</div>");
		out.println("</div>");

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
    }
    
}

