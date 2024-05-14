package se.michaelthelin.spotify;
import okhttp3.*;
import com.google.gson.Gson;
import java.util.Base64;
import java.util.Scanner;
 
public class SpotifyApiClient {

    // OkHttpClient instance for making HTTP requests
    private final OkHttpClient httpClient = new OkHttpClient();

    // Spotify API credentials
    private final String clientId = "658aebb78eea46db998ad183f4f23902";
    private final String clientSecret = "15b891710faa4be6abceeda7816d85c2";

    // URLs for Spotify API token and base API
    private final String tokenUrl = "https://accounts.spotify.com/api/token";
    private final String baseApiUrl = "https://api.spotify.com/v1";
 
    public static void main(String[] args) {
        SpotifyApiClient client = new SpotifyApiClient();
        String accessToken = client.getAccessToken();
        System.out.println("Access Token: " + accessToken);
    }

    // Method to get access token from Spotify API
    protected String getAccessToken() {
        try {
            // Encode client credentials
            String credentials = clientId + ":" + clientSecret;
            String encodedCredentials = Base64.getEncoder().encodeToString(credentials.getBytes());

            // Create request body with grant type
            RequestBody body = new FormBody.Builder()
                    .add("grant_type", "client_credentials")
                    .build();
            // Create request with authorization header
            Request request = new Request.Builder()
                    .url(tokenUrl)
                    .header("Authorization", "Basic " + encodedCredentials)
                    .post(body)
                    .build();

            // Execute request and parse response
            Response response = httpClient.newCall(request).execute();
            String jsonResponse = response.body().string();
            Gson gson = new Gson();
            TokenResponse tokenResponse = gson.fromJson(jsonResponse, TokenResponse.class);
            return tokenResponse.access_token;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Method to get track data from Spotify API
    protected String getTrackData(String accessToken, String trackId) {
        try {
            // Build URL for track endpoint
            HttpUrl url = HttpUrl.parse(baseApiUrl + "/tracks/" + trackId).newBuilder().build();

            // Create request with authorization header
            Request request = new Request.Builder()
                    .url(url)
                    .header("Authorization", "Bearer " + accessToken)
                    .build();

            // Execute request and return response body as string
            Response response = httpClient.newCall(request).execute();
            return response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Inner class to parse token response
    private static class TokenResponse {
        String access_token;
    }
}
