package com.example.guestinventory.subscription;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.xml.bind.JAXBException;

import org.jboss.resteasy.annotations.Suspend;
import org.jboss.resteasy.spi.AsynchronousResponse;

import com.example.guestinventory.common.JaxbHelper;
import com.google.appengine.api.urlfetch.HTTPResponse;
import com.google.appengine.api.urlfetch.URLFetchService;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;

@Path("/subscription")
public class SubscriptionResource {

	private static final String APPLICATION_XML = "application/xml";

	enum SubscriptionEvent {
		ORDER("SUBSCRIPTION_ORDER"), CHANGE("SUBSCRIPTION_CHANGE"), CANCEL(
				"SUBSCRIPTION_CANCEL"), NOTICE("SUBSCRIPTION_NOTICE");

		private String value;

		private SubscriptionEvent(String value) {
			this.value = value;
		}

		public static boolean isOrder(String type) {
			return valueOf(type) == ORDER;
		}

		public static boolean isChange(String type) {
			return valueOf(type) == CHANGE;
		}

		public static boolean isCancel(String type) {
			return valueOf(type) == CANCEL;
		}

		public static boolean isStatus(String type) {
			return valueOf(type) == NOTICE;
		}

	}

	enum Operation {
		CREATE, CHANGE, CANCEL, STATUS
	}

	@POST
	@Path("/create")
	@Produces(APPLICATION_XML)
	public void create(
			@Suspend(10000) final AsynchronousResponse asynchResponse,
			@PathParam("url") String stringUrl) {

		handleOperation(Operation.CREATE, asynchResponse, stringUrl);
	}

	private void handleOperation(Operation o,
			final AsynchronousResponse asynchResponse, String stringUrl) {
		// TODO verify the OAuth signature

		URLFetchService fetchService = URLFetchServiceFactory
				.getURLFetchService();
		try {
			Future<HTTPResponse> fetchResponse = fetchService
					.fetchAsync(new URL(stringUrl));

			Event event = JaxbHelper.read(fetchResponse.get().getContent(),
					Event.class);
			handleEvent(o, event, asynchResponse);
		} catch (MalformedURLException | InterruptedException
				| ExecutionException | JAXBException e) {
			// TODO log exception

			EventNotificationResult result = EventNotificationResult.failure();
			asynchResponse.setResponse(Response.ok(result).build());
		}
	}

	private void handleEvent(Operation o, Event event,
			AsynchronousResponse asynchResponse) {

		ResponseBuilder responseBuilder = null;

		switch (o) {
		case CREATE:

			if (SubscriptionEvent.isOrder(event.getType())) {
				// TODO store new user locally

				String accountIdentifier = "hjnoi";
				EventNotificationResult result = EventNotificationResult
						.success(accountIdentifier);
				responseBuilder = Response.ok(result);
			}
			break;
		case CHANGE:
			if (SubscriptionEvent.isChange(event.getType())) {
				responseBuilder = Response
						.ok(EventNotificationResult.success());
			}
			break;
		case CANCEL:
			if (SubscriptionEvent.isCancel(event.getType())) {
				// TODO remove user
				String accountIdentifier = event.getPayload().getAccount();

				responseBuilder = Response
						.ok(EventNotificationResult.success());
			}
			break;
		case STATUS:
			if (SubscriptionEvent.isStatus(event.getType())) {
				responseBuilder = Response
						.ok(EventNotificationResult.success());
			}
			break;
		default:
			break;

		}
		if (responseBuilder == null) {
			EventNotificationResult result = EventNotificationResult.failure();
			responseBuilder = Response.ok(result);
		}
		asynchResponse.setResponse(responseBuilder.build());
	}

	@POST
	@Path("/change")
	@Produces(APPLICATION_XML)
	public void change(
			@Suspend(10000) final AsynchronousResponse asynchResponse,
			@PathParam("url") String stringUrl) {

		handleOperation(Operation.CHANGE, asynchResponse, stringUrl);
	}

	@POST
	@Path("/cancel")
	@Produces(APPLICATION_XML)
	public void cancel(
			@Suspend(10000) final AsynchronousResponse asynchResponse,
			@PathParam("url") String stringUrl) {

		handleOperation(Operation.CANCEL, asynchResponse, stringUrl);

	}

	@POST
	@Path("/status")
	@Produces(APPLICATION_XML)
	public void notice(
			@Suspend(10000) final AsynchronousResponse asynchResponse,
			@PathParam("url") String stringUrl) {

		handleOperation(Operation.STATUS, asynchResponse, stringUrl);

	}

}
