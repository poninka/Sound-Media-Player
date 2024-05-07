public class PlaybackController {

    private SongDataFetcher songDataFetcher;
    private boolean isPlaying;
    private int songSeekSlider;
    
    public PlaybackController(SongDataFetcher songDataFetcher) 
    {
        this.songDataFetcher = songDataFetcher;
        this.isPlaying = false;
        this.songSeekSlider = 0;
    }
	
    public void togglePlayPause() {
        if (isPlaying) {
            pausePlayback();
        } else {
            startPlayback();
        }
    }
    
    // Code to start playback using the Spotify API
    public void startPlayback() {
       
        // Update the isPlaying flag
        isPlaying = true;
    }
    
    // Code to pause playback using the Spotify API
    public void pausePlayback() {
       
        // Update the isPlaying flag
        isPlaying = false;
    }

//Code not in use for now	
/*	
    // Code to skip to the next track using the Spotify API
    public void forwardTrack() {
   
        // Fetch and display the new track information
        songDataFetcher.fetchAndDisplaySongInfo("new_track_id");
        resetSeekPosition();
    }
    
    // Code to go back to the previous track using the Spotify API
    public void backwardTrack() {
        
        // Fetch and display the new track information
        songDataFetcher.fetchAndDisplaySongInfo("previous_track_id");
        resetSeekPosition();
    }
*/    
	
    // Code to seek to the specified position in the current track using the Spotify API
    private void seekToPosition(int position) {
        
        // Update the songSeekSlider
        songSeekSlider = position;
    }
    
    // Reset the seek position to the beginning of the track
    private void resetSeekPosition() {
    	
    	//resets the songSeekSlider
        songSeekSlider = 0;
    
    }
}
