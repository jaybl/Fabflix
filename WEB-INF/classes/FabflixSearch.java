/* A servlet to display the contents of the MySQL movieDB database */
import java.io.*;
import java.net.*;
import java.sql.*;
import java.text.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class FabflixSearch extends HttpServlet
{
    public String getServletInfo(){
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
        out.println("<HTML><HEAD><TITLE>Fabflix Search: Found Records</TITLE>");
	out.println("<script src=\"https://ajax.googleapis.com/ajax/libs/jquery/3.2.0/jquery.min.js\"></script>  <script src=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js\"></script><!-- Latest compiled and minified CSS --><link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css\" integrity=\"sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u\" crossorigin=\"anonymous\"><!-- Optional theme --><link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap-theme.min.css\" integrity=\"sha384-rHyoN1iRsVXV4nD0JutlnGaslCJuC7uwjduW9SVrLvRYooPp2bWYgmgJQIXwl/Sp\" crossorigin=\"anonymous\"><!-- Latest compiled and minified JavaScript --><script src=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js\" integrity=\"sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa\" crossorigin=\"anonymous\"></script>");
	out.println("<style>");
	out.println(".btn-search{ background-color: transparent; float: right; font-weight: bold; color: white; border-radius: 3px; }");
	out.println("select{ border: none; border-bottom: 1px solid #007EE5;  }");
	out.println("table{ margin-top: 25px; margin-bottom: 20px; }");
	out.println("</style>");
	out.println("</HEAD>");
	//out.println("<div style=\"position: fixed; top: 0; width: 100%; height: 20px; text-align: left; border-bottom: 1px solid #007EE5; " +
	//"background-color: white; padding: 10px; padding-right: 20px; box-shadow: 0 2px 26px 0 rgba(0,0,0,0.18); z-index: 900 !important\">test</div>");
	//MOVIEDIV (moved here)
	out.println( "<div id=\"movieDiv\"  style=\"box-shadow: 0 2px 26px 0 rgba(0,0,0,0.18); position: fixed; z-index=9999; padding: 60px; z-index: 99999; margin-top: 10%; margin-left: 25%; background-color: white; display: none;\"><button style=\"z-index:99999;\" onclick=\"closePopUp()\">close</button></div>");
	out.println("<div style=\"position: relative; top: 0; height: 45px; width: 100%; text-align: right; border-bottom: 1px solid #007EE5; background-color: #4f6b9b; padding: 10px; padding-right: 20px; box-shadow: 0 2px 26px 0 rgba(0,0,0,0.18);\">");
	out.println("<form action=\"/Fabflix/main\" method=\"post\"><button type=submit style=\"position: absolute; bottom: 10%; right: 29%;\" class=\"btn btn-search\">FABFLIX HOME</button></form>");
	out.println("<a href=\"/Fabflix/items?movieID=&quantity=0\"> <button style=\"position: absolute; right: 11%; bottom: 10%;\" class=\"btn btn-search\">Shopping Cart</button></a>");
	out.println("<form action=\"/Fabflix/logout\" method=\"get\"><button type=submit style=\"position: absolute; right: 1%; bottom: 10%;\" class=\"btn btn-search\">Logout</button></form></div>");
        try{
              //Class.forName("org.gjt.mm.mysql.Driver");
              Class.forName("com.mysql.jdbc.Driver").newInstance();
              Connection dbcon = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
              Statement statement = dbcon.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
	      String formMovie = "<FORM ACTION=\"/Fabflix/movie\" METHOD=\"GET\">";
	      String formStar  = "<FORM ACTION=\"/Fabflix/star\" METHOD=\"GET\">";
	      String formSearch = "<FORM ACTION=\"/Fabflix/search\" METHOD=\"GET\">";
	      String style = "style=\"border: none; background: transparent; text-decoration: underline; color: #007EE5;\"";
	      String searchValue = "";
		
		//AJAX 
	      	out.println("<script>");
	      	out.println("var ajaxRequest;");
		out.println("function closePopUp(){");
		out.println("var v = document.getElementById(\"movieDiv\");");
		out.println("console.log(v);");
		out.println("v.style.display = \"none\";}");
	      	out.println("function popUp(x){");
		out.println("console.log($(x).text());");
		out.println("var v = $(x).closest('td').prev('td').text();");
		out.println("console.log(v);");
		out.println("document.getElementById(\"movieDiv\").style.display = \"block\";");
//	      	out.println("var v = document.getElementById(\"movieWindow\").value;");
	      	out.println("var url = \"/Fabflix/AjaxPop?value=\" + v;");
	      	out.println("try{	\n	ajaxRequest = new XMLHttpRequest();	\n} catch (e){\n		try{		\n	ajaxRequest = new ActiveXObject(\"Msxml2.XMLHTTP\");	\n	} catch (e) {\n			try{\n				ajaxRequest = new ActiveXObject(\"Microsoft.XMLHTTP\");		\n	} catch (e){\n				alert(\"Your browser broke!\");	\n		}		}	}");
	      	out.println("ajaxRequest.onreadystatechange = getInfo;\n");
	      	out.println("ajaxRequest.open(\"GET\", url, true);\n ajaxRequest.send()\n");
   	      	out.println("function getInfo() { \n if(ajaxRequest.readyState == 4){			\n var v = ajaxRequest.responseText; \n ");
	      	out.println("console.log(v); document.getElementById(\"movieDiv\").innerHTML = v + \'<button style=\"z-index:99999;\" onclick=\"closePopUp()\">close</button>\';	\n	}}	");
	      	out.println("}	</script>");
	      //GET ALL PARAMETERS
	      String title = request.getParameter("title");
	      String year = request.getParameter("year");
	      String director = request.getParameter("director");
	      String star = request.getParameter("star");
              String genre = request.getParameter("genre");
	      String letter = request.getParameter("letter");
	      String sortYear = request.getParameter("sortYear");
	      String sortTitle = request.getParameter("sortTitle");
	      String page = request.getParameter("page");
		if(page == null)
			page = "1";
	      String numPerPage = request.getParameter("numPerPage");
		if(numPerPage == null)
			numPerPage = "10";

	      //SEARCH QUERY AND SETTING WHERE CLAUSE
	      String query = "SELECT * from movies, stars, stars_in_movies where" +
				" star_id = stars.id and movie_id = movies.id";	      
		if(genre != null){
			query += " and movies.id in (SELECT movies.id from movies, genres, genres_in_movies where" +
				 " genre_id = genres.id and movie_id = movies.id and " +
				 "genres.name = \"" + genre + "\")";
			searchValue += "genre=" + genre;
		}
		else if(letter != null){
			query += " and movies.title like '" + letter + "%'";
			searchValue += "letter=" + letter;
		}
		else{
			if(title==null&&year==null&&director==null&&star==null)
			{title="";year="";director="";star="";}
			searchValue += "title=" + title +
				        "&year=" + year +
				        "&director" + director +
					"&star=" + star;
	      		if (title != null && !title.isEmpty()){
				query += " and movies.title like '%" + title + "%'";
			}
	      		if(year != null && !year.isEmpty()){
				query += " and movies.year like '%" + year + "%'";
			}
			if(director != null && !director.isEmpty()){
				query += " and movies.director like '%" + director + "%'";
			}
			if(star != null && !star.isEmpty()){
				String[] names = star.split(" ");
				if(names.length == 1)
					query += " and stars.first_name like '%\"\"%' and " +
						 "stars.last_name like '%" + names[0] + "%'";
				else
					query += " and stars.first_name like '%" + names[0] + "%' and " +
						 "stars.last_name like '%" + names[1] + "%'";
			}
		}
		if(sortYear != null)
			query += " ORDER BY year "+sortYear;
		else if(sortTitle != null)
			query += " ORDER BY title "+sortTitle;
              ResultSet rs = statement.executeQuery(query);

	      //INITIALIZE DICTIONARIES
	      Map<String,String> genreDict = new HashMap<String,String>();
	      Map<String,String> starDict = new HashMap<String,String>();
	      while(rs.next()){
		String movieID = rs.getString("movie_id");
		genreDict.put(movieID, null);
		starDict.put(movieID, null);
	      }

		if(genreDict.size() == 0){
			out.println("No Results Found.");
			rs.close();
			statement.close();
			dbcon.close();
		}else{

	      //SET GENRE DICTIONARY
              Statement gstatement = dbcon.createStatement();
	      for(String key : genreDict.keySet()){
		String genreQuery = "select distinct name from genres, genres_in_movies where " + 
				"genre_id = genres.id and movie_id = " + key;
		ResultSet gs = gstatement.executeQuery(genreQuery);
		String value = "<td>";
		boolean first = true;
		while(gs.next()){
			if(first)
				value += gs.getString("name");
			else
				value += ", " + gs.getString("name");
			first = false;
		}
		gs.close();
		value += "</td>";
		genreDict.put(key, value);
	      }
	      gstatement.close();

	      //SET STAR DICTIONARY
	      Statement sstatement = dbcon.createStatement();
	      for(String key : starDict.keySet()){
		String starQuery = "select star_id, first_name, last_name from stars, stars_in_movies where " +
				"stars.id = star_id and movie_id = " + key;
		ResultSet ss = sstatement.executeQuery(starQuery);
		String value = "<td>";
		while(ss.next()){
			value += formStar+"<button type=\"SUBMIT\" " + style + " value=\"" + ss.getString("star_id") +
					"\" name=\"starID\">" + ss.getString("first_name") + " " + ss.getString("last_name") + "</button></form>";
		}
		ss.close();
		value += "</td>";
		starDict.put(key, value);
	      }
	      sstatement.close();

		//ADD SORT PARAMETERS FOR ALL EXCEPT THE SORTING LINKS
		String sortSearchValue = searchValue;
		if(sortTitle != null)
			searchValue += "&sortTitle="+sortTitle;
		else if(sortYear != null)
			searchValue += "&sortYear="+sortYear;

	      //NUM PER PAGE DROP DOWN
	      out.println("Showing <select onchange=\"location = this.value;\">" +
				"<option selected disabled hidden>"+numPerPage+"</option>" +
				"<option value=\"/Fabflix/search?"+searchValue+"&numPerPage=10\">10</option>" +
				"<option value=\"/Fabflix/search?"+searchValue+"&numPerPage=25\">25</option>" +
				"<option value=\"/Fabflix/search?"+searchValue+"&numPerPage=50\">50</option>" +
				"<option value=\"/Fabflix/search?"+searchValue+"&numPerPage=100\">100</option>" +
			  "</select> Results per page.");
		searchValue += "&numPerPage="+numPerPage;
		sortSearchValue += "&numPerPage="+numPerPage;

	      //CHANGE SORT VALUES FOR WHEN THE SORT LINKS GET CLICKED AGAIN
	      if(sortTitle == null || "DESC".equals(sortTitle))
		sortTitle = "ASC";
	      else if(sortTitle.equals("ASC"))
		sortTitle = "DESC";
	      if(sortYear == null || "DESC".equals(sortYear))
		sortYear = "ASC";
	      else if(sortYear.equals("ASC"))
		sortYear = "DESC";

	      //TABLE HEADER WITH SORT LINKS
              out.println("<br><TABLE border><tr>" +
			  "<td>ID</td>" +
			  "<td><a href=\"/Fabflix/search?sortTitle="+sortTitle+"&"+sortSearchValue+"\">Title</a></td>" +
			  "<td>Director</td>" +
			  "<td><a href=\"/Fabflix/search?sortYear="+sortYear+"&"+sortSearchValue+"\">Year</a></td>" +
			  "<td>Star</td>" +
			  "<td>Genre</td>" +
			  "</tr>");
	      
	      rs.beforeFirst();	
	      int max = Integer.parseInt(numPerPage);
	      int currentPage = Integer.parseInt(page);
	      int last = max * currentPage;
	      int first = last - max + 1;
	      int count = 0;
	      Set<String> movieIDSet = new HashSet<String>();
              while (rs.next())
              {
                String m_ID = rs.getString("movie_id");
		if(movieIDSet.contains(m_ID))
			continue;
                String m_T = rs.getString("title");
                String m_D = rs.getString("director");
                String m_YR = rs.getString("year");

		count++;
		if(count >= first && count <= last)
			out.println("<tr><td>" + m_ID + "</td>" +
                              "<td>"+formMovie+"<button type=\"SUBMIT\" " + style + " value=\"" + m_ID + "\" name=\"movieID\" " +
			      "onmouseover=\"popUp(this)\"  id=\"movieWindow\">" + m_T +  "</button></form></td>" +
			      
                              "<td>" + m_D + "</td>" + 
			      "<td>" + m_YR + "</td>" + starDict.get(m_ID) + genreDict.get(m_ID) + "</tr>");
		movieIDSet.add(m_ID);
              }
              out.println("</TABLE> Page: ");

		//PAGINATION
		if(currentPage != 1)
			out.println("<a href=\"/Fabflix/search?"+searchValue+"&page="+(currentPage-1)+"\">Prev</a>");
		int numOfPages;
		if(movieIDSet.size() % max == 0)
			numOfPages = movieIDSet.size() / max;
		else
			numOfPages = movieIDSet.size() / max + 1;
		for(int i = 1; i <= numOfPages; i++){
			if(currentPage == i)
				out.println(i);
			else
				out.println("<a href=\"/Fabflix/search?"+searchValue+"&page="+i+"\">" + i + "</a>");
		}
		if(currentPage != numOfPages)
			out.println("<a href=\"/Fabflix/search?"+searchValue+"&page="+(currentPage+1)+"\">Next</a>");

              rs.close();
              statement.close();
              dbcon.close();

		}
            }
	//END OF PROGRAM
	
        catch (SQLException ex) {
              while (ex != null) {
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


