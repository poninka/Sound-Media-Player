package se.michaelthelin.spotify;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class LaunchUI extends JFrame {
    private static final long serialVersionUID = -1702351704098547978L;
    private JLabel titleDashArtist;
    private JLabel coverArtLabel;
    private JSlider songSeekSlider;
    private JButton playPauseButton;
    private JButton selectSongButton;
    private JButton infoButton;
    private JLabel timeLabel;
    private Timer timer;
    private int currentTime;
    private boolean isPaused;
   
	AudioManager AudioFile = new AudioManager();
	
    // Method to connect the UI to the audio file
    public void connect(String inputFilePath) {
    	String fileType = AudioFile.getFileType(inputFilePath);
    	AudioFile.setSong(inputFilePath, fileType);
    }

    // Method to load image
    private ImageIcon loadImage(String imagePath){
        try{

            BufferedImage image = ImageIO.read(new File(imagePath));

            return new ImageIcon(image);
        }catch(Exception e){
            e.printStackTrace();
        }

        return null;
    }

    // Constructor to initialize UI components
    public LaunchUI() {
        setTitle("Kevin From Accounting");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 700);
        

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

        songSeekSlider = new JSlider();
        songSeekSlider.setValue(0);
        songSeekSlider.setMinimum(0);
        songSeekSlider.setPaintTicks(true);
        songSeekSlider.setPaintLabels(true);
        songSeekSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (!songSeekSlider.getValueIsAdjusting()) {
                    int newValue = songSeekSlider.getValue();
                    currentTime = newValue;
                    updateTimerLabel();
                    AudioFile.seek(newValue * 1000);
                }
            }
        });

	// Button to select a new song
        selectSongButton = new JButton(loadImage("Assets/PickSong.png"));
        selectSongButton.addActionListener(new ActionListener() {
               @Override
               public void actionPerformed(ActionEvent e) {
                   SwingUtilities.invokeLater(new Runnable() {
                       public void run() {

                           new AudioManager().setVisible(true);
                           AudioFile.stop();
                           dispose();
                       }
                   });
               }
           });

        // Button to play/pause the song
        playPauseButton = new JButton(loadImage("Assets/Play.png"));
        playPauseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isPaused) {
                	isPaused = false;
                	try {
						AudioFile.play();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
                    System.out.println("SONG IS PLAYING");

                	
                    playPauseButton.setIcon(loadImage("Assets/Pause.png"));;
                    int initialTime = songSeekSlider.getValue();
                    int maxTime = songSeekSlider.getMaximum();
                    startTimer(initialTime, maxTime);
                    updateSliderTime();
                	} else {
                	isPaused = true;
                    playPauseButton.setIcon(loadImage("Assets/Play.png"));
                    try {
						AudioFile.pause();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
                    System.out.println("SONG IS PAUSED");                    
                    stopTimer();
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
        setVisible(true);
    }

    // Method to set song information in the UI
    public void setSongInfo(String title, String artist, String coverArtLink, int songLength) {
        titleDashArtist.setText(title + " - " + artist);
        songSeekSlider.setMaximum(songLength);
        try {
            URL url = new URL(coverArtLink);
            BufferedImage img = ImageIO.read(url);
            Image scaledImage = img.getScaledInstance(300, 300, Image.SCALE_SMOOTH);
            ImageIcon icon = new ImageIcon(scaledImage);
            coverArtLabel.setIcon(icon);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to display additional info
    private void displayInfo() {
    	
    	// ADD EXTRA DATA HERE FOR DESCRPTION OR OTHER GARBAGE
    	
        String infoText = "Just make a string to add extra info here";
        JOptionPane.showMessageDialog(this, infoText, "Information", JOptionPane.INFORMATION_MESSAGE);
    }

    // Method to update slider time
    private void updateSliderTime() {
        int value = songSeekSlider.getValue();
        currentTime = value;
        int minutes = value / 60;
        int seconds = value % 60;
        String timeString = String.format("%d:%02d", minutes, seconds);
        timeLabel.setText(timeString);
    }

    // Method to start timer
    private void startTimer(int initialTime, int maxTime) {
        currentTime = initialTime;
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentTime < maxTime) {
                    currentTime++;
                    updateTimerLabel();
                } else {
                    stopTimer();
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
    }
    
}
