import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JOptionPane;
// Establish Audio Player Class which Has a temp main built to show how it works
// - Dan A B
public class AudioPlayer{
	public static void main(String args[]) {
		// Uses string variable to write the filepath but could be replaced with other input
		// These Java audio systems only work with .wav files for no
		String filepath = "MusicTestFile.wav";
		// Calls the created PlayMusic Class to insert the given file path into the controller
		PlayMusic(filepath);
		// Temp way to stop audio as Java Garbage collection stops automatically without something else running
		JOptionPane.showMessageDialog(null,"Press OK to Stop music");
	}

	// PlayMusic Class: 
	// Functions by Testing if file given exists, if it does it will try to play the audio by inserting
	// the file path into the java AudioSystem library with an audioInput Object taking the file input from the 
	// filepath string > musicPath (File Object) > AudioSystem Clip to run the function .open(AudioInput), then .start
	// if try fails it returns File Error
	public static void PlayMusic(String location) {
	try {
		File musicPath = new File(location);
		
		if (musicPath.exists()) {
			AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicPath);
			Clip musicClip = AudioSystem.getClip();
			musicClip.open(audioInput);
			musicClip.start();
		}else {System.out.println("File Error, nothing played");}
		
	}catch(Exception e) {
		System.out.println(e);
		}
	}
}