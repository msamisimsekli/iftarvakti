package com.kayalar.iftarvakti.config;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigurationReader {

	private InputStream inputStream;

	public Configurations getPropValues() throws IOException {

		Properties prop = new Properties();
		String propFileName = "config.properties";

		inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);

		if (inputStream != null) {
			prop.load(inputStream);
		} else {
			throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
		}

		Configurations config = new Configurations();

		config.setBotToken(prop.getProperty("bot.token"));
		config.setBotUserId(prop.getProperty("bot.user.id"));
		config.setDbAdress(prop.getProperty("db.address"));
		config.setDbName(prop.getProperty("db.name"));
		config.setDbPort(Integer.parseInt(prop.getProperty("db.port")));
		config.setApiToken(prop.getProperty("api.token"));
		config.setApiAddress(prop.getProperty("api.address"));

		inputStream.close();

		return config;
	}
}
