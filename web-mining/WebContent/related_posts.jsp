<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.*"%>
<%@ page import="java.sql.*"%>
<%@ page import="controller.DatabaseHandler"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="shortcut icon" type="image/png" href="favicon.ico" />
<!--Import Google Icon Font-->
<link href="http://fonts.googleapis.com/icon?family=Material+Icons"
	rel="stylesheet">
<!--Import materialize.css-->
<link type="text/css" rel="stylesheet" href="css/materialize.min.css"
	media="screen,projection" />

<!--Let browser know website is optimized for mobile-->
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<title>Blog Mining</title>
</head>
<body>
	<div class="container">
		<!-- Page Content goes here -->

		<div class="row">

			<div class="section">
				<h4>Related Blogs</h4>
				<br> <a href="index.jsp">back</a>
			</div>



			<ul class="collection">

				<%
					//create a database handler
					DatabaseHandler dbh = new DatabaseHandler();
					Integer postID = Integer.parseInt(request.getParameter("postid"));
					Integer siteID = Integer.parseInt(request.getParameter("siteid"));
					ResultSet resultSet = dbh.getRelatedPostsForWeb(postID, siteID);
					while (resultSet.next()) {
				%>

				<li class="collection-item avatar"><i
					class="material-icons circle red">insert_chart</i> <span
					class="title">
						<h5>
							<%
								out.println(resultSet.getString("post_title").replaceAll("[\\<|\\?]", " "));
							%>

						</h5>
				</span>
					<p>
						Likes:
						<%
						out.println(resultSet.getInt("likes_count"));
					%><br>
					<p>
						Comments:
						<%
						out.println(resultSet.getInt("comments_count"));
					%><br> <a href="#"> <font size="3" color="red">Related
								Posts</font></a> <br>
					</p> <a href="<%out.println(resultSet.getString("post_url"));%>"
					target="_blank" class="secondary-content"><i
						class="material-icons">send</i></a></li>
				<%
					}
				%>
			</ul>
		</div>
	</div>
</body>
</html>