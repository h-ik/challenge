package com.example.guestinventory;

import java.io.IOException;
import java.net.URI;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.annotations.Suspend;
import org.jboss.resteasy.spi.AsynchronousResponse;
import org.jboss.resteasy.spi.HttpRequest;

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
@Path("/guestinventory/sign")
public class SignGuestinventoryResource {
	private static final String TEXT_PLAIN = "text/plain";

	public SignGuestinventoryResource() {
	}

	// Process the http POST of the form
	@POST
	@Produces(TEXT_PLAIN)
	public void doPost(@Context HttpRequest request,
			@FormParam("guestinventoryName") String guestinventoryName,
			@FormParam("content") String content,
			@Suspend(10000) final AsynchronousResponse asynchResponse)
			throws IOException {

		Greeting greeting;

		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser(); // Find out who the user is.

		if (user != null) {
			greeting = new Greeting(guestinventoryName, content,
					user.getUserId(), user.getEmail());
		} else {
			greeting = new Greeting(guestinventoryName, content);
		}

		// Use Objectify to save the greeting and now() is used to make the call
		// synchronously as we will immediately get a new page using redirect
		// and we want the data to be present.
		ObjectifyService.ofy().save().entity(greeting).now();

		asynchResponse.setResponse(Response.seeOther(
				URI.create("/guestinventory.jsp?guestinventoryName="
						+ guestinventoryName)).build());
	}
}
