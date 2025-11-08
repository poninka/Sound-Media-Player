package com.soundmediaplayer.ui;

import java.io.*;
import java.awt.*;
import java.util.Random;
import javax.imageio.ImageIO;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.sound.sampled.Clip;

@SuppressWarnings("serial")
public class AudioManager extends JFrame {
    // Class variables for managing audio files and playback	
    String filePath;  // Path of the audio file
    private Clip clip;  // Clip object for handling WAV files
    private Player player;  // Player object for handling MP3 files
    private String fileType;  // Type of the audio file (WAV or MP3)
    private long audioLength;  // Length of the audio file in seconds
    private boolean isPaused;  // Flag to check if the audio is paused
    private int currentFrame;  // Current frame position for MP3 files
    private long clipTimePosition;  // Position of the clip in microseconds when paused
    private FileInputStream fileInputStream;  // Input stream for the audio file
    
    // Cross-platform directory paths
    private static final Path UPLOAD_DIRECTORY = Paths.get("AudioFiles", "UploadedFiles");

    // Getter method for the paused state
    public boolean getPause() {
        return isPaused;
    }
    
    // Setter method for the current frame
    public void setCurrentFrame(int frame) {
        currentFrame = frame;
    }
    
    // Constructor to initialize the UI
    public AudioManager() {
        setTitle("Select, Search, or Upload a Song File:");
        setSize(550, 100);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setLayout(new GridLayout(1, 3));
        setLocationRelativeTo(null);
	    
        // Panel for selecting a file
        JPanel selectPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        selectPanel.setBackground(Color.darkGray);
        selectPanel.setBorder(new EmptyBorder(10, 0, 0, 0));

        // Button for selecting a file
        JButton selectButton = new JButton("Select File");
        selectButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Path defaultPath = Paths.get(System.getProperty("user.dir"), "AudioFiles", "UploadedFiles");
                File defaultDir = defaultPath.toFile();
                if (!defaultDir.exists()) {
                    defaultDir = new File(System.getProperty("user.dir"));
                }
                
                JFileChooser fileChooser = new JFileChooser(defaultDir);
                int returnValue = fileChooser.showOpenDialog(AudioManager.this);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    String filePath = selectedFile.getAbsolutePath();
                    String fileType = getFileType(filePath);
                    setSong(filePath, fileType);
                    
                    // Switch back to LaunchUI
                    LaunchUI ui = WindowManager.getLaunchUI();
                    ui.connect(filePath);
                    ui.setSongInfo(getTitle(filePath), fileType, setImage(), 100);
                    WindowManager.setCurrentWindow(ui);
                    JOptionPane.showMessageDialog(ui, "File Selected!");
                }
            }
        });
        selectPanel.add(selectButton);

        // Spotify Search button
        JButton spotifyButton = new JButton(loadImageFromResource("/assets/SpotifyLogo.png"));
        spotifyButton.setBorder(new EmptyBorder(10, 0, 0, 0));
        spotifyButton.setSize(3, 1);
        spotifyButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SpotifyLaunchUI ui = WindowManager.getSpotifyLaunchUI();
                WindowManager.setCurrentWindow(ui);
                ui.setLocationRelativeTo(null);
            }
        });
        selectPanel.add(spotifyButton);

        // Upload File button
        JPanel uploadPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        uploadPanel.setBackground(Color.darkGray);
        uploadPanel.setBorder(new EmptyBorder(10, 0, 0, 0));
        JButton uploadButton = new JButton("Upload File");
        uploadButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int returnValue = fileChooser.showOpenDialog(AudioManager.this);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    String fileName = selectedFile.getName();
                    
                    // Create upload directory if it doesn't exist
                    File uploadDir = UPLOAD_DIRECTORY.toFile();
                    if (!uploadDir.exists()) {
                        uploadDir.mkdirs();
                    }
                    
                    File destinationFile = new File(uploadDir, fileName);
                    try {
                        copyFile(selectedFile, destinationFile);
                        JOptionPane.showMessageDialog(AudioManager.this, "File uploaded successfully!");
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(AudioManager.this, "Error uploading file: " + ex.getMessage());
                    }
                }
            }

            // Method to copy file from source to destination
            public void copyFile(File sourceFile, File destFile) throws IOException {
                try (InputStream in = new FileInputStream(sourceFile);
                     OutputStream out = new FileOutputStream(destFile)) {
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = in.read(buffer)) > 0) {
                        out.write(buffer, 0, length);
                    }
                }
            }
        });
        uploadPanel.add(uploadButton);

        // Add panels to the frame
        add(selectPanel, BorderLayout.WEST);
        add(spotifyButton, BorderLayout.CENTER);
        add(uploadPanel, BorderLayout.EAST);
    }
    
    // Method to set WAV file for playback
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
                }
            } else {
                JOptionPane.showMessageDialog(null, "Error File Not Found");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    // Method to play audio file
    public void play() throws IOException {
        if ("WAV".equals(fileType)) {
            if (clip != null) {
                if (isPaused) {
                    clip.setMicrosecondPosition(clipTimePosition);
                    clip.start();
                    isPaused = false;
                } else {
                    clip.start();
                }
            }
        } else if ("MP3".equals(fileType)) {
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

    // Method to stop audio playback
    public void stop() {
        if ("WAV".equals(fileType)) {
            if (clip != null) {
                clip.stop();
            }
        } else if ("MP3".equals(fileType)) {
            if (player != null) {
                player.close();
            }
        }
    }

    // Method to pause audio playback
    public void pause() throws IOException {
        if ("WAV".equals(fileType)) {
            if (clip != null && clip.isRunning()) {
                clipTimePosition = clip.getMicrosecondPosition();
                clip.stop();
                isPaused = true;
            }
        } else if ("MP3".equals(fileType)) {
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

    // Method to load image from resources
    private ImageIcon loadImageFromResource(String resourcePath) {
        try {
            java.net.URL imageUrl = getClass().getResource(resourcePath);
            if (imageUrl != null) {
                BufferedImage image = ImageIO.read(imageUrl);
                return new ImageIcon(image);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Method to seek to a specific position in the audio
    public void seek(int seconds) {
        if ("WAV".equals(fileType)) {
            long microseconds = seconds * 1000L;
            if (clip != null && microseconds >= 0 && microseconds <= clip.getMicrosecondLength()) {
                clip.setMicrosecondPosition(microseconds);
            }
        } else if ("MP3".equals(fileType)) {
            if (player != null) {
                try {
                    currentFrame = seconds * 1000;
                    if (fileInputStream != null) {
                        fileInputStream.skip(currentFrame);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Method to get the length of the audio file
    public long getLength(String inputFilePath, String fileType) {
        this.filePath = inputFilePath;
        this.fileType = fileType;
        
        if ("WAV".equals(fileType)) {
            File file = new File(inputFilePath);
            AudioInputStream audioInputStream = null;
            try {
                audioInputStream = AudioSystem.getAudioInputStream(file);
                AudioFormat format = audioInputStream.getFormat();
                long frames = audioInputStream.getFrameLength();
                double secondsRaw = (frames + 0.0) / format.getFrameRate();
                double seconds = Math.round(secondsRaw * 100.00) / 100.00;
                this.audioLength = (long) seconds;
            } catch (UnsupportedAudioFileException | IOException e) {
                e.printStackTrace();
            } finally {
                if (audioInputStream != null) {
                    try {
                        audioInputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        
        if ("MP3".equals(fileType)) {
            // MP3 length calculation would go here if needed
        }
        return audioLength;
    }

    // Method to set the audio file
    public void setSong(String inputFilePath, String fileType) {
        this.filePath = inputFilePath;
        this.fileType = fileType;
        
        if ("WAV".equals(fileType)) {
            setWav(filePath);
        }
        if ("MP3".equals(fileType)) {
            this.filePath = inputFilePath;
            this.isPaused = false;
            this.currentFrame = 0;
        }
    }

    // Method to get the title of the audio file
    public static String getTitle(String filePath) {
        File file = new File(filePath);
        String fileName = file.getName();
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex > 0 && dotIndex < fileName.length() - 1) {
            return fileName.substring(0, dotIndex);
        }
        return fileName;
    }

    // Method to get the file type of the audio file
    public String getFileType(String inputFilePath) {
        this.filePath = inputFilePath;
        File audioFile = new File(filePath);
        
        String fileName = audioFile.getName().toLowerCase();
        if (fileName.endsWith(".wav")) {
            return "WAV";
        } else if (fileName.endsWith(".mp3")) {
            return "MP3";
        } else {
            return "Unsupported File Type";
        }
    }
    
    // Method to set a random image
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

    // Main method to control the input of files and initialize the player
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LaunchUI ui = WindowManager.getLaunchUI();
            
            Path audioPath = Paths.get("AudioFiles", "chill.wav");
            String inputFilePath = audioPath.toString();
            AudioManager songFile = new AudioManager();
            String fileType = songFile.getFileType(inputFilePath);
            
            songFile.setSong(inputFilePath, fileType);
            
            WindowManager.setCurrentWindow(ui);
            ui.setLocationRelativeTo(null);
            ui.connect(inputFilePath);
            ui.setSongInfo("Welcome To the Audio Player!", "", songFile.setImage(), (int) songFile.getLength(inputFilePath, fileType));
        });
    }
}

