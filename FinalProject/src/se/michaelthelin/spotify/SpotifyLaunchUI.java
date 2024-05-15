package se.michaelthelin.spotify;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import javax.swing.border.EmptyBorder;

import se.michaelthelin.spotify.SongDataFetcher.SongInfo;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.awt.Image;

public class SpotifyLaunchUI extends JFrame {
   private static final long serialVersionUID = -1702351704098547978L;
   private JLabel titleDashArtist;
   private JLabel coverArtLabel;
   private JTextField searchBar; // Added JTextField for search bar

   // Constructor to initialize UI components
   public SpotifyLaunchUI() {
       setTitle("Kevin From Accounting");
       setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       setSize(500, 700);

       JPanel mainPanel = new JPanel();
       mainPanel.setBackground(new java.awt.Color(0,0,0,220));
       mainPanel.setLayout(new BorderLayout());
       mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
       
       titleDashArtist = new JLabel("Spotify - Search");
       titleDashArtist.setForeground(Color.white);
       titleDashArtist.setHorizontalAlignment(SwingConstants.CENTER);
       titleDashArtist.setFont(new Font("Verdana", Font.BOLD, 20));
       
       coverArtLabel = new JLabel();
       coverArtLabel.setHorizontalAlignment(SwingConstants.CENTER);

       searchBar = new JTextField();
       searchBar.addActionListener(new ActionListener() {
    	    public void actionPerformed(ActionEvent e) {
    	        String songName = searchBar.getText();
    	        SongDataFetcher songDataFetcher = new SongDataFetcher();
    	        SongInfo songInfo = songDataFetcher.fetchAndDisplaySongInfo(songName);
    	        if (songInfo != null) {
    	            setSongInfo(songInfo.title, songInfo.artist, songInfo.coverArtPath);
    	        }
    	    }
    	});



       mainPanel.add(titleDashArtist, BorderLayout.NORTH); 
       mainPanel.add(coverArtLabel, BorderLayout.CENTER);
       mainPanel.add(searchBar, BorderLayout.SOUTH);

       add(mainPanel);
       setVisible(true);
   }

   // Method to set song information in the UI
   public void setSongInfo(String title, String artist, String coverArtLink) {
       titleDashArtist.setText(title + " - " + artist);
      
       try {
           @SuppressWarnings("deprecation")
		URL url = new URL(coverArtLink);
           BufferedImage img = ImageIO.read(url);
           Image scaledImage = img.getScaledInstance(300, 300, Image.SCALE_SMOOTH);
           ImageIcon icon = new ImageIcon(scaledImage);
           coverArtLabel.setIcon(icon);
       } catch (IOException e) {
           e.printStackTrace();
       }
   
   }

// Method to launch the main UI
public void launchUI2() {
	SwingUtilities.invokeLater(() -> {
		 LaunchUI ui = new LaunchUI();
		 
		  });
		}

}
