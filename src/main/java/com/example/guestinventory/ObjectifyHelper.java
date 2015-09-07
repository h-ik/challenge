package com.example.guestinventory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.googlecode.objectify.ObjectifyService;

/**
 * ObjectifyHelper, a ServletContextListener, is setup in web.xml to run before
 * a JSP is run. This is required to let JSP's access Objectify.
 **/
public class ObjectifyHelper implements ServletContextListener {
	public void contextInitialized(ServletContextEvent event) {
		// This will be invoked as part of a warmup request, or the first user
		// request if no warmup request.
		ObjectifyService.register(Guestinventory.class);
		ObjectifyService.register(Greeting.class);
	}

	public void contextDestroyed(ServletContextEvent event) {
		// App Engine does not currently invoke this method.
	}
}