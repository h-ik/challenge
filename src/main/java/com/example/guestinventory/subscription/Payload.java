package com.example.guestinventory.subscription;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "payload")
@XmlAccessorType(XmlAccessType.FIELD)
public class Payload {

	@XmlAttribute
	private String order;

	@XmlAttribute
	private String compagny;

	@XmlAttribute
	private String account;

	@XmlAttribute
	private String notice;

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public String getCompagny() {
		return compagny;
	}

	public void setCompagny(String compagny) {
		this.compagny = compagny;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getNotice() {
		return notice;
	}

	public void setNotice(String notice) {
		this.notice = notice;
	}
}
