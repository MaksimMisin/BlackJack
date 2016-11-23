package com.github.maksmshn.blackjack_server;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A singleton properties class. The code adopted from
 * https://github.com/zdata-inc/StormSampleProject
 *
 */
public class Properties {
	private final Logger logger = LoggerFactory.getLogger(getClass());

	private static Properties properties;
	private Configuration config;
	private final String configurationFileName = "/config.properties";

	private Properties() {
		try {
			this.config = new PropertiesConfiguration(this.getClass()
					.getResource(configurationFileName));
			logger.debug("Configuration file {} loaded.", configurationFileName);
		} catch (Exception ex) {
			logger.warn("Failed to load configuration file {}. Ignoring it.",
					configurationFileName, ex);
		}
	}

	private static Properties get() {
		if (properties == null) {
			properties = new Properties();
		}
		return properties;
	}

	public static boolean getBoolean(String key) {
		return get().config.getBoolean(key);
	}

	public static double getDouble(String key) {
		return get().config.getDouble(key);
	}
}
