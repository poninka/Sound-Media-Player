package se.michaelthelin.spotify;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.awt.Desktop;
import java.net.URI;
import java.util.Scanner;

import javax.swing.SwingUtilities;

public class SongDataFetcher {

    private static SpotifyApiClient spotifyApiClient;
    private static SpotifySongSearcher spotifySongSearcher;
    public SongDataFetcher() {
        spotifyApiClient = new SpotifyApiClient();
        spotifySongSearcher = new SpotifySongSearcher();
    }

    public class SongInfo {
        String title;
        String artist;
        String coverArtPath;
        int songLength;

        public SongInfo(String title, String artist, String coverArtPath, int songLength) {
            this.title = title;
            this.artist = artist;
            this.coverArtPath = coverArtPath;
            this.songLength = songLength;
        }
    }

    public SongInfo fetchAndDisplaySongInfo(String songName) {
        String accessToken = spotifyApiClient.getAccessToken();
        String trackId = spotifySongSearcher.searchSong(accessToken, songName);
        SongInfo songInfo = null;

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
                songInfo = new SongInfo(title, artist, coverArtPath, songLength);
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

        return songInfo;
    }

    public static void main(String args[]) {
        SwingUtilities.invokeLater(() -> {
            LaunchUI ui = new LaunchUI();
            // No song info is set when the UI is launched
        });
    }

    
}
