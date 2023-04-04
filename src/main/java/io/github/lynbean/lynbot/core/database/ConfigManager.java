package io.github.lynbean.lynbot.core.database;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ConfigManager {
    private volatile static Map<String, String> configMap = new HashMap<>();

    public static void registerProperties(Properties properties) {
        for (String key : properties.stringPropertyNames()) {
            if (configMap.putIfAbsent(key, properties.getProperty(key)) != null) {
                throw new IllegalArgumentException("Property with key " + key + " already exists");
            }
        }
    }

    public static Map<String, String> getConfigMap() {
        return Collections.unmodifiableMap(configMap);
    }

    public static String get(String key) {
        if (!configMap.containsKey(key)) {
            throw new IllegalArgumentException("Property with key " + key + " does not exist");
        }
        if (configMap.get(key).strip().length() == 0) {
            return null;
        }
        return configMap.get(key).strip();
    }

    public static String get(String key, String defaultValue) {
        return configMap.getOrDefault(key, defaultValue).strip();
    }
}