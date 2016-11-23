package com.github.maksmshn.blackjack_client.web_game.model;

import java.util.Date;

import javax.ws.rs.core.Response.Status;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ErrorMessage {
	public Date timestamp;
	public Status responseStatus;
	public int statusCode;
	public String errorMessage;

	public ErrorMessage() {
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public Status getResponseStatus() {
		return responseStatus;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	@Override
	public String toString() {
		return "ErrorMessage [timestamp=" + timestamp + ", responseStatus=" + responseStatus + ", statusCode="
				+ statusCode + ", errorMessage=" + errorMessage + "]";
	}

}
