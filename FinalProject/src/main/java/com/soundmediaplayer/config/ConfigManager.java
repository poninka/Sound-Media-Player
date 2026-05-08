package com.soundmediaplayer.config;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Manages configuration properties for the application.
 * Supports loading from config.properties file and runtime overrides.
 */
public class ConfigManager {
    private static final String CONFIG_FILE = "config.properties";
    private static final String CONFIG_EXAMPLE = "config.properties.example";
    private static Properties properties;
    private static String overrideClientId;
    private static String overrideClientSecret;

    static {
        loadProperties();
    }

    /**
     * Loads properties from config.properties file.
     * Falls back to example file if config.properties doesn't exist.
     * Also tries loading from project root directory.
     */
    private static void loadProperties() {
        properties = new Properties();
        
        // First try loading from resources
        try (InputStream input = ConfigManager.class.getClassLoader()
                .getResourceAsStream(CONFIG_FILE)) {
            if (input != null) {
                properties.load(input);
                return;
            }
        } catch (IOException e) {
            // Continue to try other locations
        }
        
        // Try loading from project root
        try {
            java.nio.file.Path configPath = java.nio.file.Paths.get(CONFIG_FILE);
            if (java.nio.file.Files.exists(configPath)) {
                try (InputStream input = new java.io.FileInputStream(configPath.toFile())) {
                    properties.load(input);
                    return;
                }
            }
        } catch (IOException e) {
            // Continue to try example file
        }
        
        // Fall back to example file from resources
        try (InputStream exampleInput = ConfigManager.class.getClassLoader()
                .getResourceAsStream(CONFIG_EXAMPLE)) {
            if (exampleInput != null) {
                properties.load(exampleInput);
            }
        } catch (IOException e) {
            System.err.println("Error loading config files: " + e.getMessage());
        }
    }

    /**
     * Gets the Spotify client ID, checking override first, then properties file.
     */
    public static String getClientId() {
        if (overrideClientId != null && !overrideClientId.isEmpty()) {
            return overrideClientId;
        }
        return properties.getProperty("spotify.client.id", "");
    }

    /**
     * Gets the Spotify client secret, checking override first, then properties file.
     */
    public static String getClientSecret() {
        if (overrideClientSecret != null && !overrideClientSecret.isEmpty()) {
            return overrideClientSecret;
        }
        return properties.getProperty("spotify.client.secret", "");
    }

    /**
     * Sets runtime override for client ID.
     */
    public static void setClientIdOverride(String clientId) {
        overrideClientId = clientId;
    }

    /**
     * Sets runtime override for client secret.
     */
    public static void setClientSecretOverride(String clientSecret) {
        overrideClientSecret = clientSecret;
    }

    /**
     * Clears runtime overrides, reverting to properties file values.
     */
    public static void clearOverrides() {
        overrideClientId = null;
        overrideClientSecret = null;
    }

    /**
     * Checks if valid credentials are available (either from file or override).
     */
    public static boolean hasValidCredentials() {
        String id = getClientId();
        String secret = getClientSecret();
        return id != null && !id.isEmpty() && !id.equals("your_client_id_here")
                && secret != null && !secret.isEmpty() && !secret.equals("your_client_secret_here");
    }
}

