package com.github.maksmshn.blackjack_server.webapi;

import java.util.Date;

import javax.ws.rs.core.Response;
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

	ErrorMessage(Status status, String errorMessage) {
		this.timestamp = new Date();
		this.responseStatus = status;
		this.statusCode = status.getStatusCode();
		this.errorMessage = errorMessage;
	}

	ErrorMessage(Exception e) {
		this.timestamp = new Date();
		this.responseStatus = Status.BAD_REQUEST;
		this.statusCode = responseStatus.getStatusCode();
		this.errorMessage = e.getMessage();
	}

	Response toResponse() {
		return Response.status(responseStatus).entity(this).build();
	}

	@Override
	public String toString() {
		return "ErrorMessage [timestamp=" + timestamp + ", responseStatus=" + responseStatus + ", statusCode="
				+ statusCode + ", errorMessage=" + errorMessage + "]";
	}

}
