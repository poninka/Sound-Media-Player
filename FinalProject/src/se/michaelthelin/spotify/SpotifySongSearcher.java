package se.michaelthelin.spotify;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import okhttp3.*;

public class SpotifySongSearcher {

    // OkHttpClient instance for making HTTP requests
    private final OkHttpClient httpClient = new OkHttpClient();

    // Base API URL for Spotify
    private final String baseApiUrl = "https://api.spotify.com/v1";

    // Method to search for a song on Spotify
    public String searchSong(String accessToken, String songName) {
        try {
            // Build URL for search endpoint with query parameters
        	 HttpUrl url = HttpUrl.parse(baseApiUrl + "/search").newBuilder()
                     .addQueryParameter("q", songName)
                     .addQueryParameter("type", "track")
                     .build();

             // Create request with authorization header
             Request request = new Request.Builder()
                     .url(url)
                     .header("Authorization", "Bearer " + accessToken)
                     .build();

             // Execute request and get response body as string
             Response response = httpClient.newCall(request).execute();
             String jsonResponse = response.body().string();

             // Parse the JSON response to get the track ID
             JsonElement root = JsonParser.parseString(jsonResponse);
             String trackId = root.getAsJsonObject()
                     .get("tracks").getAsJsonObject()
                     .get("items").getAsJsonArray()
                     .get(0).getAsJsonObject()
                     .get("id").getAsString();

             return trackId;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
