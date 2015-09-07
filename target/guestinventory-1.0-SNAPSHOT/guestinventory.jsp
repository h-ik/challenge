<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ page import="com.google.appengine.api.users.User"%>
<%@ page import="com.google.appengine.api.users.UserService"%>
<%@ page import="com.google.appengine.api.users.UserServiceFactory"%>
<%@ page import="java.util.List"%>
<%@ page import="com.example.guestinventory.Greeting"%>
<%@ page import="com.example.guestinventory.Guestinventory"%>
<%@ page import="com.googlecode.objectify.Key"%>
<%@ page import="com.googlecode.objectify.ObjectifyService"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<html>
<head>
<link type="text/css" rel="stylesheet" href="/stylesheets/main.css" />
</head>

<body>

	<%
		String guestinventoryName = request
				.getParameter("guestinventoryName");
		if (guestinventoryName == null) {
			guestinventoryName = "default";
		}
		pageContext.setAttribute("guestinventoryName", guestinventoryName);
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		if (user != null) {
			pageContext.setAttribute("user", user);
	%>

	<p>
		Hello, ${fn:escapeXml(user.nickname)}! (You can <a
			href="<%=userService.createLogoutURL(request.getRequestURI())%>">sign
			out</a>.)
	</p>
	<%
		} else {
	%>
	<p>
		Hello! <a
			href="<%=userService.createLoginURL(request.getRequestURI())%>">Sign
			in</a> to include your name with greetings you post.
	</p>
	<%
		}
	%>

	<%
		// Create the correct Ancestor key
		Key<Guestinventory> theinventory = Key.create(Guestinventory.class,
				guestinventoryName);

		// Run an ancestor query to ensure we see the most up-to-date
		// view of the Greetings belonging to the selected Guestinventory.
		List<Greeting> greetings = ObjectifyService.ofy().load()
				.type(Greeting.class) // We want only Greetings
				.ancestor(theinventory) // Anyone in this inventory
				.order("-date") // Most recent first - date is indexed.
				.limit(5) // Only show 5 of them.
				.list();

		if (greetings.isEmpty()) {
	%>
	<p>Guestinventory '${fn:escapeXml(guestinventoryName)}' has no
		messages.</p>
	<%
		} else {
	%>
	<p>Messages in Guestinventory
		'${fn:escapeXml(guestinventoryName)}'.</p>
	<%
		// Look at all of our greetings
			for (Greeting greeting : greetings) {
				pageContext.setAttribute("greeting_content",
						greeting.content);
				String author;
				if (greeting.author_email == null) {
					author = "An anonymous person";
				} else {
					author = greeting.author_email;
					String author_id = greeting.author_id;
					if (user != null && user.getUserId().equals(author_id)) {
						author += " (You)";
					}
				}
				pageContext.setAttribute("greeting_user", author);
	%>
	<p>
		<b>${fn:escapeXml(greeting_user)}</b> wrote:
	</p>
	<blockquote>${fn:escapeXml(greeting_content)}</blockquote>
	<%
		}
		}
	%>

	<form action="/sign" method="post">
		<div>
			<textarea name="content" rows="3" cols="60"></textarea>
		</div>
		<div>
			<input type="submit" value="Post Greeting" />
		</div>
		<input type="hidden" name="guestinventoryName"
			value="${fn:escapeXml(guestinventoryName)}" />
	</form>

	<form action="/guestinventory.jsp" method="get">
		<div>
			<input type="text" name="guestinventoryName"
				value="${fn:escapeXml(guestinventoryName)}" />
		</div>
		<div>
			<input type="submit" value="Switch Guestinventory" />
		</div>
	</form>

</body>
</html>