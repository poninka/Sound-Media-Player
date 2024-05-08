package se.michaelthelin.spotify;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.util.Scanner;

public class SongDataFetcher {

    private static SpotifyApiClient spotifyApiClient;
    private static SpotifySongSearcher spotifySongSearcher;

    public SongDataFetcher() {
        spotifyApiClient = new SpotifyApiClient();
        spotifySongSearcher = new SpotifySongSearcher();
    }

    public void fetchAndDisplaySongInfo(String songName) {
        String accessToken = spotifyApiClient.getAccessToken();
        String trackId = spotifySongSearcher.searchSong(accessToken, songName);

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
                System.out.println(" Title: " + title + "\n Artist: " +  artist + "\n Image: " + coverArtPath + "\n Duration: " + songLength + "s");
            } else {
                System.out.println("Failed to fetch track data from Spotify API.");
            }
        } else {
            System.out.println("Failed to get access token from Spotify API.");
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
