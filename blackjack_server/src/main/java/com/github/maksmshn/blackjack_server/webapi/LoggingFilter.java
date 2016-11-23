package com.github.maksmshn.blackjack_server.webapi;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A simple response and request filter that monitors communication
 * between server and client and logs all requests/responses.
 * Expects JSON input/output.
 *
 */
@Provider
public class LoggingFilter implements ContainerResponseFilter,
		ContainerRequestFilter {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	/** Should be smaller than the size of */
	private static int maxRequestSize = 1000;
	/** Magic injection!*/
	@Context
    private HttpServletRequest sr;

	/**
	 * Intercept client request and read it. Code from here:
	 * https://github.com/jersey/jersey/blob/master/core-common/src/main/java/org/glassfish/jersey/logging/LoggingInterceptor.java
	 */
	private String convertRequestToString(ContainerRequestContext requestContext) {
		StringBuilder builder = new StringBuilder();
		InputStream stream = requestContext.getEntityStream();
		// We want to read ahead a little and then reset the stream.
		// To do it we are using mark/reset method.
		if (!stream.markSupported()) {
			stream = new BufferedInputStream(stream);
		}
		stream.mark(maxRequestSize + 1);
		byte[] entity = new byte[maxRequestSize + 1];
		int entitySize = 0;
		try {
			entitySize = stream.read(entity);
		} catch (IOException e) {
			logger.warn("Logging filter couldn't read request {} body",
					requestContext, e);
		}
		builder.append(new String(entity, 0, Math.min(entitySize,
				maxRequestSize)));
		if (entitySize > maxRequestSize) {
			builder.append("...more...");
		}
		builder.append('\n');
		try {
			stream.reset();
		} catch (IOException e) {
			logger.warn("Logging filter couldn't reset request {} stream",
					requestContext, e);
		}
		// Put stream back
		requestContext.setEntityStream(stream);
		return builder.toString();
	}

	@Override
	public void filter(ContainerRequestContext requestContext,
			ContainerResponseContext responseContext) {
		// We are not printing client request here.
		logger.info("Server response headers: {}", responseContext.getHeaders());
		if (responseContext.hasEntity()) {
			logger.info("Server response body: {}", responseContext.getEntity());
		}

	}

	@Override
	public void filter(ContainerRequestContext requestContext)
			throws IOException {
		logger.info("Client IP: {}", sr.getRemoteAddr());
		logger.info("Client request method: {}", requestContext.getMethod());
		logger.info("Client request URI: {}",
				requestContext.getUriInfo().getAbsolutePath());
		logger.info("Client request headers: {}", requestContext.getHeaders());
		if (requestContext.hasEntity()) {
			String body = convertRequestToString(requestContext);
			logger.info("Client request body: {}", body);
		}
	}

}
