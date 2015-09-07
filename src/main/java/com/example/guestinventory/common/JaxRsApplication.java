package com.example.guestinventory.common;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import com.example.guestinventory.subscription.SubscriptionResource;

@ApplicationPath("/")
public class JaxRsApplication extends Application {
	protected Set<Object> singletons = new HashSet<Object>();

	public JaxRsApplication() {
		singletons.add(new SubscriptionResource());
	}

	@Override
	public Set<Object> getSingletons() {
		return singletons;
	}
}

// public class JaxRsApplication {
// }