package com.example.guestinventory;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.googlecode.objectify.ObjectifyService;

/**
 * Form Handling Servlet Most of the action for this sample is in
 * webapp/guestinventory.jsp, which displays the {@link Greeting}'s. This
 * servlet has one method {@link #doPost(<#HttpServletRequest req#>,
 * <#HttpServletResponse resp#>)} which takes the form data and saves it.
 */
public class SignGuestinventoryServlet extends HttpServlet {

	// Process the http POST of the form
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		Greeting greeting;

		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser(); // Find out who the user is.

		String guestinventoryName = req.getParameter("guestinventoryName");
		String content = req.getParameter("content");
		if (user != null) {
			greeting = new Greeting(guestinventoryName, content,
					user.getUserId(), user.getEmail());
		} else {
			greeting = new Greeting(guestinventoryName, content);
		}

		// Use Objectify to save the greeting and now() is used to make the call
		// synchronously as we
		// will immediately get a new page using redirect and we want the data
		// to be present.
		ObjectifyService.ofy().save().entity(greeting).now();

		resp.sendRedirect("/guestinventory.jsp?guestinventoryName="
				+ guestinventoryName);
	}
}
