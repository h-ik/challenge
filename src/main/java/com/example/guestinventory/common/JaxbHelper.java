package com.example.guestinventory.common;

import java.io.ByteArrayInputStream;
import java.io.OutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

public class JaxbHelper {

	private static JAXBContext CONTEXT = null;

	static {

		try {
			CONTEXT = JAXBContext
					.newInstance("com.example.guestinventory.subscription");
		} catch (JAXBException e) {
			throw new RuntimeException(e);
		}

	}

	@SuppressWarnings("unchecked")
	public static <T> T read(byte[] in, Class<T> class1) throws JAXBException {

		Unmarshaller unmarshaller = CONTEXT.createUnmarshaller();
		return (T) unmarshaller.unmarshal(new ByteArrayInputStream(in));
	}

	public static void write(Object object, OutputStream out)
			throws JAXBException {

		Marshaller marshaller = CONTEXT.createMarshaller();
		marshaller.marshal(object, out);
	}
}
