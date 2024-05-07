package se.michaelthelin.spotify;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class SongDataFetcher {

    private static SpotifyApiClient spotifyApiClient;
    //private LaunchUI launchUI;

    public SongDataFetcher() {
        spotifyApiClient = new SpotifyApiClient();
        //launchUI = new LaunchUI();
    }

   public static void fetchAndDisplaySongInfo(String trackId) {

        String accessToken = spotifyApiClient.getAccessToken();

        if (accessToken != null) {
            String trackData = spotifyApiClient.getTrackData(accessToken, trackId);
            if (trackData != null) {

                // Parse trackData to extract song information
                Gson gson = new Gson();
                JsonObject trackJson = gson.fromJson(trackData, JsonObject.class);

                String title = trackJson.get("name").getAsString();
                String artist = trackJson.getAsJsonArray("artists").get(0).getAsJsonObject().get("name").getAsString();
                String coverArtPath = trackJson.getAsJsonObject("album").getAsJsonArray("images").get(0).getAsJsonObject().get("url").getAsString();
                int songLength = trackJson.get("duration_ms").getAsInt() / 1000; // converting milliseconds to seconds
                System.out.println(title + artist + coverArtPath + songLength);

                // Display song information in the UI
              //  launchUI.setSongInfo(title, artist, coverArtPath, songLength);

            } 
            else {
                System.out.println("Failed to fetch track data from Spotify API.");
            }

        } 
        else {
            System.out.println("Failed to get access token from Spotify API.");
        }
    }
   
   public static void main(String args[]) {
	   
	   fetchAndDisplaySongInfo("3n3Ppam7vgaVa1iaRUc9Lp");
   }
}