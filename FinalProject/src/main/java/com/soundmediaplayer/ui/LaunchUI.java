package com.soundmediaplayer.ui;

import com.soundmediaplayer.ui.components.PlaybackSlider;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class LaunchUI extends JFrame {
    private static final long serialVersionUID = -1702351704098547978L;
    private JLabel titleDashArtist;
    private JLabel coverArtLabel;
    private PlaybackSlider songSeekSlider;
    private JButton playPauseButton;
    private JButton selectSongButton;
    private JButton infoButton;
    private JLabel timeLabel;
    private Timer timer;
    private int currentTime;
    private boolean isPaused = true;
    private boolean isScrubbing;
    private boolean resumeAfterScrub;
   
    AudioManager audioFile = new AudioManager();
    
    // Method to connect the UI to the audio file
    public void connect(String inputFilePath) {
        String fileType = audioFile.getFileType(inputFilePath);
        audioFile.setSong(inputFilePath, fileType);
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

    // Constructor to initialize UI components
    public LaunchUI() {
        setTitle("Sound Media Player");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 700);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(new java.awt.Color(0, 0, 0, 220));
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(25, 25, 25, 25));
        
        titleDashArtist = new JLabel("Error");
        titleDashArtist.setForeground(Color.white);
        titleDashArtist.setHorizontalAlignment(SwingConstants.CENTER);
        titleDashArtist.setFont(new Font("Verdana", Font.BOLD, 20));

        coverArtLabel = new JLabel();
        coverArtLabel.setHorizontalAlignment(SwingConstants.CENTER);

        songSeekSlider = new PlaybackSlider();
        songSeekSlider.setValue(0);
        songSeekSlider.setMinimum(0);
        songSeekSlider.setPaintTicks(true);
        songSeekSlider.setPaintLabels(true);
        songSeekSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (songSeekSlider.isProgrammaticChange()) {
                    return;
                }
                currentTime = songSeekSlider.getValue();
                updateTimerLabel();
                if (!songSeekSlider.getValueIsAdjusting() && !isScrubbing) {
                    beginScrub();
                    finalizeScrub();
                }
            }
        });
        songSeekSlider.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                beginScrub();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                finalizeScrub();
            }
        });

        // Button to select a new song
        selectSongButton = new JButton(loadImageFromResource("/assets/PickSong.png"));
        selectSongButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Stop current playback
                audioFile.stop();
                // Switch to AudioManager for file selection
                WindowManager.showAudioManager();
            }
        });

        // Button to play/pause the song
        playPauseButton = new JButton(loadImageFromResource("/assets/Play.png"));
        playPauseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isPaused) {
                    playCurrentTrack();
                } else {
                    pauseCurrentTrack();
                }
            }
        });

        // Info button
        infoButton = new JButton("Info");
        infoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayInfo();
            }
        });

        timeLabel = new JLabel("0:00");
        timeLabel.setForeground(Color.black);
        timeLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel controlPanel = new JPanel(new BorderLayout());
        controlPanel.add(playPauseButton, BorderLayout.EAST);
        controlPanel.add(selectSongButton, BorderLayout.CENTER);
        controlPanel.add(infoButton, BorderLayout.WEST);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(controlPanel, BorderLayout.NORTH);
        bottomPanel.add(songSeekSlider, BorderLayout.CENTER);
        bottomPanel.add(timeLabel, BorderLayout.SOUTH);

        mainPanel.add(titleDashArtist, BorderLayout.NORTH);
        mainPanel.add(coverArtLabel, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    /**
     * Loads a song selected from the AudioManager window and starts playback immediately.
     */
    public void loadSongFromSelection(String inputFilePath) {
        if (inputFilePath == null || inputFilePath.isBlank()) {
            return;
        }
        String fileType = audioFile.getFileType(inputFilePath);
        audioFile.setSong(inputFilePath, fileType);
        long rawLength = audioFile.getLength(inputFilePath, fileType);
        int songLength = (int) Math.max(1, rawLength);
        setSongInfo(AudioManager.getTitle(inputFilePath), fileType, audioFile.setImage(), songLength);
        playFromBeginning();
    }

    // Method to set song information in the UI
    public void setSongInfo(String title, String artist, String coverArtLink, int songLength) {
        titleDashArtist.setText(title + " - " + artist);
        songSeekSlider.setMaximum(Math.max(1, songLength));
        songSeekSlider.setProgrammaticValue(0);
        currentTime = 0;
        stopTimer();
        isPaused = true;
        playPauseButton.setIcon(loadImageFromResource("/assets/Play.png"));
        updateTimerLabel();
        applyCoverArt(coverArtLink);
    }

    private void playFromBeginning() {
        pauseCurrentTrack();
        songSeekSlider.setProgrammaticValue(0);
        currentTime = 0;
        playCurrentTrack();
    }

    private void playCurrentTrack() {
        if (!audioFile.hasLoadedSong() || !isPaused) {
            return;
        }
        try {
            audioFile.seek(currentTime * 1000);
            audioFile.play();
            isPaused = false;
            playPauseButton.setIcon(loadImageFromResource("/assets/Pause.png"));
            startTimer(currentTime, songSeekSlider.getMaximum());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void pauseCurrentTrack() {
        if (isPaused || !audioFile.hasLoadedSong()) {
            return;
        }
        playPauseButton.setIcon(loadImageFromResource("/assets/Play.png"));
        try {
            audioFile.pause();
            audioFile.seek(currentTime * 1000);
        } catch (IOException e) {
            e.printStackTrace();
        }
        isPaused = true;
        stopTimer();
    }

    private void beginScrub() {
        if (isScrubbing) {
            return;
        }
        isScrubbing = true;
        resumeAfterScrub = !isPaused && audioFile.hasLoadedSong();
        if (resumeAfterScrub) {
            pauseCurrentTrack();
        } else {
            stopTimer();
        }
    }

    private void finalizeScrub() {
        if (!isScrubbing) {
            return;
        }
        if (audioFile.hasLoadedSong()) {
            audioFile.seek(songSeekSlider.getValue() * 1000);
        }
        currentTime = songSeekSlider.getValue();
        updateTimerLabel();
        if (resumeAfterScrub) {
            playCurrentTrack();
        }
        resumeAfterScrub = false;
        isScrubbing = false;
    }

    private void applyCoverArt(String coverArtLink) {
        ImageIcon icon = loadImageFromUrl(coverArtLink);
        if (icon == null) {
            icon = loadImageFromUrl(audioFile.setImage());
        }
        if (icon != null) {
            coverArtLabel.setText(null);
            coverArtLabel.setIcon(icon);
        } else {
            coverArtLabel.setIcon(null);
            coverArtLabel.setText("No Artwork Available");
            coverArtLabel.setForeground(Color.white);
        }
    }

    private ImageIcon loadImageFromUrl(String coverArtLink) {
        if (coverArtLink == null || coverArtLink.isBlank()) {
            return null;
        }
        try {
            URL url = new URL(coverArtLink);
            BufferedImage img = ImageIO.read(url);
            if (img == null) {
                return null;
            }
            Image scaledImage = img.getScaledInstance(300, 300, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImage);
        } catch (IOException e) {
            System.err.println("Unable to load cover art: " + e.getMessage());
            return null;
        }
    }

    // Method to display additional info
    private void displayInfo() {
        String infoText = "Sound Media Player - A Java application for playing local audio files and searching Spotify tracks.";
        JOptionPane.showMessageDialog(this, infoText, "Information", JOptionPane.INFORMATION_MESSAGE);
    }

    // Method to start timer
    private void startTimer(int initialTime, int maxTime) {
        stopTimer();
        currentTime = initialTime;
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentTime < maxTime) {
                    currentTime++;
                    updateTimerLabel();
                } else {
                    stopTimer();
                    isPaused = true;
                    playPauseButton.setIcon(loadImageFromResource("/assets/Play.png"));
                }
            }
        });
        timer.start();
    }

    // Method to stop timer
    private void stopTimer() {
        if (timer != null) {
            timer.stop();
        }
    }

    // Method to update timer label
    private void updateTimerLabel() {
        int minutes = currentTime / 60;
        int seconds = currentTime % 60;
        String timeString = String.format("%d:%02d", minutes, seconds);
        timeLabel.setText(timeString);
        if (!songSeekSlider.getValueIsAdjusting() && !isScrubbing) {
            songSeekSlider.setProgrammaticValue(currentTime);
        }
    }
}
