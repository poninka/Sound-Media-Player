import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JOptionPane;
import javax.sound.sampled.Clip;

public class AudioPlayer {
	
	// Audio File Filter marks String to WAV or MP3 depending on 
	// corresponding file type.
    String filePath;
    private String fileType;
	private int audioLength;
	
	public void playWav(String inputFilePath) {
		this.filePath = inputFilePath;
	try {
		File musicPath = new File(inputFilePath);
		
		if (musicPath.exists()) {
			AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicPath);
			Clip musicClip = AudioSystem.getClip();
			musicClip.open(audioInput);
			musicClip.start();
			JOptionPane.showMessageDialog(null,"Press OK to Stop music");
		}else{JOptionPane.showMessageDialog(null,"Error File Not Found");}
		
	}catch(Exception e) {
		System.out.println(e);
		}
	}


public void getLength(String inputFilePath, String fileType) {
	this.filePath = inputFilePath;
	this.fileType = fileType;
	
		if (fileType == "WAV") {
			File file = new File(inputFilePath);
			AudioInputStream audioInputStream = null;
			try {
				audioInputStream = AudioSystem.getAudioInputStream(file);
			} catch (UnsupportedAudioFileException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			AudioFormat format = audioInputStream.getFormat();
			long frames = audioInputStream.getFrameLength();
			double SecondsRaw = (frames+0.0) / format.getFrameRate();
			double Seconds = Math.round(SecondsRaw * 100.00) / 100.00;
			this.audioLength = (int)Seconds;

			System.out.println(audioLength);
			System.out.println(fileType);
			}
	
	if (fileType == "MP3") {
		System.out.println("MP3 LENGTH NOT FINISHED");
		System.out.println(fileType);
	}
}

public void playSong(String inputFilePath, String fileType) {
	this.filePath = inputFilePath;
	this.fileType = fileType;
	
	if (fileType == "WAV") {
		playWav(filePath);
	}
	if (fileType == "MP3") {
		System.out.println("PLAYBACK NOT FINISHED FOR MP3");
		System.out.println(fileType);
	}
}

	
    public String getFileType(String inputFilePath) {
        this.filePath = inputFilePath;
        File audioFile = new File(filePath);
        
        FileFilter wavFileFilter = new FileFilter() {
            @Override
            public boolean accept(File file) {
                return file.getName().toLowerCase().endsWith(".wav");
            }
        };
        
        FileFilter mp3FileFilter = new FileFilter() {
            @Override
            public boolean accept(File file) {
                return file.getName().toLowerCase().endsWith(".mp3");
            }
        };

        if (wavFileFilter.accept(audioFile)) {
            return "WAV";
        } else if (mp3FileFilter.accept(audioFile)) {
            return "MP3";
        } else {
            return "Unsupported File Type";
        }
    } 
    
  // Main that controls the INPUT of files and gets the type and can send to the player.
	public static void main(String args[]) {	
		
		String inputFilePath = "AudioFiles/rock.wav";
		
		AudioPlayer AudioFile = new AudioPlayer();
		String fileType = AudioFile.getFileType(inputFilePath);
		

		AudioFile.getLength(inputFilePath,fileType.toString());
		AudioFile.playSong(inputFilePath, fileType.toString());
		
		System.out.println();

	}
}
