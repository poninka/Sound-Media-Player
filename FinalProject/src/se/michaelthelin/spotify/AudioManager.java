package se.michaelthelin.spotify;
import java.io.*;
import java.awt.*;
import java.util.Random;
import javax.imageio.ImageIO;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javazoom.jl.decoder.Bitstream;
import javax.swing.border.EmptyBorder;
import javazoom.jl.decoder.Header;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jl.player.advanced.PlaybackEvent;
import javazoom.jl.player.advanced.PlaybackListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.sound.sampled.Clip;


@SuppressWarnings("serial")
public class AudioManager extends JFrame {
	
    String filePath;  // Path of the audio file
    private Clip clip;  // Clip object for handling WAV files
    private Player player;  // Player object for handling MP3 files
    private String fileType;  // Type of the audio file (WAV or MP3)
	private long audioLength;  // Length of the audio file in seconds
    private boolean isPaused ;  // Flag to check if the audio is paused
    private int currentFrame;  // Current frame position for MP3 files
    private long clipTimePosition;  // Position of the clip in microseconds when paused
    private FileInputStream fileInputStream;  // Input stream for the audio file
    private static final String DEFAULT_DIRECTORY = "AudioFiles/UploadedFiles";  // Default directory for file selection
    private static final String UPLOAD_DIRECTORY = "AudioFiles/UploadedFiles";  // Directory for uploading files

    // Getter method for the paused state
    public boolean getPause() {
    	return isPaused;
    }
    // Setter method for the current frame
    public void setCurrentFrame(int frame){
        currentFrame = frame;
    }
    // Constructor to initialize the UI
    public AudioManager() {
        setTitle("Select, Search, or Upload a Song File:");
        setSize(550, 100);
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        setLayout(new GridLayout(1, 3));
        setLocation(1100,700);
	    
	// Panel for selecting a file
        JPanel selectPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        selectPanel.setBackground(Color.darkGray);
        selectPanel.setBorder(new EmptyBorder(10,0,0,0));

	// Button for selecting a file
        JButton selectButton = new JButton("Select File");
        selectButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser(DEFAULT_DIRECTORY);
                int returnValue = fileChooser.showOpenDialog(AudioManager.this);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    String filePath = selectedFile.getAbsolutePath();
                    String fileType = getFileType(filePath);
                    dispose();
                    setSong(filePath,fileType);
                    LaunchUI ui = new LaunchUI();
                    ui.setLocationRelativeTo(null);
                    ui.setVisible(true);
                    ui.connect(filePath);
                    ui.setSongInfo(getTitle(filePath), fileType, setImage(),100);
                    JOptionPane.showMessageDialog(AudioManager.this, "File Selected!" );
                }
            }
        });
        selectPanel.add(selectButton);
        
        JButton spotifyButton = new JButton(loadImage("Assets/SpotifyLogo.png"));
        spotifyButton.setBorder(new EmptyBorder(10,0,0,0));
        spotifyButton.setSize(3, 1);
        spotifyButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                 
                   
                	SwingUtilities.invokeLater(() -> {
               		 SpotifyLaunchUI ui = new SpotifyLaunchUI();
               		 
               		  });
               		
                    dispose();
                    //launchSpotify.
                    //SwingUtilities.invokeLater(() -> {
                     //   LaunchUI  = new LaunchUI();
                        // No song info is set when the UI is launched
                    //});
                }
                	
                }
           // }
        );
        selectPanel.add(spotifyButton);

        JPanel uploadPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        uploadPanel.setBackground(Color.darkGray);
        uploadPanel.setBorder(new EmptyBorder(10,0,0,0));
        JButton uploadButton = new JButton("Upload File");
        uploadButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int returnValue = fileChooser.showOpenDialog(AudioManager.this);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    String fileName = selectedFile.getName();
                    File destinationFile = new File(UPLOAD_DIRECTORY + File.separator + fileName);
                    try {
                        copyFile(selectedFile, destinationFile);
                        JOptionPane.showMessageDialog(AudioManager.this, "File uploaded successfully!");
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(AudioManager.this, "Error uploading file: " + ex.getMessage());
                    }
                }
            }

            public void copyFile(File sourceFile, File destFile) throws IOException {
                InputStream in = new FileInputStream(sourceFile);
                OutputStream out = new FileOutputStream(destFile);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = in.read(buffer)) > 0) {
                    out.write(buffer, 0, length);
                }
                in.close();
                out.close();
            }
        });
        uploadPanel.add(uploadButton);
        
        add(selectPanel, BorderLayout.WEST);
        add(spotifyButton, BorderLayout.CENTER);
        add(uploadPanel, BorderLayout.EAST);
    }   
    
	public void setWav(String inputFilePath) {
		this.filePath = inputFilePath;
	try {
		File musicPath = new File(inputFilePath);
		
		if (musicPath.exists()) {
			AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicPath);
			if (isPaused) {
                clip.setMicrosecondPosition(clipTimePosition);
                
                isPaused = false;
            } else {
			clip = AudioSystem.getClip();
			clip.open(audioInput);

		}} else{JOptionPane.showMessageDialog(null,"Error File Not Found");}
		
	}catch(Exception e) {
		System.out.println(e);
		}
	}

    public void play() throws IOException {
    if (fileType == "WAV") {
        if (clip != null) {
            if (isPaused) {
                clip.setMicrosecondPosition(clipTimePosition);
                clip.start();
                isPaused = false;
            } else {
                clip.start();
            }
        }
    }else {if(fileType == "MP3") {
    	try {
             if (player == null) {
                 fileInputStream = new FileInputStream(filePath);
                 player = new Player(fileInputStream);
                 isPaused = false;
                 new Thread(() -> {
                     try {
                         player.play();
                     } catch (JavaLayerException e) {
                         e.printStackTrace();
                     }
                 }).start();
                 isPaused = false;
             } else {
                 if (isPaused) {
                     fileInputStream = new FileInputStream(filePath);
                     player = new Player(fileInputStream);
                     fileInputStream.skip(currentFrame);
                     new Thread(() -> {
                         try {
                             player.play();
                         } catch (JavaLayerException e) {
                             e.printStackTrace();
                         }
                     }).start();
                     isPaused = false;
                 }
             }
         } catch (FileNotFoundException | JavaLayerException e) {
             e.printStackTrace();
   }
  }
 }
}
 
    public void stop() {
        if (fileType == "WAV") {
        	clip.stop();
        }else {if(fileType == "MP3") {
        	player.close();
        	}
        }
        
    }

    public void pause() throws IOException {
    if (fileType == "WAV") {
        if (clip != null && clip.isRunning()) {
            clipTimePosition = clip.getMicrosecondPosition();
            clip.stop();
            isPaused = true;
        }
    } else {if(fileType == "MP3") {
        if (player != null) {
            if (!isPaused) {
                player.close();
                isPaused = true;
            } else {
                play();
                isPaused = false;
            }
        }
  }
 }
}

    private ImageIcon loadImage(String imagePath){
        try{

            BufferedImage image = ImageIO.read(new File(imagePath));

            return new ImageIcon(image);
        }catch(Exception e){
            e.printStackTrace();
        }

        return null;
    }
    
    public void seek(int seconds) {
    	if (fileType == "WAV") {
    	long microseconds = seconds * 1000;
        if (clip != null && microseconds >= 0 && microseconds <= clip.getMicrosecondLength()) {
            clip.setMicrosecondPosition(microseconds);
        }   
    }else {if(fileType == "MP3") {
    	
        if (player != null) {
            try {
                currentFrame = seconds * 1000;
                fileInputStream.skip(currentFrame);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    	
    	}}
    }
	 
public long getLength(String inputFilePath, String fileType) {
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
			this.audioLength = (long)Seconds;

			//System.out.println(audioLength);
			//System.out.println(fileType);
			}
	
	if (fileType == "MP3") {

	}
	return audioLength;
	
}

public void setSong(String inputFilePath, String fileType) {
	this.filePath = inputFilePath;
	this.fileType = fileType;
	
	if (fileType == "WAV") {
		setWav(filePath);
	}
	if (fileType == "MP3") {
        this.filePath = inputFilePath;
        this.isPaused = false;
        this.currentFrame = 0;
	}
}

	public static String getTitle(String filePath) {
		File file = new File(filePath);
		String fileName = file.getName();
		int dotIndex = fileName.lastIndexOf('.');
		if (dotIndex > 0 && dotIndex < fileName.length() - 1) {
			return fileName.substring(0, dotIndex);
		}
		return fileName;
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
	


	    public String setImage() {
	    	String[] images = new String[10];
		    Random random = new Random();
	       
	        images[0] = "https://www.reddit.com/media?url=https%3A%2F%2Fi.redd.it%2Ffunniest-cat-pictures-you-have-v0-cvk0vuc0hj5a1.jpg%3Fwidth%3D3000%26format%3Dpjpg%26auto%3Dwebp%26s%3D73c395c63462f04c52e1550559dfb9809dd2a599";
	        images[1] = "https://i.pinimg.com/736x/32/7e/db/327edb9a15b304efc264668ada03f725.jpg";
	        images[2] = "https://media.tenor.com/2A0HDP3HSvEAAAAM/very-cat.gif";
	        images[3] = "https://media.tenor.com/f4PUj7wUIm4AAAAM/cat-tongue.gif";
	        images[4] = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSxAovcWUqhjsERX0HasPytNiLHhVvK6hGkzTgArROSaw&s";
	        images[5] = "https://wl-brightside.cf.tsp.li/resize/728x/jpg/e54/e78/30a24451a8ba30d37a3dfa888c.jpg";
	        images[6] = "https://i.pinimg.com/736x/b4/ae/cb/b4aecbe26279a220286cf432da8a7d67.jpg";
	        images[7] = "https://pbs.twimg.com/profile_images/1719004925986758657/971b49Ts_400x400.jpg";
	        images[8] = "https://tr.rbxcdn.com/3bc5c56d710a6b4e0cd2af65e0f6cdf0/420/420/Hat/Webp";
	        images[9] = "https://pbs.twimg.com/media/GHOmEMpWIAA67db.jpg";
		        int index = random.nextInt(images.length);
		        return images[index];
		    }

	   

    
  // Main that controls the INPUT of files and gets the type and can send to the player.
	public static void main(String args[]) {	
		 SwingUtilities.invokeLater(() -> {
	        	LaunchUI ui = new LaunchUI();
	        	
	            String inputFilePath = "AudioFiles/chill.wav";
	        	AudioManager songFile = new AudioManager();
	        	String fileType = songFile.getFileType(inputFilePath);
	        	
	        	songFile.setSong(inputFilePath, fileType.toString());
	        	
	        	ui.setLocationRelativeTo(null);
                ui.setVisible(true);
	        	ui.connect(inputFilePath);
	            ui.setSongInfo("Welcome To the Audio Player!", "",songFile.setImage(),(int)songFile.getLength(inputFilePath, fileType));
	        });
	    }
	}
