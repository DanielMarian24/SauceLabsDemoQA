package com.mydemo.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {
    private static final Properties props = new Properties();

    static {
        try (InputStream is = ConfigReader.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (is == null) {
                throw new IllegalStateException("config.properties not found on classpath");
            }
            props.load(is);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config.properties", e);
        }
    }

    public static String get(String key) {
        // env var override: dots -> underscores, upper case (e.g. sauce.app.storage -> SAUCE_APP_STORAGE)
        // plus direct secrets SAUCE_USERNAME / SAUCE_ACCESS_KEY handled in DriverFactory
        String envKey = key.toUpperCase().replace('.', '_').replace('-', '_');
        String envVal = System.getenv(envKey);
        if (envVal != null && !envVal.isBlank()) {
            return envVal;
        }
        String sysVal = System.getProperty(key);
        if (sysVal != null) {
            return sysVal;
        }
        return props.getProperty(key);
    }

    public static int getInt(String key, int def) {
        try {
            return Integer.parseInt(get(key));
        } catch (Exception e) {
            return def;
        }
    }
}
