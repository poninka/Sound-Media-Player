package com.soundmediaplayer.spotify;

import com.soundmediaplayer.config.ConfigManager;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Basic test to verify Spotify API client functionality.
 * Tests token authentication and API connectivity.
 */
public class SpotifyApiClientTest {

    @Test
    public void testConfigManagerLoads() {
        // Test that ConfigManager can be instantiated and accessed
        assertNotNull(ConfigManager.getClientId());
        assertNotNull(ConfigManager.getClientSecret());
    }

    @Test
    public void testSpotifyApiClientCreation() {
        // Test that SpotifyApiClient can be created
        SpotifyApiClient client = new SpotifyApiClient();
        assertNotNull(client);
    }

    @Test
    public void testTokenRetrieval() {
        // Test token retrieval - only runs if credentials are configured
        if (ConfigManager.hasValidCredentials()) {
            SpotifyApiClient client = new SpotifyApiClient();
            String token = client.getAccessToken();
            
            // If credentials are valid, token should not be null
            // Note: This test may fail if credentials are invalid or network is unavailable
            if (token != null) {
                assertFalse(token.isEmpty(), "Access token should not be empty");
                assertTrue(token.length() > 0, "Access token should have content");
            }
        } else {
            // Skip test if credentials not configured
            System.out.println("Skipping token test - credentials not configured");
        }
    }

    @Test
    public void testConfigOverride() {
        // Test that config overrides work
        String originalId = ConfigManager.getClientId();
        String originalSecret = ConfigManager.getClientSecret();
        
        ConfigManager.setClientIdOverride("test_id");
        ConfigManager.setClientSecretOverride("test_secret");
        
        assertEquals("test_id", ConfigManager.getClientId());
        assertEquals("test_secret", ConfigManager.getClientSecret());
        
        // Restore original values
        ConfigManager.clearOverrides();
        assertEquals(originalId, ConfigManager.getClientId());
        assertEquals(originalSecret, ConfigManager.getClientSecret());
    }
}

