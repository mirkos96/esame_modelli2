package org.personalized.dashboard.bootstrap;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by sudan on 17/5/15.
 */
public class ConfigManager {

    private static final Properties propertyConfig = new Properties();
    private static boolean isInitialized = false;

    private ConfigManager() throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("config" +
                ".properties");

        if (inputStream == null) {
            throw new FileNotFoundException("Property files cannot be found in the classpath");
        } else {
            propertyConfig.load(inputStream);
            isInitialized = true;
        }
    }

    public static void init() throws IOException {
        if (!isInitialized)
            new ConfigManager();
    }

    public static String getValue(String key) {
        return propertyConfig.getProperty(key);
    }
}
