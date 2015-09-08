package com.example.guestinventory.common;

import java.io.ByteArrayInputStream;
import java.io.OutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

public class JaxbHelper {

	private static final String PACKAGES = "com.example.guestinventory.subscription"
			+ ":com.example.guestinventory.subscription.error"
			+ ":com.example.guestinventory.subscription.result";
	private static JAXBContext CONTEXT = null;

	static {

		try {
			CONTEXT = JAXBContext.newInstance(PACKAGES);
		} catch (JAXBException e) {
			throw new RuntimeException(e);
		}

	}

	@SuppressWarnings("unchecked")
	public static Object read(byte[] in) throws JAXBException {

		Unmarshaller unmarshaller = CONTEXT.createUnmarshaller();
		return unmarshaller.unmarshal(new ByteArrayInputStream(in));
	}

	public static void write(Object object, OutputStream out)
			throws JAXBException {

		Marshaller marshaller = CONTEXT.createMarshaller();
		marshaller.marshal(object, out);
	}
}
