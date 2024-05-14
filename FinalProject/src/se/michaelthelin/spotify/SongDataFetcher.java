package se.michaelthelin.spotify;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.awt.Desktop;
import java.net.URI;
import java.util.Scanner;

public class SongDataFetcher {

    // Static fields for Spotify API client and song searcher
    private static SpotifyApiClient spotifyApiClient;
    private static SpotifySongSearcher spotifySongSearcher;

    // Contstructor to initialize the Spotify API client and song searcher
    public SongDataFetcher() {
        spotifyApiClient = new SpotifyApiClient();
        spotifySongSearcher = new SpotifySongSearcher();
    }

    // Method to fetch and display song information
    public void fetchAndDisplaySongInfo(String songName) {
        // Get Access token from Spotify API Client
        String accessToken = spotifyApiClient.getAccessToken();

        // Search for the song using the access token and song name
        String trackId = spotifySongSearcher.searchSong(accessToken, songName);

        if (accessToken != null) {
            // Get track data using the acces token and track ID
            String trackData = spotifyApiClient.getTrackData(accessToken, trackId);
            if (trackData != null) {
                // Parse trackData to extract song information
                Gson gson = new Gson();
                JsonObject trackJson = gson.fromJson(trackData, JsonObject.class);

                // Extract song details from the JSON object
                String title = trackJson.get("name").getAsString();
                String artist = trackJson.getAsJsonArray("artists").get(0).getAsJsonObject().get("name").getAsString();
                String coverArtPath = trackJson.getAsJsonObject("album").getAsJsonArray("images").get(0).getAsJsonObject().get("url").getAsString();
                int songLength = trackJson.get("duration_ms").getAsInt() / 1000; // converting milliseconds to seconds

                // Display the song information
                System.out.println(" Title: " + title + "\n Artist: " +  artist + "\n Image: " + coverArtPath + "\n Duration: " + songLength + "s");
            } else {
                System.out.println("Failed to fetch track data from Spotify API.");
            }
        } else {
            System.out.println("Failed to get access token from Spotify API.");
        }
        
        try {
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                Desktop.getDesktop().browse(new URI("https://open.spotify.com/track/" + trackId));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String args[]) {
        SongDataFetcher songDataFetcher = new SongDataFetcher();

        // Ask the user for a song name
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter a song name to search for:");
        String songName = scanner.nextLine();

        songDataFetcher.fetchAndDisplaySongInfo(songName);
    }
}
