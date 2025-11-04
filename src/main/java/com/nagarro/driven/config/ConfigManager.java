package com.nagarro.driven.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Properties;

public class ConfigManager {
    private static final Properties props = new Properties();

    static {
        String env = System.getProperty("environment", System.getenv("ENVIRONMENT"));
        if (env == null || env.isEmpty()) {
            env = "qa";
        }

        String configDir = System.getProperty(
                "config.path",
                System.getenv("CONFIG_PATH") != null
                        ? System.getenv("CONFIG_PATH")
                        : "src/main/resources/config"
        );

        String filePath = Paths.get(configDir, env + ".properties").toString();

        try (FileInputStream fis = new FileInputStream(filePath)) {
            props.load(fis);
            System.out.println("✅ Loaded configuration from: " + filePath);
        } catch (IOException e) {
            throw new RuntimeException("❌ Failed to load config for environment: " + env + " from " + filePath, e);
        }
    }

    public static String get(String key) {
        String value = System.getProperty(key, System.getenv(key.toUpperCase()));
        if (value == null || value.isEmpty()) {
            value = props.getProperty(key);
        }

        if (value == null || value.isEmpty()) {
            throw new RuntimeException("❌ Missing property: " + key);
        }
        return value;
    }
}
