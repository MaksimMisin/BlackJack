package com.github.maksmshn.blackjack_server;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

/**
 * The code in this class is initialised before Jersey application starts.
 */
@ApplicationPath("/webapi")
public class BlackJackConfig extends ResourceConfig {
	private final Logger logger = LoggerFactory.getLogger(getClass());

	public BlackJackConfig() {
		logger.info(">>> Application startup");
		bridgeJulToSlf4J();
	}

	/**
	 * Jersey uses java.util.logging. Bridge jul to slf4j code from:
	 * https://github.com/leifoolsen/jersey2-guicepersist-example
	 */
	private static void bridgeJulToSlf4J() {
		java.util.logging.LogManager.getLogManager().reset();
		SLF4JBridgeHandler.removeHandlersForRootLogger();
		SLF4JBridgeHandler.install();
		java.util.logging.Logger.getGlobal().setLevel(
				java.util.logging.Level.FINEST);
		java.util.logging.Logger.getLogger("org.glassfish.jersey").setLevel(
				java.util.logging.Level.FINEST);

	}
}
