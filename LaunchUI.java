import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import javax.swing.Timer;

public class LaunchUI extends JFrame {
    private static final long serialVersionUID = -1702351704098547978L;
    private JLabel titleDashArtist;
    private JLabel coverArtLabel;
    private JSlider songSeekSlider;
    private JButton playPauseButton;
    private JButton infoButton;
    private JLabel timeLabel;
    private Timer timer;
    private int currentTime;

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
                updateSliderTime();
            }
        });

        playPauseButton = new JButton("Play");
        playPauseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (playPauseButton.getText().equals("Play")) {
                	
                    //System.out.println("this plays when the song starts playing");
                	//add the code that starts the song playing
                	
                    playPauseButton.setText("Pause");
                    int initialTime = songSeekSlider.getValue();
                    int maxTime = songSeekSlider.getMaximum();
                    startTimer(initialTime, maxTime);
                } else {
                    playPauseButton.setText("Play");
                    
                   // System.out.println("this plays when the song is paused");
                   //add the code that pauses the song from playing
                    
                    stopTimer();
                }
            }
        });

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

    private void displayInfo() {
        String infoText = "Just make a string to add extra info here";
        JOptionPane.showMessageDialog(this, infoText, "Information", JOptionPane.INFORMATION_MESSAGE);
    }

    private void updateSliderTime() {
        int value = songSeekSlider.getValue();
        currentTime = value;
        int minutes = value / 60;
        int seconds = value % 60;
        String timeString = String.format("%d:%02d", minutes, seconds);
        timeLabel.setText(timeString);
    }
    
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
    
    private void stopTimer() {
        if (timer != null) {
            timer.stop();
        }
    }
    
    private void updateTimerLabel() {
        int minutes = currentTime / 60;
        int seconds = currentTime % 60;
        String timeString = String.format("%d:%02d", minutes, seconds);
        timeLabel.setText(timeString);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LaunchUI ui = new LaunchUI();
            ui.setSongInfo("The Walls", "Chase Atlantic", "https://pbs.twimg.com/media/FBl3p9wWYAEE0VJ.jpg:large", 200);
        });
    }
}
