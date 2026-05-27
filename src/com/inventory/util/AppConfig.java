package com.inventory.util;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 *
 * @author Dearclaudia
 */
public class AppConfig {

    private static final Map<String, String> ENV_KEYS = new HashMap<>();

    static {
        ENV_KEYS.put("db.host", "DB_HOST");
        ENV_KEYS.put("db.port", "DB_PORT");
        ENV_KEYS.put("db.name", "DB_NAME");
        ENV_KEYS.put("db.user", "DB_USER");
        ENV_KEYS.put("db.password", "DB_PASSWORD");
    }

    private static final Properties props = new Properties();

    private static final String CONFIG_IN_PACKAGE = "com/inventory/config/config.properties";
    private static final String CONFIG_IN_RESOURCES = "config.properties";

    static {
        try (InputStream input = openConfigStream()) {
            if (input != null) {
                props.load(input);
            } else {
                System.err.println(
                        "config.properties tidak ditemukan. "
                        + "Letakkan di src/com/inventory/config/ lalu Clean and Build.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static InputStream openConfigStream() {
        ClassLoader loader = AppConfig.class.getClassLoader();
        InputStream input = loader.getResourceAsStream(CONFIG_IN_PACKAGE);
        if (input == null) {
            input = loader.getResourceAsStream(CONFIG_IN_RESOURCES);
        }
        return input;
    }

    public static String get(String key) {
        String envKey = ENV_KEYS.get(key);
        if (envKey != null) {
            String envValue = System.getenv(envKey);
            if (envValue != null && !envValue.isBlank()) {
                return envValue.trim();
            }
        }
        return props.getProperty(key);
    }
}
