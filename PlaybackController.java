//Test - Daguilar0123 made changes to this file on my mac on eclipse
public class PlaybackController {
	
	 public int audioLength; 
	 public int playbackStartingTime; 
	 public boolean pausePlayBool;
	 
	 private int userChoice;
	 
	 public boolean isPaused(int userChoice) {
		 if(userChoice == 0) {
			 pausePlayBool = true;
		 }
		 else {
			 pausePlayBool = false;
		 }
		 
		 return pausePlayBool;
	 }

	 public void seek() {
		 
		  currentPos = playbackStartingTime;
		 
		 while(currentPos < audioLength) {
			 currentPos++;
	 }
}

