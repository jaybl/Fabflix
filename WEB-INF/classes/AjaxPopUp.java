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


public class AjaxPopUp extends HttpServlet
{
	@Override
	 public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
	{
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		String id = request.getParameter("value");
		String loginUrl = "jdbc:mysql:///moviedb?autoReconnect=true&useSSL=false";
		String loginUser = "root";
	        String loginPasswd = "rootPassWord";
		String formItems = "<FORM  ACTION=\"/Fabflix/items\" METHOD=\"GET\" >";
		out.println("<script> function clickAlert(){ alert(\"Processing...\");} </script>");

		try{              		
			Class.forName("com.mysql.jdbc.Driver").newInstance();		              
			Connection dbcon = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
	              	Statement statement = dbcon.createStatement();
			
			String query = "select * from movies where movies.id = " + id;
			ResultSet rs = statement.executeQuery(query);
			while(rs.next()){
				String m_id = rs.getString("ID");
				String m_t = rs.getString("title");
				String m_y = rs.getString("year");
				String m_d = rs.getString("director");
				String img_url = rs.getString("banner_url");
				String trailer = rs.getString("trailer_url");
				
				out.println("<img src=\"" + img_url + "\" alt=\"" + m_t +"\" style=" +
			      	"\"position:absolute;width:111px;height:166px;vertical-align: middle; margin-left: 10%;;\"><br>");
				out.println("<p style=\"margin-top: 75%;\">" +
			  	"<b>ID:</b> " + m_id +
			  	"<br> <b>Title:</b> " + m_t +
			  	"<br> <b>Year:</b> " + m_y +
			  	"<br> <b>Director:</b> " + m_d + "</p>");
				out.println("<a href=" + trailer + ">Trailer Link</a><br><br>");
				out.println(formItems + "<button type=\"SUBMIT\" onclick=\"clickAlert()\" value=\"" + m_id + "\" name=\"movieID\">Add to Shopping Cart</button>");
				out.println("<INPUT  type\"TEXT\" value=\"\" name=\"quantity\" style=\"width: 40px; height: 40px; margin-left: 5px; padding-left: 5px;\"></form><br>");

			}			
			rs.close();
			dbcon.close();
		}
		catch (Exception e){
			out.println(e);
		}
	}
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
		return;
    }
}

