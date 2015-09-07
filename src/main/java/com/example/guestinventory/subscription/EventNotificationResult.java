package com.example.guestinventory.subscription;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "result")
public class EventNotificationResult {

	@XmlAttribute
	private boolean success;

	@XmlAttribute
	private String message;

	@XmlAttribute
	private String accountIdentifier;

	@XmlAttribute
	private String errorCode;

	public boolean getSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getAccountIdentifier() {
		return accountIdentifier;
	}

	public void setAccountIdentifier(String accountIdentifier) {
		this.accountIdentifier = accountIdentifier;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public static EventNotificationResult success() {
		EventNotificationResult result = new EventNotificationResult();
		result.setSuccess(true);
		return result;
	}

	public static EventNotificationResult success(String accountIdentifier) {
		EventNotificationResult result = success();
		result.setAccountIdentifier(accountIdentifier);
		return result;
	}

	public static EventNotificationResult failure() {
		EventNotificationResult result = new EventNotificationResult();
		result.setSuccess(false);
		return result;
	}
}
