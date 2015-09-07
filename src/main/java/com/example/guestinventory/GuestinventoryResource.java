package com.example.guestinventory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URI;
import java.util.Properties;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.annotations.Suspend;
import org.jboss.resteasy.spi.AsynchronousResponse;
import org.jboss.resteasy.spi.HttpRequest;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

@Path("/guestinventory")
public class GuestinventoryResource {
	private static final String TEXT_PLAIN = "text/plain";

	public GuestinventoryResource() {
	}

	@GET
	@Produces(TEXT_PLAIN)
	public void doGet(@Context HttpRequest request,
			@QueryParam("testing") String param,
			@Suspend(10000) final AsynchronousResponse asynchResponse)
			throws IOException {

		if (param == null) {

			ByteArrayOutputStream os = new ByteArrayOutputStream();
			PrintStream ps = new PrintStream(os);
			Properties p = System.getProperties();
			p.list(ps);

			StringBuilder sb = new StringBuilder();
			sb.append("Hello, this is a testing servlet. \n\n");
			sb.append(os.toString("UTF8"));
			asynchResponse.setResponse(Response.ok(sb.toString()).build());

		} else {

			UserService userService = UserServiceFactory.getUserService();
			User currentUser = userService.getCurrentUser();

			if (currentUser != null) {
				asynchResponse.setResponse(Response.ok(
						"Hello, " + currentUser.getNickname()).build());

			} else {
				String loginURL = userService.createLoginURL(request.getUri()
						.getAbsolutePath().getPath());
				asynchResponse.setResponse(Response.seeOther(
						URI.create(loginURL)).build());
			}
		}
	}
}
